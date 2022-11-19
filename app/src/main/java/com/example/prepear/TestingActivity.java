package com.example.prepear;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class TestingActivity extends AppCompatActivity {
    DailyMealPlan dailyMealPlan;
    int LAUNCH_VIEW_INGREDIENT_TYPE_MEAL_ACTIVITY = 1;
    int LAUNCH_VIEW_RECIPE_TYPE_MEAL_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        dailyMealPlan = new DailyMealPlan();
        dailyMealPlan.setCurrentDailyMealPlanDate("2022-11-14");
        Meal meal = new Meal("Recipe", "20221029_215342");
        meal.setCustomizedNumberOfServings(6);
        ArrayList<Meal> arrayList = new ArrayList<>();
        arrayList.add(meal);
        dailyMealPlan.setDailyMealDataList(arrayList);

        /*
        dailyMealPlan = new DailyMealPlan();
        dailyMealPlan.setCurrentDailyMealPlanDate("2022-11-14");
        Meal meal = new Meal("Ingredient", "2022-11-18 00:19:14");
        meal.setCustomizedAmount(6);
        ArrayList<Meal> arrayList = new ArrayList<>();
        arrayList.add(meal);
        dailyMealPlan.setDailyMealDataList(arrayList);
         */

        Button button = findViewById(R.id.button1);
        button.setOnClickListener((View v) -> {
            if (meal.getMealType().equals("Recipe")) {
                Intent switchActivityIntent = new Intent(TestingActivity.this, ViewRecipeTypeMealActivity.class);
                switchActivityIntent.putExtra("viewed meal", meal);
                startActivityForResult(switchActivityIntent, LAUNCH_VIEW_RECIPE_TYPE_MEAL_ACTIVITY);
            } else {
                Intent switchActivityIntent = new Intent(TestingActivity.this, ViewIngredientTypeMealActivity.class);
                switchActivityIntent.putExtra("viewed meal", meal);
                startActivityForResult(switchActivityIntent, LAUNCH_VIEW_INGREDIENT_TYPE_MEAL_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DatabaseController databaseController = new DatabaseController();

        if (resultCode == Activity.RESULT_OK) {
            String action = data.getStringExtra("action");
            if (action.equals("delete meal")) {
                Meal mealToDelete = (Meal) data.getSerializableExtra("mealToDelete");
                databaseController.deleteMealFromDailyMealPlan(TestingActivity.this, dailyMealPlan, mealToDelete);
            } else if (action.equals("update meal")) {
                Meal mealToUpdate = (Meal) data.getSerializableExtra("mealToUpdate");
                databaseController.addEditMealToDailyMealPlan(TestingActivity.this, dailyMealPlan, mealToUpdate);
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            // No action
        }
    }
}