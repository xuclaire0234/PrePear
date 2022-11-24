package com.example.prepear;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class ViewDailyMealPlanActivity extends AppCompatActivity{
    // On below part: initialize class attributes
    private DailyMealPlan clickedDailyMealPlan;
    private ArrayList<Meal> dailyMealDataList; // contains all meals inside this current daily meal plan
    private ArrayAdapter<Meal> dailyMealArrayAdapter; // initialize a customized ArrayAdapter for future use
    private final int LAUNCH_ADD_DAILY_MEAL_ACTIVITY = 3;
    private final int LAUNCH_VIEW_INGREDIENT_TYPE_MEAL_ACTIVITY = 1;
    private final int LAUNCH_VIEW_RECIPE_TYPE_MEAL_ACTIVITY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_meal_plan);
        // On below line: get the passed-in DailyMealPlan from MealPlanFragment after user's clicks on one of items inside MealPlanFragment's ListView
        clickedDailyMealPlan = (DailyMealPlan) getIntent().getSerializableExtra("selected daily meal plan");
        // On below part: initialize activity class attributes
        String currentDailyMealPlanDate = clickedDailyMealPlan.getCurrentDailyMealPlanDate(); // storing current daily meal plan's date
        dailyMealDataList = clickedDailyMealPlan.getDailyMealDataList();
        Button addDailyMealButton = findViewById(R.id.add_daily_meal_button); // used to add a new meal in when clicking
        Button backButton = findViewById(R.id.back_button); // back button for the user to go back to the previous activity
        TextView currentDailyMealPlanDateTextView = findViewById(R.id.current_daily_meal_plan_date_text); // TextView object for displaying the current DailyMealPlan's date
        ListView dailyMealListView = findViewById(R.id.daily_meals_listView); // display the meal items
        dailyMealArrayAdapter = new DailyMealPlanCustomList(getApplicationContext(), dailyMealDataList); // initialize customized ArrayAdapter for future use
        dailyMealListView.setAdapter(dailyMealArrayAdapter); //  set the customized ArrayAdapter
        currentDailyMealPlanDateTextView.setText(currentDailyMealPlanDate); // set the TextView's text as current daily meal plan's date String

        // On below part: the user clicks the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // exit the ViewDailyMealPlanActivity and re-direct to the MealPlanFragment
            }
        });
        // On below part: the user attempts to add one new daily meal inside the current daily meal plan by clicking "ADD MEAL" Button
        addDailyMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On below line: start launching the AddDailyMealActivity
                Intent addMealIntent = new Intent(ViewDailyMealPlanActivity.this, AddDailyMealActivity.class);
                addMealIntent.putExtra("current daily meal plan", clickedDailyMealPlan); // pass the current daily meal plan to AddDailyMealActivity for later use
                startActivityForResult(addMealIntent, LAUNCH_ADD_DAILY_MEAL_ACTIVITY);
            }
        });

        // On below part: the user clicks a existing meal item on the ListView for current daily meal plan's all meals
        dailyMealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal clickedMeal = dailyMealArrayAdapter.getItem(position); // grab the meal item which user just clicked
                if (clickedMeal.getMealType().equals("Recipe")) { // if the clicked meal is recipe type
                    Intent switchActivityIntent = new Intent(ViewDailyMealPlanActivity.this, ViewRecipeTypeMealActivity.class);
                    switchActivityIntent.putExtra("viewed meal", clickedMeal);
                    startActivityForResult(switchActivityIntent, LAUNCH_VIEW_RECIPE_TYPE_MEAL_ACTIVITY);
                } else if (clickedMeal.getMealType().equals("IngredientInStorage")) { // if the clicked meal is ingredient type
                    Intent switchActivityIntent = new Intent(ViewDailyMealPlanActivity.this, ViewIngredientTypeMealActivity.class);
                    switchActivityIntent.putExtra("viewed meal", clickedMeal);
                    startActivityForResult(switchActivityIntent, LAUNCH_VIEW_INGREDIENT_TYPE_MEAL_ACTIVITY);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //
        DatabaseController databaseController = new DatabaseController();
        boolean currentMealIsExisting = false;
        if (requestCode == LAUNCH_ADD_DAILY_MEAL_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Meal addedMeal = (Meal) data.getSerializableExtra("added meal");
                if (Objects.equals(addedMeal.getMealType(), "IngredientInStorage")) {
                    for (Meal existingMeal : dailyMealDataList) {
                        if (Objects.equals(existingMeal.getDocumentID(), addedMeal.getDocumentID())) {
                            existingMeal.addExtraCustomizedAmount(addedMeal.getCustomizedAmount());
                            databaseController.addEditMealToDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, existingMeal);
                            currentMealIsExisting = true;
                        }
                    }
                    if ( ! currentMealIsExisting) {
                        dailyMealArrayAdapter.add(addedMeal);
                        databaseController.addEditMealToDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, addedMeal);
                    }
                } else if (Objects.equals(addedMeal.getMealType(), "Recipe")) {
                    dailyMealArrayAdapter.add(addedMeal);
                    databaseController.addEditMealToDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, addedMeal);
                }

            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                String action = data.getStringExtra("action");
                if (action.equals("delete meal")) { // if the user deletes the current meal
                    Meal mealToDelete = (Meal) data.getSerializableExtra("mealToDelete");
                    for (Meal eachMeal: dailyMealDataList) {
                        if (Objects.equals(eachMeal.getDocumentID(), mealToDelete.getDocumentID())){
                            dailyMealArrayAdapter.remove(eachMeal);
                            dailyMealDataList.remove(eachMeal);
                            dailyMealArrayAdapter.notifyDataSetChanged();
                        }
                    }
                    databaseController.deleteMealFromDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, mealToDelete);
                } else if (action.equals("update meal")) { // if the user edits or updates an existing meal
                    Meal mealToUpdate = (Meal) data.getSerializableExtra("mealToUpdate");
                    for (Meal eachMeal: dailyMealDataList) {
                        if (Objects.equals(eachMeal.getDocumentID(), mealToUpdate.getDocumentID())){
                            if (Objects.equals(eachMeal.getMealType(), "IngredientInStorage")) {
                                eachMeal.setCustomizedAmount(mealToUpdate.getCustomizedAmount());
                            } else if (Objects.equals(eachMeal.getMealType(), "Recipe")) {
                                eachMeal.setCustomizedNumberOfServings(mealToUpdate.getCustomizedNumberOfServings());
                            }
                        }
                    }
                    databaseController.addEditMealToDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, mealToUpdate);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // No action
            }
            dailyMealArrayAdapter.notifyDataSetChanged();
        }
    }
}