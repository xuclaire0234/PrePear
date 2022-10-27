package com.example.prepear;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddEditRecipeActivity extends AppCompatActivity {
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
    private Uri currentImageURI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_add_edit_recipe);

        imageImageView = findViewById(R.id.imageView);
        editImageButton = findViewById(R.id.edit_image_button);
        titleEditText = findViewById(R.id.title_EditText);
        preparationTimeEditText = findViewById(R.id.preparation_time_EditText);
        numberOfServingsEditText = findViewById(R.id.number_of_servings_EditText);
        recipeCategoryEditText = findViewById(R.id.recipe_category_EditText);
        commentsEditText = findViewById(R.id.comments_EditText);
        addIngredientInRecipeButton = findViewById(R.id.add_ingredient_in_recipe_button);
        ingredientInRecipeListView = findViewById(R.id.ingredient_in_recipe_ListView);
        commitButton = findViewById(R.id.commit_button);
        cancelButton = findViewById(R.id.cancel_button);

        if (getCallingActivity().getClassName().equals("ViewRecipeActivity")) {
            viewedRecipe = (Recipe) getIntent().getSerializableExtra("viewed recipe");
            currentImageURI = viewedRecipe.getImageURI();
            imageImageView.setImageURI(currentImageURI);
            titleEditText.setText(viewedRecipe.getTitle());
            preparationTimeEditText.setText(viewedRecipe.getPreparationTime().toString());
            numberOfServingsEditText.setText(viewedRecipe.getNumberOfServings().toString());
            recipeCategoryEditText.setText(viewedRecipe.getRecipeCategory());
            commentsEditText.setText(viewedRecipe.getComments());
            ingredientInRecipeDataList = viewedRecipe.getListOfIngredients();
        } else {
            ingredientInRecipeDataList = new ArrayList<>();
            ingredientInRecipeDataList.add(new IngredientInRecipe("banana", 2,
                    "piece", "Need to be fresh."));
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

        /*
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddEditRecipeActivity.this)
                        .galleryOnly()
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
         */

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Uri imageURI = currentImageURI;
                final String title = titleEditText.getText().toString();
                final Integer preparationTime = Integer.parseInt(preparationTimeEditText.getText().toString());
                final Integer numberOfServings = Integer.parseInt(numberOfServingsEditText.getText().toString());
                final String recipeCategory = recipeCategoryEditText.getText().toString();
                final String comments = commentsEditText.getText().toString();
                final ArrayList<IngredientInRecipe> listOfIngredients = ingredientInRecipeDataList;

                if (title.equals("") || preparationTime.equals("") || numberOfServings.equals("")
                        || recipeCategory.equals("") || comments.equals("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter the full information, add/edit failed.", Toast.LENGTH_LONG).show();
                } else {
                    if (getCallingActivity().getClassName().equals("ViewRecipeListActivity")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("new recipe", new Recipe(imageURI, title, preparationTime,
                                numberOfServings, recipeCategory, comments));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        viewedRecipe.setImageURI(imageURI);
                        viewedRecipe.setTitle(title);
                        viewedRecipe.setPreparationTime(preparationTime);
                        viewedRecipe.setNumberOfServings(numberOfServings);
                        viewedRecipe.setRecipeCategory(recipeCategory);
                        viewedRecipe.setComments(comments);
                        viewedRecipe.setListOfIngredients(listOfIngredients);
                        finish();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCallingActivity().getClassName().equals("ViewRecipeListActivity")) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        currentImageURI = data.getData();
        imageImageView.setImageURI(currentImageURI);
    }

    public void onConfirmPressed(IngredientInRecipe ingredientToAdd) {
        ingredientInRecipeArrayAdapter.add(ingredientToAdd);
    }

    public void onOkPressed() {};

    public void onDeletePressed(IngredientInRecipe ingredientToDelete) {
        ingredientInRecipeArrayAdapter.remove(ingredientToDelete);
    }
}
