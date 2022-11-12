/**
 * Classname: AddEditRecipeActivity
 * Version Information: 1.0.0
 * Date: 11/2/2022
 * Author: Jiayin He, Yingyue Cao
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * This class defines the add/edit recipe activity that allows user to either add a new recipe or
 * edit a existing recipe.
 */
public class AddEditRecipeActivity extends AppCompatActivity implements RecipeAddEditIngredientFragment.OnFragmentInteractionListener {
    private ArrayAdapter<CharSequence> recipeCategorySpinnerAdapter;
    private ImageView imageImageView;
    private FloatingActionButton editImageButton;
    private EditText titleEditText;
    private TextView showTitleWordCount;
    private EditText preparationTimeEditText;
    private TextView showPreparationTimeWordCount;
    private EditText numberOfServingsEditText;
    private TextView showNumberOfServingsWordCount;
    private Spinner recipeCategorySpinner;
    private EditText recipeCategoryEditText;
    private TextView showRecipeCategoryWordCount;
    private LinearLayout newRecipeCategoryLinearLayout;
    private EditText commentsEditText;
    private TextView showCommentWordCount;
    private Button addIngredientInRecipeButton;
    private ListView ingredientInRecipeListView;
    private Button commitButton;
    private Button cancelButton;
    private ArrayAdapter<IngredientInRecipe> ingredientInRecipeArrayAdapter;
    private ArrayList<IngredientInRecipe> ingredientInRecipeDataList;
    private Recipe viewedRecipe;
    private String linkOfImage;
    private StorageReference storageReference;
    private Uri imageLocationPath;
    private boolean pictureSelected;
    private Integer positionToEditInViewIngredient = -1;
    private ArrayList<String> editDeleteListSaved;
    private String idOfRecipe;

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
        showTitleWordCount = findViewById(R.id.title_word_count);
        preparationTimeEditText = findViewById(R.id.preparation_time_EditText);
        showPreparationTimeWordCount = findViewById(R.id.preparation_time_word_count);
        numberOfServingsEditText = findViewById(R.id.number_of_servings_EditText);
        showNumberOfServingsWordCount = findViewById(R.id.number_of_servings_word_count);
        recipeCategorySpinner = findViewById(R.id.recipe_category_Spinner);
        recipeCategoryEditText = findViewById(R.id.recipe_category_EditText);
        showRecipeCategoryWordCount = findViewById(R.id.recipe_category_word_count);
        newRecipeCategoryLinearLayout = findViewById(R.id.new_recipe_category_LinearLayout);
        commentsEditText = findViewById(R.id.comments_EditText);
        showCommentWordCount = findViewById(R.id.comments_word_count);
        addIngredientInRecipeButton = findViewById(R.id.add_ingredient_in_recipe_button);
        ingredientInRecipeListView = findViewById(R.id.ingredient_in_recipe_ListView);
        commitButton = findViewById(R.id.commit_button);
        cancelButton = findViewById(R.id.cancel_button);

        /* connects to firebase storage */
        storageReference = FirebaseStorage.getInstance().getReference("imageFolder");

