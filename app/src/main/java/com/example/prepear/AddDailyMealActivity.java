/**
 * Class Name: AddDailyMealPlanActivity
 * Version Information: Version 1.0.0
 * Date: Nov 22nd, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 */

package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.Recipe.RecipeFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * This Activity class is used to launch the daily-meal-adding activity for the user to choose a daily meal either from Ingredient Storage or from Recipes
 */
public class AddDailyMealActivity extends AppCompatActivity
        implements IngredientFragment.IngredientTypeMealChoiceReceiver,
        RecipeFragment.RecipeTypeMealChoiceReceiver, View.OnClickListener {
    // On below: initialize class attributes
    // On below line: String which represents the meal's name (description if the meal is an in-storage ingredient / title if a recipe)
    private String mealNameStr;
    private final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_meal);

        TextView addedMealNameTextView = findViewById(R.id.meal_name_text); // TextView for displaying the picked meal's name
        TextInputEditText addedMealAmountEditText = findViewById(R.id.meal_amount_input); // EditText for user's meal amount / number of servings
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button); // meal-adding confirmation Button
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button); // Button for picking a meal from the Ingredient Storage
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button); // Button for picking a meal from the Recipes
        Button backButton = findViewById(R.id.back_button); // back button for exiting current activity and re-direct to the previous activity
        // On below line: receive the passed-in Daily Meal Plan object from ViewMealPlan Activity
        DailyMealPlan currentDailyMealPlan = (DailyMealPlan) getIntent().getSerializableExtra("current daily meal plan");

        // On below part: the user clicks the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // finish the current activity and re-direct to the previous activity (ViewDailyMealPlan Activity)
            }
        });

        // On below part: the user attempts to add a new ingredient type meal from Ingredient Storage by clicking pickIngredientTypeMealButton
        pickIngredientTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On below part: launch and direct to the Ingredient Fragment for user to pick an ingredient type meal inside
                FragmentTransaction ingredientFragmentTransition; // begin Fragment Transaction
                ingredientFragmentTransition = getSupportFragmentManager().beginTransaction();
                FrameLayout frameLayout = findViewById(android.R.id.content);
                frameLayout.removeAllViews(); // remove all Views from display
                // On below line: replace the current View with the fragment
                ingredientFragmentTransition.replace(android.R.id.content, new IngredientFragment(), "selectIngredient");
                ingredientFragmentTransition.addToBackStack(null);
                ingredientFragmentTransition.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        });
        // On below part: the user attempts to add a new recipe type meal from Recipes by clicking pickRecipeTypeMealButton
        pickRecipeTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On below part: launch and direct to the Recipe Fragment for user to pick a meal inside
                FragmentTransaction recipeFragmentTransition; // begin fragment transaction
                recipeFragmentTransition = getSupportFragmentManager().beginTransaction();
                FrameLayout frameLayout = findViewById(android.R.id.content);
                frameLayout.removeAllViews(); // remove all views from display
                // On below line: replace the current view with the fragment
                recipeFragmentTransition.replace(android.R.id.content, new RecipeFragment(), "selectRecipe");
                recipeFragmentTransition.addToBackStack(null);
                recipeFragmentTransition.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        });
    }

    // On below part: override the method inside IngredientTypeMealChoiceReceiver interface for IngredientFragment for use
    @Override
    public void addIngredientTypeMealInDailyMealPlan(IngredientInStorage clickedIngredient) {
        // On below part: find the corresponding objects for the user to use during the process of picking a new in-storage ingredient type meal
        setContentView(R.layout.activity_add_daily_meal);
        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealAmountEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button);

        /*
        * On below part: restrict the user not to re-direct RecipeFragment during the process of picking a meal in IngredientFragment
        * pop-up Toast message to remind the user with the current meal-picking process in the Ingredient Storage
        * */
        pickRecipeTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please continue and finish adding the meal from your ingredient storage" +
                        " or please click Back ", Toast.LENGTH_LONG).show();
            }
        });

        if (clickedIngredient != null) { // if the user successfully picks a ingredient type meal
            addedMealAmountEditText.setVisibility(View.VISIBLE); // reveal the EditText for user's meal amount input
            addMealButton.setEnabled(true); // enable the meal-adding confirmation Button for user to use after inputting meal amount
            addedMealNameTextView.setText(clickedIngredient.getBriefDescription()); // display the picked meal's name (Ingredient Type Meal's description)
            // On below part: when the user attempts to add this picked meal in the current daily meal plan by clicking the "Add Meal" Button
            addMealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // On below part: grab user input for the picked in-storage ingredient type meal's amount and store in a String
                    String userMealAmountInput = Objects.requireNonNull(addedMealAmountEditText.getText())
                            .toString()
                            .trim();
                    // On below part: check if user input is valid
                    // On below line: cast and convert user-input meal amount into double type value for use
                    double userMealAmountValue = Double.parseDouble(userMealAmountInput);
                    if (userMealAmountInput.isEmpty()) { // if user doesn't input any value on EditText input field for meal amount
                        Toast.makeText(getApplicationContext(), "Please enter a amount value for this meal.", Toast.LENGTH_LONG).show();
                    } else if (userMealAmountValue <= 0) { // if user-input meal amount actual value <= 0
                        Toast.makeText(getApplicationContext(), "Please enter a valid positive value for this meal's amount!!", Toast.LENGTH_LONG).show();
                    } else { // user input success after checking for validation
                        Intent receivedIntent = new Intent(); // initialize an intent for passing the current picked Meal object
                        Meal addedMeal = new Meal("IngredientInStorage", clickedIngredient.getDocumentId(), clickedIngredient.getDocumentId());
                        addedMeal.setCustomizedAmount(userMealAmountValue); // store user-input meal amount double type value in picked Meal object
                        receivedIntent.putExtra("added meal", addedMeal); // store this picked Meal object and pack it for passing back
                        setResult(Activity.RESULT_OK, receivedIntent); // pass the picked Meal object back to previous activity for use
                        finish(); // exit after launching the activity
                    }
                }
            });
        }
    }
    // On below part: override the method inside RecipeTypeMealChoiceReceiver interface for RecipeFragment for use
    @Override
    public void addRecipeTypeMealInDailyMealPlan(Recipe clickedRecipe) {
        // On below part: find the corresponding objects for the user to use during the process of picking a new recipe type meal
        setContentView(R.layout.activity_add_daily_meal);
        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealNumberOfServingsEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button);
        /*
         * On below part: restrict the user not to re-direct IngredientFragment during the process of picking a meal in RecipeFragment
         * pop-up Toast message to remind the user with the current meal-picking process in the user's Recipes
         * */
        pickIngredientTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please continue and finish adding the meal from your recipe folder" +
                        " or please click BACK ", Toast.LENGTH_LONG).show();
            }
        });

        if (clickedRecipe != null) { // if the user successfully picked a Recipe Type Meal
            addedMealNumberOfServingsEditText.setVisibility(View.VISIBLE); // reveal the EditText for user's meal number of servings input
            addMealButton.setEnabled(true); // enable the meal-adding confirmation Button for user to use after inputting meal number of servings
            addedMealNameTextView.setText(clickedRecipe.getTitle()); // display the picked Meal object's name (Recipe Type Meal's title)
            // On below part: when the user attempts to add this picked meal in the current daily meal plan by clicking the "Add Meal" Button
            addMealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // On below line: grab user input for current picked recipe type meal's number of servings
                    String userMealNumberOfServingsInput = addedMealNumberOfServingsEditText.getText().toString().trim();
                    /* On below part: based on one child or youth is regarded as 0.5 person for a meal's number of servings
                    * After user's double type input for picked recipe type meal's number of servings,
                    * always round up to the closest integer
                    * */
                    double userMealNumberOfServingsInputDoubleValue = Double.parseDouble(userMealNumberOfServingsInput);
                    int userMealNumOfServesValue = (int) Math.round(userMealNumberOfServingsInputDoubleValue);
                    // On below part: check for user input's validation
                    if (userMealNumberOfServingsInput.isEmpty()) { // if user doesn't input any value on EditText input field for meal number of servings
                        Toast.makeText(getApplicationContext(), "Please enter a valid number of servings value for this meal.", Toast.LENGTH_LONG).show();
                    } else if (userMealNumOfServesValue <= 0) { // if user entered non-positive value for meal number of servings
                        Toast.makeText(getApplicationContext(), "Please enter a valid positive value for this meal's number of servings!!", Toast.LENGTH_LONG).show();
                    } else { // if user's input is valid
                        Intent receivedIntent = new Intent(); // initialize an intent for passing the current picked Meal object
//                        addedMealNameTextView.setText(clickedRecipe.getTitle());
                        // On below line: create a new Meal object representing user's current picked recipe type meal
                        Meal addedMeal = new Meal("Recipe", clickedRecipe.getId(), String.valueOf(new Date()));
                        addedMeal.setCustomizedNumberOfServings(userMealNumOfServesValue); // stores this meal's number of servings
                        receivedIntent.putExtra("added meal", addedMeal); // store this picked Meal object and pack it for passing back
                        setResult(Activity.RESULT_OK, receivedIntent); // pass the picked Meal object back to previous activity for use
                        finish(); // exit current activity after passing this Meal object back to previous activity for use
                    }
                }
            });
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}