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

        clickedDailyMealPlan = (DailyMealPlan) getIntent().getSerializableExtra("selected daily meal plan");
        // On below part: initialize activity class attributes
        // storing current daily meal plan's date
        String currentDailyMealPlanDate = clickedDailyMealPlan.getCurrentDailyMealPlanDate();
        dailyMealDataList = clickedDailyMealPlan.getDailyMealDataList();
        Button addDailyMealButton = findViewById(R.id.add_daily_meal_button); // used to add a new meal in when clicking
        Button backButton = findViewById(R.id.back_button);
        TextView currentDailyMealPlanDateTextView = findViewById(R.id.current_daily_meal_plan_date_text);
        ListView dailyMealListView = findViewById(R.id.daily_meals_listView); // display the meal items
        dailyMealArrayAdapter = new DailyMealPlanCustomList(getApplicationContext(), dailyMealDataList);
        dailyMealListView.setAdapter(dailyMealArrayAdapter);
        DatabaseController databaseController = new DatabaseController();

        currentDailyMealPlanDateTextView.setText(currentDailyMealPlanDate);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addDailyMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMealIntent = new Intent(ViewDailyMealPlanActivity.this, AddDailyMealActivity.class);
                addMealIntent.putExtra("current daily meal plan", clickedDailyMealPlan);
                startActivityForResult(addMealIntent, LAUNCH_ADD_DAILY_MEAL_ACTIVITY);
            }
        });

        dailyMealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal clickedMeal = dailyMealArrayAdapter.getItem(position);
                if (clickedMeal.getMealType().equals("Recipe")) {
                    Intent switchActivityIntent = new Intent(ViewDailyMealPlanActivity.this, ViewRecipeTypeMealActivity.class);
                    switchActivityIntent.putExtra("viewed meal", clickedMeal);
                    startActivityForResult(switchActivityIntent, LAUNCH_VIEW_RECIPE_TYPE_MEAL_ACTIVITY);
                } else if (clickedMeal.getMealType().equals("IngredientInStorage")) {
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
                if (action.equals("delete meal")) {
                    Meal mealToDelete = (Meal) data.getSerializableExtra("mealToDelete");
                    for (Meal eachMeal: dailyMealDataList) {
                        if (Objects.equals(eachMeal.getDocumentID(), mealToDelete.getDocumentID())){
                            dailyMealArrayAdapter.remove(eachMeal);
                            dailyMealDataList.remove(eachMeal);
                            dailyMealArrayAdapter.notifyDataSetChanged();
                        }
                    }
                    databaseController.deleteMealFromDailyMealPlan(ViewDailyMealPlanActivity.this, clickedDailyMealPlan, mealToDelete);
                } else if (action.equals("update meal")) { //
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