        /* sets up filter to control the length of the title text */
        final TextWatcher titleTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showTitleWordCount.setText(String.valueOf(30 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        titleEditText.addTextChangedListener(titleTextEditorWatcher);

        /* sets up filter to control the length of the preparation time text */
        final TextWatcher preparationTimeTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showPreparationTimeWordCount.setText(String.valueOf(10 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        preparationTimeEditText.addTextChangedListener(preparationTimeTextEditorWatcher);

        /* sets up filter to control the length of the number of servings text */
        final TextWatcher numberOfServingsTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showNumberOfServingsWordCount.setText(String.valueOf(3 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        numberOfServingsEditText.addTextChangedListener(numberOfServingsTextEditorWatcher);

        /* sets up filter to control the length of the recipe category text */
        final TextWatcher recipeCategoryTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showRecipeCategoryWordCount.setText(String.valueOf(20 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        recipeCategoryEditText.addTextChangedListener(recipeCategoryTextEditorWatcher);


        /* sets up filter to control the length of the comment text */
        final TextWatcher commentsTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showCommentWordCount.setText(String.valueOf(60 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        commentsEditText.addTextChangedListener(commentsTextEditorWatcher);

        /* sets up recipe category spinner */
        recipeCategorySpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.recipe_category,
                android.R.layout.simple_spinner_item);
        recipeCategorySpinner.setAdapter(recipeCategorySpinnerAdapter);
        recipeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecipeCategory = recipeCategorySpinner.getSelectedItem().toString();
                if (selectedRecipeCategory.equals("Other")) {
                    newRecipeCategoryLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    newRecipeCategoryLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* sets up image picker*/
        editImageButton.setOnClickListener((View v) -> {
            ImagePicker.Companion.with(this)
                    .crop() // crop image (optional)
                    .compress(1024) // final image size will be less than 1 MB (optional)
                    .maxResultSize(1000, 1000) // final image resolution will be less than 1080 x 1080 (optional)
                    .start();
        });

        if (getIntent().getStringExtra("calling activity").equals("2")) {
            /* If the calling activity is ViewRecipeActivity, display the information of the viewing recipe. */
            viewedRecipe = (Recipe) getIntent().getSerializableExtra("viewed recipe");
            idOfRecipe = viewedRecipe.getId();
            linkOfImage = viewedRecipe.getImageURI();
            Glide.with(AddEditRecipeActivity.this)
                    .load(linkOfImage).into(imageImageView);
            titleEditText.setText(viewedRecipe.getTitle());
            preparationTimeEditText.setText(viewedRecipe.getPreparationTime().toString());
            numberOfServingsEditText.setText(viewedRecipe.getNumberOfServings().toString());
            String recipeCategory = viewedRecipe.getRecipeCategory();
            List<String> recipeCategories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.recipe_category)));
            if (recipeCategories.contains(recipeCategory)) {
                recipeCategorySpinner.setSelection(recipeCategorySpinnerAdapter.getPosition(recipeCategory));
            } else {
                recipeCategorySpinner.setSelection(recipeCategorySpinnerAdapter.getPosition("Other"));
                newRecipeCategoryLinearLayout.setVisibility(View.VISIBLE);
                recipeCategoryEditText.setText(recipeCategory);
            }
            commentsEditText.setText(viewedRecipe.getComments());
            ingredientInRecipeDataList = viewedRecipe.getListOfIngredients();
        } else {
            /* If the calling activity is ViewRecipeListActivity, prompt user to add a new recipe */
            ingredientInRecipeDataList = new ArrayList<>();
            idOfRecipe = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        }
        ingredientInRecipeArrayAdapter = new CustomIngredientInRecipeList(this, ingredientInRecipeDataList);
        ingredientInRecipeListView.setAdapter(ingredientInRecipeArrayAdapter);
        editDeleteListSaved = new ArrayList<String>();

        /* sets add ingredient button to direct to RecipeAddIngredientFragment */
        addIngredientInRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionToEditInViewIngredient = -1;
                new RecipeAddEditIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT_IN_RECIPE");
            }
        });

        /* sets each ingredient object on listview to direct to RecipeEditIngredientFragment */
        ingredientInRecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionToEditInViewIngredient = position;
                RecipeAddEditIngredientFragment.newInstance(ingredientInRecipeArrayAdapter.getItem(position)).show(getSupportFragmentManager(),
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
     * This gets the picture being selected by the user and display it on ImageView.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                /* gets the image selected by user */
                imageLocationPath = data.getData();
                Bitmap objectBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageLocationPath);

                /* displays image */
                imageImageView.setImageBitmap(objectBitmap);
                pictureSelected = true;
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This gets the extension of image being selected.
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
            /* if no new picture being selected */

            /* gets the information of recipe being edited/added */
            final String title = titleEditText.getText().toString();
            final String preparationTime = preparationTimeEditText.getText().toString();
            final String numberOfServings = numberOfServingsEditText.getText().toString();
            String recipeCategory = recipeCategorySpinner.getSelectedItem().toString();
            if (recipeCategory.equals("Other")) {
                recipeCategory = recipeCategoryEditText.getText().toString();
            }
            final String comments = commentsEditText.getText().toString();
            final String imageURI = linkOfImage;

            /* checks if there is any necessary information missing */
            if (title.equals("") || preparationTime.equals("") || numberOfServings.equals("")
                    || recipeCategory.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "You did not enter the full information, commit failed. " +
                                "Please make sure that all necessary fields " +
                                "(title, preparation time, number of servings, recipe category) " +
                                "has been filled.",
                        Toast.LENGTH_LONG).show();
            } else {
                /* add to database */
                Recipe newRecipe = new Recipe(imageURI, title, Integer.parseInt(preparationTime),
                        (Integer.parseInt(numberOfServings)), recipeCategory, comments);
                DatabaseController databaseController = new DatabaseController();
                databaseController.addEditRecipeToRecipeList(AddEditRecipeActivity.this, newRecipe, ingredientInRecipeDataList, editDeleteListSaved, idOfRecipe);

                /* sends new recipe being added back to the ViewRecipeListActivity and return */
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
                                /* gets the information of recipe being edited/added */
                                final String title = titleEditText.getText().toString();
                                final String preparationTime = preparationTimeEditText.getText().toString();
                                final String numberOfServings = numberOfServingsEditText.getText().toString();
                                String recipeCategory = recipeCategorySpinner.getSelectedItem().toString();
                                if (recipeCategory.equals("Other")) {
                                    recipeCategory = recipeCategoryEditText.getText().toString();
                                }
                                final String comments = commentsEditText.getText().toString();
                                final String imageURI = task.getResult().toString();

                                /* checks if there is any necessary information missing */
                                if (title.equals("") || preparationTime.equals("") || numberOfServings.equals("")
                                        || recipeCategory.equals("")) {
                                    Toast.makeText(getApplicationContext(),
                                            "You did not enter the full information, commit failed. " +
                                                    "Please make sure that all necessary fields " +
                                                    "(title, preparation time, number of servings, recipe category) " +
                                                    "has been filled.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    /* add to database */
                                    Recipe newRecipe = new Recipe(imageURI, title, Integer.parseInt(preparationTime),
                                            (Integer.parseInt(numberOfServings)), recipeCategory, comments);
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