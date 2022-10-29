package com.example.prepear;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddEditRecipeActivity extends AppCompatActivity implements RecipeEditIngredientFragment.OnFragmentInteractionListener, RecipeAddIngredientFragment.OnFragmentInteractionListener{
    private ImageView imageImageView;
    private FloatingActionButton editImageButton;
    private EditText titleEditText;
    private EditText preparationTimeEditText;
    private EditText numberOfServingsEditText;
    private EditText recipeCategoryEditText;
    private EditText commentsEditText;
    private Button addIngredientInRecipeButton;
    private ListView ingredientInRecipeListView;
    private Button commitButton;
    private Button cancelButton;
    private ArrayAdapter<IngredientInRecipe> ingredientInRecipeArrayAdapter;
    private ArrayList<IngredientInRecipe> ingredientInRecipeDataList;
    private Recipe viewedRecipe;
    private String linkOfImage;
    StorageReference storageReference;
    final int IMAGE_REQUEST = 1;
    Uri imageLocationPath;
    FirebaseFirestore db;
    private boolean pictureSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        editImageButton = findViewById(R.id.edit_image_button);
        imageImageView = findViewById(R.id.imageView);
        titleEditText = findViewById(R.id.title_EditText);
        preparationTimeEditText = findViewById(R.id.preparation_time_EditText);
        numberOfServingsEditText = findViewById(R.id.number_of_servings_EditText);
        recipeCategoryEditText = findViewById(R.id.recipe_category_EditText);
        commentsEditText = findViewById(R.id.comments_EditText);
        addIngredientInRecipeButton = findViewById(R.id.add_ingredient_in_recipe_button);
        ingredientInRecipeListView = findViewById(R.id.ingredient_in_recipe_ListView);
        commitButton = findViewById(R.id.commit_button);
        cancelButton = findViewById(R.id.cancel_button);

        final String TAG = "Recipes";

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("imageFolder");


        if (getIntent().getStringExtra("calling activity").equals("2")) {
            viewedRecipe = (Recipe) getIntent().getSerializableExtra("viewed recipe");
            linkOfImage = viewedRecipe.getImageURI();
            Glide.with(AddEditRecipeActivity.this)
                    .load(linkOfImage).into(imageImageView);
            titleEditText.setText(viewedRecipe.getTitle());
            preparationTimeEditText.setText(viewedRecipe.getPreparationTime().toString());
            numberOfServingsEditText.setText(viewedRecipe.getNumberOfServings().toString());
            recipeCategoryEditText.setText(viewedRecipe.getRecipeCategory());
            commentsEditText.setText(viewedRecipe.getComments());
            ingredientInRecipeDataList = viewedRecipe.getListOfIngredients();
        } else {
            ingredientInRecipeDataList = new ArrayList<>();
        }



        ingredientInRecipeArrayAdapter = new CustomIngredientInRecipeList(this, ingredientInRecipeDataList);
        ingredientInRecipeListView.setAdapter(ingredientInRecipeArrayAdapter);



        addIngredientInRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecipeAddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT_IN_RECIPE");
            }
        });


        ingredientInRecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeEditIngredientFragment.newInstance(ingredientInRecipeArrayAdapter.getItem(position)).show(getSupportFragmentManager(), "EDIT_INGREDIENT_IN_RECIPE");
            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("calling activity").equals("1")) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    public void onConfirmPressed(IngredientInRecipe ingredientToAdd) {
        ingredientInRecipeArrayAdapter.add(ingredientToAdd);
    }

    @Override
    public void onOkPressed(IngredientInRecipe ingredient) {};

    public void onDeletePressed(IngredientInRecipe ingredientToDelete) {
        ingredientInRecipeArrayAdapter.remove(ingredientToDelete);
    }

    public void selectImage(View view) {
        Intent objectIntent = new Intent();
        objectIntent.setType("image/*");

        objectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(objectIntent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageLocationPath = data.getData();
                Bitmap objectBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocationPath);

                imageImageView.setImageBitmap(objectBitmap);
                pictureSelected = true;
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private String getExtension (Uri uri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

            return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void uploadRecipe (View view) {
        if (pictureSelected == false) {
            HashMap<String, Object> data = new HashMap<>();

            final String title = titleEditText.getText().toString();
            final Number preparationTime = Integer.parseInt(preparationTimeEditText.getText().toString());
            final Number numberOfServings = Integer.parseInt(numberOfServingsEditText.getText().toString());
            final String recipeCategory = recipeCategoryEditText.getText().toString();
            final String comments = commentsEditText.getText().toString();
            final ArrayList<IngredientInRecipe> listOfIngredients = ingredientInRecipeDataList;
            final String imageURI = linkOfImage;

            if (title.equals("") || preparationTime.equals("") || numberOfServings.equals("")
                    || recipeCategory.equals("") || comments.equals("")) {
                Toast.makeText(getApplicationContext(), "You did not enter the full information, add/edit failed.", Toast.LENGTH_LONG).show();
            } else {
                data.put("Image URI", imageURI);
                data.put("Preparation Time", preparationTime);
                data.put("Number of Servings", numberOfServings);
                data.put("Recipe Category", recipeCategory);
                data.put("Comments", comments);

                db
                        .collection("Recipes")
                        .document(title)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddEditRecipeActivity.this, "Recipe is uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddEditRecipeActivity.this, "Fails to upload recipe", Toast.LENGTH_SHORT).show();
                            }
                        });

                Intent returnIntent = new Intent();
                returnIntent.putExtra("new recipe", new Recipe(imageURI, title, preparationTime.intValue(),
                        numberOfServings.intValue(), recipeCategory, comments));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        } else {
            try {
                if (!titleEditText.getText().toString().isEmpty() && imageLocationPath != null) {
                    String nameOfImage = titleEditText.getText().toString() + "." + getExtension(imageLocationPath);
                    final StorageReference imageRef = storageReference.child(nameOfImage);

                    UploadTask objectUploadTask = imageRef.putFile(imageLocationPath);
                    objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                HashMap<String, Object> data = new HashMap<>();

                                final String title = titleEditText.getText().toString();
                                final Number preparationTime = Integer.parseInt(preparationTimeEditText.getText().toString());
                                final Number numberOfServings = Integer.parseInt(numberOfServingsEditText.getText().toString());
                                final String recipeCategory = recipeCategoryEditText.getText().toString();
                                final String comments = commentsEditText.getText().toString();
                                final ArrayList<IngredientInRecipe> listOfIngredients = ingredientInRecipeDataList;
                                final String imageURI = task.getResult().toString();

                                if (title.equals("") || preparationTime.equals("") || numberOfServings.equals("")
                                        || recipeCategory.equals("") || comments.equals("")) {
                                    Toast.makeText(getApplicationContext(), "You did not enter the full information, add/edit failed.", Toast.LENGTH_LONG).show();
                                } else {
                                    data.put("Image URI", imageURI);
                                    data.put("Preparation Time", preparationTime);
                                    data.put("Number of Servings", numberOfServings);
                                    data.put("Recipe Category", recipeCategory);
                                    data.put("Comments", comments);

                                    db
                                            .collection("Recipes")
                                            .document(title)
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(AddEditRecipeActivity.this, "Recipe is uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddEditRecipeActivity.this, "Fails to upload recipe", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("new recipe", new Recipe(imageURI, title, preparationTime.intValue(),
                                            numberOfServings.intValue(), recipeCategory, comments));
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            } else if (!task.isSuccessful()) {
                                Toast.makeText(AddEditRecipeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide name for image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
