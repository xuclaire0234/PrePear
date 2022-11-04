/**
 * Classname: AddEditRecipeActivity
 * Version Information: 1.0.0
 * Date: 11/2/2022
 * Author: Jiayin He
 * Copyright Notice:
 */

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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class defines the add/edit recipe activity that allows user to either add a new recipe or
 * edit a existing recipe.
 */
public class AddEditRecipeActivity extends AppCompatActivity implements RecipeEditIngredientFragment.OnFragmentInteractionListener,
        RecipeAddIngredientFragment.OnFragmentInteractionListener{
    private ArrayAdapter<CharSequence> recipeCategorySpinnerAdapter;
    private ImageView imageImageView;
    private FloatingActionButton editImageButton;
    private EditText titleEditText;
    private EditText preparationTimeEditText;
    private EditText numberOfServingsEditText;
    private Spinner recipeCategorySpinner;
    private EditText commentsEditText;
    private Button addIngredientInRecipeButton;
    private ListView ingredientInRecipeListView;
    private Button commitButton;
    private Button cancelButton;
    private ArrayAdapter<IngredientInRecipe> ingredientInRecipeArrayAdapter;
    private ArrayList<IngredientInRecipe> ingredientInRecipeDataList;
    private Recipe viewedRecipe;
    private String linkOfImage;
    private StorageReference storageReference;
    private final int IMAGE_REQUEST = 1;
    private Uri imageLocationPath;
    private boolean pictureSelected;
    private Integer positionToEditInViewIngredient = -1;
    private ArrayList<String> editDeleteListSaved;
    private String idOfRecipe;
    private Boolean isEditedRecipe;

    /**
     * This creates the AddEditRecipeActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        /* connects the layout with the views and buttons */
        editImageButton = findViewById(R.id.edit_image_button);
        imageImageView = findViewById(R.id.imageView);
        titleEditText = findViewById(R.id.title_EditText);
        preparationTimeEditText = findViewById(R.id.preparation_time_EditText);
        numberOfServingsEditText = findViewById(R.id.number_of_servings_EditText);
        recipeCategorySpinner = findViewById(R.id.recipe_category_Spinner);
        commentsEditText = findViewById(R.id.comments_EditText);
        addIngredientInRecipeButton = findViewById(R.id.add_ingredient_in_recipe_button);
        ingredientInRecipeListView = findViewById(R.id.ingredient_in_recipe_ListView);
        commitButton = findViewById(R.id.commit_button);
        cancelButton = findViewById(R.id.cancel_button);

        /* sets up recipe category spinner */
        recipeCategorySpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.recipe_category,
                android.R.layout.simple_spinner_item);
        recipeCategorySpinner.setAdapter(recipeCategorySpinnerAdapter);
        recipeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* connects to firebase storage */
        storageReference = FirebaseStorage.getInstance().getReference("imageFolder");

        // ???
        editDeleteListSaved = new ArrayList<String>();

        if (getIntent().getStringExtra("calling activity").equals("2")) {
            /* If the calling activity is ViewRecipeActivity, display the information of the viewing recipe. */
            isEditedRecipe = Boolean.TRUE;
            viewedRecipe = (Recipe) getIntent().getSerializableExtra("viewed recipe");
            idOfRecipe = viewedRecipe.getId();
            linkOfImage = viewedRecipe.getImageURI();
            Glide.with(AddEditRecipeActivity.this)
                    .load(linkOfImage).into(imageImageView);
            titleEditText.setText(viewedRecipe.getTitle());
            preparationTimeEditText.setText(viewedRecipe.getPreparationTime().toString());
            numberOfServingsEditText.setText(viewedRecipe.getNumberOfServings().toString());
            recipeCategorySpinner.setSelection(recipeCategorySpinnerAdapter.getPosition(viewedRecipe.getRecipeCategory()));
            commentsEditText.setText(viewedRecipe.getComments());
            ingredientInRecipeDataList = viewedRecipe.getListOfIngredients();
        } else {
            /* If the calling activity is ViewRecipeListActivity, prompt user to add a new recipe */
            isEditedRecipe = Boolean.FALSE;
            ingredientInRecipeDataList = new ArrayList<>();
            idOfRecipe = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        }
        ingredientInRecipeArrayAdapter = new CustomIngredientInRecipeList(this, ingredientInRecipeDataList);
        ingredientInRecipeListView.setAdapter(ingredientInRecipeArrayAdapter);

        /* sets add ingredient button to direct to RecipeAddIngredientFragment */
        addIngredientInRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionToEditInViewIngredient = -1;
                new RecipeAddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT_IN_RECIPE");
            }
        });

        /* sets each ingredient object on listview to direct to RecipeEditIngredientFragment */
        ingredientInRecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionToEditInViewIngredient = position;
                RecipeEditIngredientFragment.newInstance(ingredientInRecipeArrayAdapter.getItem(position)).show(getSupportFragmentManager(),
                        "EDIT_INGREDIENT_IN_RECIPE");
            }
        });

        /* sets cancel button to directly return to the ViewRecipeActivity after clicking it */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("calling activity").equals("1")) {
                    /* if the calling activity is ViewRecipeListActivity */
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                } else {
                    /* if the calling activity is ViewRecipeActivity */
                    finish();
                }
            }
        });
    }

    /**
     * This adds a new ingredient into ingredient in recipe list.
     * @param ingredientToAdd
     *      This is the ingredient to add which is of type {@link IngredientInRecipe}
     */
    public void onConfirmPressed(IngredientInRecipe ingredientToAdd) {
        String ingredientId = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        ingredientToAdd.setId(ingredientId);
        ingredientInRecipeDataList.add(ingredientToAdd);
        ingredientInRecipeArrayAdapter.notifyDataSetChanged();
        positionToEditInViewIngredient = -1;
    }

    /**
     * This edits the information of the ingredient being selected inside ingredient in recipe list.
     * @param ingredient
     *      This is the ingredient to edit the information which is of type {@link IngredientInRecipe}
     */
    @Override
    public void onOkPressed(IngredientInRecipe ingredient) {
        if (positionToEditInViewIngredient != -1) {
            editDeleteListSaved.add(ingredientInRecipeDataList.get(positionToEditInViewIngredient).getId());
            ingredientInRecipeArrayAdapter.remove(ingredientInRecipeDataList.get(positionToEditInViewIngredient));
            String ingredientId = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            ingredient.setId(ingredientId);
            ingredientInRecipeArrayAdapter.add(ingredient);
            positionToEditInViewIngredient = -1;
        }
    };

    /**
     * This deletes the ingredient being selected from ingredient in recipe list.
     * @param ingredientToDelete
     *      This is the ingredient to be deleted which is of type {@link IngredientInRecipe}
     */
    public void onDeletePressed(IngredientInRecipe ingredientToDelete) {
        editDeleteListSaved.add(ingredientInRecipeDataList.get(positionToEditInViewIngredient).getId());
        ingredientInRecipeArrayAdapter.remove(ingredientInRecipeDataList.get(positionToEditInViewIngredient));
        ingredientInRecipeArrayAdapter.notifyDataSetChanged();
        positionToEditInViewIngredient = -1;
    }

    /**
     * This allows user to select a picture from local storage.
     * @param view
     *      This represents the current view.
     */
    public void selectImage(View view) {
        Intent objectIntent = new Intent();
        objectIntent.setType("image/*");

        objectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(objectIntent,IMAGE_REQUEST);
    }

    /**
     * This gets the picture being selected by the user and display it on ImageView.
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * This returns the part of the path to the image.
     * @param uri
     *      This is the uri of the image of type {@link Uri}
     * @return
     */
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

    /**
     * This uploads the recipe to the recipe list while at the same time sends the added recipe to
     * the ViewRecipeListActivity.
     * @param view
     *      This represents the current view.
     */
    public void uploadRecipe (View view) {
        if (pictureSelected == false) {
            // if no new picture being selected
            // gets the information of recipe being edited/added
            final String title = titleEditText.getText().toString();
            final Number preparationTime = Integer.parseInt(preparationTimeEditText.getText().toString());
            final Number numberOfServings = Integer.parseInt(numberOfServingsEditText.getText().toString());
            final String recipeCategory = recipeCategorySpinner.getSelectedItem().toString();
            final String comments = commentsEditText.getText().toString();
            final String imageURI = linkOfImage;

            // checks if there is any necessary information missing
            if (title.equals("") || preparationTime.equals("") || numberOfServings.equals("")
                    || recipeCategory.equals("")) {
                Toast.makeText(getApplicationContext(), "You did not enter the full information, add/edit failed.", Toast.LENGTH_LONG).show();
            } else {
                // add to database
                Recipe newRecipe = new Recipe(imageURI, title, preparationTime.intValue(),
                        numberOfServings.intValue(), recipeCategory, comments);
                DatabaseController databaseController = new DatabaseController();
                databaseController.addEditRecipeToRecipeList(AddEditRecipeActivity.this, newRecipe, ingredientInRecipeDataList, editDeleteListSaved, idOfRecipe);

                // sends new recipe being added back to the ViewRecipeListActivity and return
                Intent returnIntent = new Intent();
                returnIntent.putExtra("new recipe", newRecipe);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        } else {
            /* if new picture has been selected */
            try {
                /* check to see if a valid name has been provided */
                if (!titleEditText.getText().toString().isEmpty() && imageLocationPath != null) {
                    String nameOfImage = titleEditText.getText().toString() + "." + getExtension(imageLocationPath);
                    final StorageReference imageRef = storageReference.child(nameOfImage);

                    /* store the new picture to the image folder in storage */
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
                                /* gets the detailed information being entered by user */
                                final String title = titleEditText.getText().toString();
                                final Number preparationTime = Integer.parseInt(preparationTimeEditText.getText().toString());
                                final Number numberOfServings = Integer.parseInt(numberOfServingsEditText.getText().toString());
                                final String recipeCategory = recipeCategorySpinner.getSelectedItem().toString();
                                final String comments = commentsEditText.getText().toString();
                                final String imageURI = task.getResult().toString();

                                /* check to see if there is any necessary information not been entered */
                                if (title.equals("") || preparationTime.equals("") || numberOfServings.equals("")
                                        || recipeCategory.equals("")) {
                                    Toast.makeText(getApplicationContext(), "You did not enter the full information, add/edit failed.", Toast.LENGTH_LONG).show();
                                } else {
                                    /* add to database */
                                    Recipe newRecipe = new Recipe(imageURI, title, preparationTime.intValue(),
                                            numberOfServings.intValue(), recipeCategory, comments);
                                    DatabaseController databaseController = new DatabaseController();
                                    databaseController.addEditRecipeToRecipeList(AddEditRecipeActivity.this, newRecipe, ingredientInRecipeDataList, editDeleteListSaved, idOfRecipe);

                                    /* sends new recipe being added back to the ViewRecipeListActivity and return */
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("new recipe", newRecipe);
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
