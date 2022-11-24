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

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // back arrow Toolbar
        TextView addedMealNameTextView = findViewById(R.id.meal_name_text); // TextView for displaying the picked meal's name
        TextInputEditText addedMealAmountEditText = findViewById(R.id.meal_amount_input); // EditText for user's meal amount / number of servings
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button); // meal-adding confirmation Button
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button); // Button for picking a meal from the Ingredient Storage
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button); // Button for picking a meal from the Recipes
        // On below line: receive the passed-in Daily Meal Plan object from ViewMealPlan Activity
        DailyMealPlan currentDailyMealPlan = (DailyMealPlan) getIntent().getSerializableExtra("current daily meal plan");

        pickIngredientTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On below part: launch and direct to the Ingredient Fragment for user to pick a meal inside
                FragmentTransaction ingredientFragmentTransition;
                ingredientFragmentTransition = getSupportFragmentManager().beginTransaction();
                FrameLayout frameLayout = findViewById(android.R.id.content);
                frameLayout.removeAllViews();
                ingredientFragmentTransition.replace(android.R.id.content, new IngredientFragment(), "selectIngredient");
                ingredientFragmentTransition.addToBackStack(null);
                ingredientFragmentTransition.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        });

        pickRecipeTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On below part: launch and direct to the Recipe Fragment for user to pick a meal inside
                FragmentTransaction recipeFragmentTransition;
                recipeFragmentTransition = getSupportFragmentManager().beginTransaction();
                FrameLayout frameLayout = findViewById(android.R.id.content);
                frameLayout.removeAllViews();
                recipeFragmentTransition.replace(android.R.id.content, new RecipeFragment(), "selectRecipe");
                recipeFragmentTransition.addToBackStack(null);
                recipeFragmentTransition.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        });
    }

    /**
     * @param clickedIngredient
     */
    @Override
    public void addIngredientTypeMealInDailyMealPlan(IngredientInStorage clickedIngredient) {
        setContentView(R.layout.activity_add_daily_meal);
        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealAmountEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button);

        /*
         * On below part: restrict the user not to re-direct Recipe Fragment during the process of picking a meal in Ingredient Fragment
         * pop-up Toast message to remind the user with the current meal-picking process in the Ingredient Storage
         * */
        pickRecipeTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please continue and finish adding the meal from your ingredient storage" +
                        " or please click CANCEL ", Toast.LENGTH_LONG).show();
            }
        });

        if (clickedIngredient != null) { // if the user successfully picks a ingredient type meal
            addedMealAmountEditText.setVisibility(View.VISIBLE); // reveal the EditText for user's meal amount input
            addMealButton.setEnabled(true); // enable the meal-adding confirmation Button for user to use after inputting meal amount
            addedMealNameTextView.setText(clickedIngredient.getBriefDescription()); // display the picked meal's name
            addMealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // On below part: grab user input for the picked in-storage ingredient type meal's amount and store in a String
                    String userMealAmountInput = Objects.requireNonNull(addedMealAmountEditText.getText())
                            .toString()
                            .trim();
                    // On below line: cast and convert user-input meal amount into double type value for use
                    double userMealAmountValue = Double.parseDouble(userMealAmountInput);
                    // On below part: check if user input is valid
                    if (userMealAmountInput.isEmpty()) { // if user doesn't input any value on EditText input field for meal amount
                        Toast.makeText(getApplicationContext(), "Please enter a amount value for this meal.", Toast.LENGTH_LONG).show();
                    } else if (userMealAmountValue <= 0) { // if user-input meal amount actual value <= 0
                        Toast.makeText(getApplicationContext(), "Please enter a valid positive value for this meal's amount!!", Toast.LENGTH_LONG).show();
                    } else { // user input success after checking for validation
                        Intent receivedIntent = new Intent(); // initialize an intent for passing the current picked Meal object
                        Meal addedMeal = new Meal("IngredientInStorage", clickedIngredient.getDocumentId(), clickedIngredient.getDocumentId());
                        addedMeal.setCustomizedAmount(userMealAmountValue); // store user-input meal amount double type value in picked Meal object
                        receivedIntent.putExtra("added meal", addedMeal); // pass this picked Meal object in the upcoming activity
                        setResult(Activity.RESULT_OK, receivedIntent);
                        finish(); // after launching the activity, exit afterwards
                    }
                }
            });
        }
    }

    /**
     * @param clickedRecipe
     */
    @Override
    public void addRecipeTypeMealInDailyMealPlan(Recipe clickedRecipe) {
        setContentView(R.layout.activity_add_daily_meal);
        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealNumberOfServingsEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button);

        pickIngredientTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please continue and finish adding the meal from your recipe folder" +
                        " or please click CANCEL ", Toast.LENGTH_LONG).show();
            }
        });

        if (clickedRecipe != null) {
            addedMealNumberOfServingsEditText.setVisibility(View.VISIBLE);
            addMealButton.setEnabled(true);;
            addedMealNameTextView.setText(clickedRecipe.getTitle());
            addMealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userMealNumberOfServingsInput = addedMealNumberOfServingsEditText.getText().toString().trim();
                    double userMealNumberOfServingsInputDoubleValue = Double.parseDouble(userMealNumberOfServingsInput);
                    int userMealNumOfServesValue = (int) Math.round(userMealNumberOfServingsInputDoubleValue);
                    if (userMealNumberOfServingsInput.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a number of servings value for this meal.", Toast.LENGTH_LONG).show();
                    } else if (userMealNumOfServesValue <= 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid positive value for this meal's number of servings!!", Toast.LENGTH_LONG).show();
                    } else {
                        Intent receivedIntent = new Intent();
                        addedMealNameTextView.setText(clickedRecipe.getTitle());
                        Meal addedMeal = new Meal("Recipe", clickedRecipe.getId(), String.valueOf(new Date()));
                        addedMeal.setCustomizedNumberOfServings(userMealNumOfServesValue);
                        receivedIntent.putExtra("added meal", addedMeal);
                        setResult(Activity.RESULT_OK, receivedIntent);
                        finish();
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