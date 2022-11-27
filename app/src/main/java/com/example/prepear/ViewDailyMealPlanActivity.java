/**
 * Classname: ViewDailyMealPlanActivity
 * Version Information: 2.0.0
 * Date: 11/17/2022
 * Author: Shihao Liu, Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * This class defines the view daily meal plan activity that displays all the meals being planned to be
 * eaten on a specific date.
 */
public class ViewDailyMealPlanActivity extends AppCompatActivity{
    // initializes class attributes
    private DailyMealPlan clickedDailyMealPlan;
    private ArrayList<Meal> dailyMealDataList; // contains all meals inside this current daily meal plan
    private ArrayAdapter<Meal> dailyMealArrayAdapter; // initialize a customized ArrayAdapter for future use
    private final int LAUNCH_VIEW_INGREDIENT_TYPE_MEAL_ACTIVITY = 1;
    private final int LAUNCH_VIEW_RECIPE_TYPE_MEAL_ACTIVITY = 2;
    private final int LAUNCH_ADD_DAILY_MEAL_ACTIVITY = 3;

    /**
     * This method creates the new ViewDailyMealPlanActivity.
     * @param savedInstanceState can be used to recreate the activity and load all data from it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_meal_plan);
        // gets the passed-in DailyMealPlan from MealPlanFragment after user's clicks on one of items inside MealPlanFragment's ListView
        clickedDailyMealPlan = (DailyMealPlan) getIntent().getSerializableExtra("selected daily meal plan");
        // initializes activity class attributes
        String currentDailyMealPlanDate = clickedDailyMealPlan.getCurrentDailyMealPlanDate(); // storing current daily meal plan's date
        dailyMealDataList = clickedDailyMealPlan.getDailyMealDataList();
        Button addDailyMealButton = findViewById(R.id.add_daily_meal_button); // used to add a new meal in when clicking
        Button backButton = findViewById(R.id.back_button); // back button for the user to go back to the previous activity
        TextView currentDailyMealPlanDateTextView = findViewById(R.id.current_daily_meal_plan_date_text); // TextView object for displaying the current DailyMealPlan's date
        ListView dailyMealListView = findViewById(R.id.daily_meals_listView); // display the meal items
        dailyMealArrayAdapter = new DailyMealPlanCustomList(getApplicationContext(), dailyMealDataList, clickedDailyMealPlan); // initialize customized ArrayAdapter for future use
        dailyMealListView.setAdapter(dailyMealArrayAdapter); //  set the customized ArrayAdapter
        currentDailyMealPlanDateTextView.setText(currentDailyMealPlanDate); // set the TextView's text as current daily meal plan's date String

        // sets up the back button to return to the calling MealPlanFragment once being clicked
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // exit the ViewDailyMealPlanActivity and re-direct to the MealPlanFragment
            }
        });
        // sets up the add daily meal button to direct to AddDailyMealActivity once being clicked
        addDailyMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starts launching the AddDailyMealActivity
                Intent addMealIntent = new Intent(ViewDailyMealPlanActivity.this, AddDailyMealActivity.class);
                addMealIntent.putExtra("current daily meal plan", clickedDailyMealPlan); // pass the current daily meal plan to AddDailyMealActivity for later use
                startActivityForResult(addMealIntent, LAUNCH_ADD_DAILY_MEAL_ACTIVITY);
            }
        });

        /* sets up the daily meal list view to direct to either ViewRecipeTypeMealActivity
        or ViewIngredientTypeMealActivity depending on the meal type for displaying detailed
        information once being clicked */
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

    /**
     * This method deals with the information sent from the child activities.
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data the data being passed
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DatabaseController databaseController = new DatabaseController();
        boolean currentMealIsExisting = false;

        // checks the child activity that sent the request
        if (requestCode == LAUNCH_ADD_DAILY_MEAL_ACTIVITY) { // if the child activity was AddDailyMealActivity
            if (resultCode == Activity.RESULT_OK) { // if the child activity sent a result
                // adds the sent meal to the ArrayAdapter as well as the database
                Meal addedMeal = (Meal) data.getSerializableExtra("added meal");
                if (Objects.equals(addedMeal.getMealType(), "IngredientInStorage")) { // if the meal type is ingredient
                    // checks if the ingredient is already in the daily meal list, and adds the amount to it is true
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
                } else if (Objects.equals(addedMeal.getMealType(), "Recipe")) { // if the meal type is recipe
                    dailyMealArrayAdapter.add(addedMeal);
                    databaseController.addEditMealToDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, addedMeal);
                }

            }
        } else { // if the child activity was ViewRecipeTypeMealActivity or ViewIngredientTypeMealActivity
            if (resultCode == Activity.RESULT_OK) { // if the child activity sent a result
                String action = data.getStringExtra("action");
                if (action.equals("delete meal")) { // if the user wants to delete the current meal
                    // deletes the meal from both the ArrayAdapter and database
                    Meal mealToDelete = (Meal) data.getSerializableExtra("mealToDelete");
                    Log.d(TAG, mealToDelete.getMealID());
                    for (Meal eachMeal: dailyMealDataList) {
                        if (Objects.equals(eachMeal.getMealID(), mealToDelete.getMealID())){
                            dailyMealArrayAdapter.remove(eachMeal);
                            //dailyMealDataList.remove(eachMeal);
                            dailyMealArrayAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    databaseController.deleteMealFromDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, mealToDelete);
                } else if (action.equals("update meal")) { // if the user want to update an existing meal
                    // updates the information of the meal inside both the ArrayAdapter and database
                    Meal mealToUpdate = (Meal) data.getSerializableExtra("mealToUpdate");
                    for (Meal eachMeal: dailyMealDataList) {
                        if (Objects.equals(eachMeal.getMealID(), mealToUpdate.getMealID())){
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
            if (resultCode == Activity.RESULT_CANCELED) { // if the child activity cancelled sending the result
                // No action
            }
            dailyMealArrayAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This method resorts the daily meal plan list view by time picked by user after returning to the activity.
     */
    @Override
    public void onRestart() {
        super.onRestart();

        // resorts the daily meal plan list view by time picked by user through comparing hours and minutes
        Collections.sort(dailyMealDataList, new Comparator<Meal>() {
            @Override
            public int compare(Meal meal1, Meal meal2) {
                String time1 = String.format(Locale.getDefault(), "%02d:%02d", meal1.getEatHour(), meal1.getEatMinute());
                String time2 = String.format(Locale.getDefault(), "%02d:%02d", meal2.getEatHour(), meal2.getEatMinute());
                return time1.compareTo(time2);
                /*
                if (meal1.getEatHour().equals(meal2.getEatHour())) {
                    return meal1.getEatMinute().compareTo(meal2.getEatMinute());
                } else {
                    return meal1.getEatHour().compareTo(meal2.getEatHour());
                }
                 */
            }
        });
        dailyMealArrayAdapter.notifyDataSetChanged();
    }
}