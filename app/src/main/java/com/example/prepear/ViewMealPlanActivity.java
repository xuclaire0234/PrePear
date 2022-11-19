package com.example.prepear;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewMealPlanActivity extends AppCompatActivity{
    private ListView mealPlanList; // for displaying all added meal plans
    private ArrayAdapter<DailyMealPlan> mealPlanAdapter;
    private ArrayList<DailyMealPlan> mealPlanDataList = new ArrayList<DailyMealPlan>(); // store meal plan entries
    private int LAUNCH_ADD_MEAL_PLAN_ACTIVITY = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal_plan);

        // On below: Grab the ListView object for use
        mealPlanList = findViewById(R.id.meal_plan_listview);
        // On below: initialize the used-defined ArrayAdapter for use
        mealPlanAdapter = new MealPlanCustomList(this, mealPlanDataList);
        // On below: build a connection between the meal plan data list and the ArrayAdapter
        mealPlanList.setAdapter(mealPlanAdapter);

        // On below: grab the ingredient addition button for use
        final FloatingActionButton addMealPlanButton = findViewById(R.id.add_meal_plan_button);
        addMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below call activity for new in-storage ingredient
                Intent intent = new Intent(ViewMealPlanActivity.this, AddMealPlanActivity.class);
                startActivityForResult(intent, LAUNCH_ADD_MEAL_PLAN_ACTIVITY);
            }
        });

        mealPlanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Grab the clicked item out of the ListView
                Object clickedItem = mealPlanList.getItemAtPosition(position);
                // Casting this clicked item to IngredientInStorage type from Object type
                DailyMealPlan clickedFood= (DailyMealPlan) clickedItem;
                // call activity to edit ingredient
                Intent intent = new Intent(ViewMealPlanActivity.this, ViewDailyMealPlanActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    /**
     * This method updates the ingredient list view after the AddEditIngredientActivity concludes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_MEAL_PLAN_ACTIVITY) {

        }

    }

}