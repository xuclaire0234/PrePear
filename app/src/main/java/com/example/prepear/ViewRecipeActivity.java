/**
 * Classname: ViewRecipeActivity
 * Version Information: 1.0.0
 * Date: 11/3/2022
 * Author: Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This class defines the view recipe activity that allows user to view details of a specific recipe.
 */
@SuppressWarnings("FieldCanBeLocal")
public class ViewRecipeActivity extends AppCompatActivity {
    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView preparationTimeTextView;
    private TextView numberOfServingsTextView;
    private TextView recipeCategoryTextView;
    private TextView commentsTextView;
    private ListView ingredientInRecipeListView;
    private ArrayAdapter<IngredientInRecipe> ingredientInRecipeArrayAdapter;
    private ArrayList<IngredientInRecipe> ingredientInRecipeDataList;
    private Button editButton;
    private Button deleteButton;
    private Button returnButton;
    private Recipe viewedRecipe;

    /**
     * This creates the ViewRecipeActivity.
     * @param savedInstanceState
     *      can be used to recreate the activity and load all data from it
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        // connects the layout with the views and buttons
        imageImageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.title_TextView);
        preparationTimeTextView = findViewById(R.id.preparation_time_TextView);
        numberOfServingsTextView = findViewById(R.id.number_of_servings_TextView);
        recipeCategoryTextView = findViewById(R.id.recipe_category_TextView);
        commentsTextView = findViewById(R.id.comments_TextView);
        ingredientInRecipeListView = findViewById(R.id.ingredient_in_recipe_ListView);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        returnButton = findViewById(R.id.return_button);

        /* gets the information of the specific recipe being clicked inside ViewRecipeListActivity
        and display them on the screen */
        viewedRecipe = (Recipe) getIntent().getSerializableExtra("viewed recipe");
        Glide.with(ViewRecipeActivity.this)
                .load(viewedRecipe.getImageURI()).into(imageImageView);
        titleTextView.setText(viewedRecipe.getTitle());
        preparationTimeTextView.setText(String.format(Locale.getDefault(), "%d", viewedRecipe.getPreparationTime()));
        numberOfServingsTextView.setText(String.format(Locale.getDefault(), "%d", viewedRecipe.getNumberOfServings()));
        recipeCategoryTextView.setText(viewedRecipe.getRecipeCategory());
        commentsTextView.setText(viewedRecipe.getComments());
        ingredientInRecipeDataList = viewedRecipe.getListOfIngredients();
        ingredientInRecipeArrayAdapter = new CustomIngredientInRecipeList(this, ingredientInRecipeDataList);
        ingredientInRecipeListView.setAdapter(ingredientInRecipeArrayAdapter);

        /* sets edit button to jump to AddEditRecipeActivity after clicking it, while at the same time
        pass the recipe object being viewed to the AddEditRecipeActivity to let it display its information */
        editButton.setOnClickListener((View v) -> {
                Intent switchActivityIntent = new Intent(ViewRecipeActivity.this, AddEditRecipeActivity.class);
                switchActivityIntent.putExtra("viewed recipe", viewedRecipe);
                switchActivityIntent.putExtra("calling activity", "2"); // pass the number 2 to AddEditRecipeActivity to indicate that it should be used to edit recipe
                startActivity(switchActivityIntent);

                /* return to the calling ViewRecipeListActivity automatically after editing the recipe */
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
        });

        /* sets delete button to delete the viewing recipe after clicking it, and then return
        to the ViewRecipeListActivity */
        deleteButton.setOnClickListener((View v) -> {
                /* delete recipe from database */
                DatabaseController databaseController = new DatabaseController();
                databaseController.deleteRecipeFromRecipeList(ViewRecipeActivity.this, viewedRecipe);

                /* return to the calling ViewRecipeListActivity */
                Intent returnIntent = new Intent();
                returnIntent.putExtra("delete recipe", viewedRecipe);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
        });

        /* sets return button to directly return to the ViewRecipeListActivity after clicking it */
        returnButton.setOnClickListener((View v) -> {
                /* return to the calling ViewRecipeListActivity */
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
        });
    }
}
