package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddDailyMealActivity extends AppCompatActivity implements IngredientFragment.IngredientTypeMealChoiceReceiver {
    private String mealNameStr; //
    private DatabaseController databaseController;
    private DailyMealPlan currentDailyMealPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_meal);

        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealAmountEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button);
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button);
        currentDailyMealPlan = (DailyMealPlan) getIntent().getSerializableExtra("current daily meal plan");
    }

    /**
     * @param clickedIngredient
     */
    public void addIngredientTypeMealInDailyMealPlan(IngredientInStorage clickedIngredient) {
        setContentView(R.layout.activity_add_daily_meal);
        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealAmountEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button);
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button);

        pickRecipeTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please continue and finish adding the meal from your ingredient storage" +
                        " or please click CANCEL ", Toast.LENGTH_LONG).show();
            }
        });

        if (clickedIngredient != null) {
            addedMealAmountEditText.setVisibility(View.VISIBLE);
            addMealButton.setEnabled(true);
            addMealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userMealAmountInput = Objects.requireNonNull(addedMealAmountEditText.getText())
                            .toString().trim();
                    double userMealAmountValue = Double.parseDouble(userMealAmountInput);
                    if (userMealAmountInput.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a amount value for this meal.", Toast.LENGTH_LONG).show();
                    } else if (userMealAmountValue <= 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid positive value for this meal's amount!!", Toast.LENGTH_LONG).show();
                    } else {
                        Intent receivedIntent = new Intent();
                        addedMealNameTextView.setText(clickedIngredient.getBriefDescription());
                        Meal addedMeal = new Meal("IngredientInStorage", clickedIngredient.getDocumentId());
                        databaseController.addEditMealToDailyMealPlan(getApplicationContext(), currentDailyMealPlan, addedMeal);
                    }

                }
            });
        }
    }

    void addRecipeTypeMealInDailyMealPlan(Recipe clickedRecipe) {
        setContentView(R.layout.activity_add_daily_meal);
        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealNumberOfServingsEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button);
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button);

        pickIngredientTypeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please continue and finish adding the meal from your recipe folder" +
                        " or please click CANCEL ", Toast.LENGTH_LONG).show();
            }
        });

        if (clickedRecipe != null) {
            addedMealNumberOfServingsEditText.setVisibility(View.VISIBLE);
            addMealButton.setEnabled(true);
            addMealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userMealNumberOfServingsInput = addedMealNumberOfServingsEditText.getText().toString().trim();
                    Integer userMealNumOfServesValue = Integer.parseInt(userMealNumberOfServingsInput);
                    if (userMealNumberOfServingsInput.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a number of servings value for this meal.", Toast.LENGTH_LONG).show();
                    } else if (userMealNumOfServesValue <= 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid positive value for this meal's number of servings!!", Toast.LENGTH_LONG).show();
                    } else {
                        Intent receivedIntent = new Intent();
                        addedMealNameTextView.setText(clickedRecipe.getTitle());
                        Meal addedMeal = new Meal("IngredientInStorage", clickedRecipe.getId());
                        databaseController.addEditMealToDailyMealPlan(getApplicationContext(), currentDailyMealPlan, addedMeal);
                    }
                }
            });
        }
    }

}