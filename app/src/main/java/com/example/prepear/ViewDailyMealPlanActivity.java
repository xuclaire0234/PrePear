package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewDailyMealPlanActivity extends AppCompatActivity{
    // On below part: initialize activity class attributes
    private String currentDailyMealPlanDate; // storing current daily meal plan's date
    private ArrayList<Meal> dailyMealDataList; // contains all meals inside this current daily meal plan
    private DailyMealPlan dailyMealPlan; //
    private ArrayAdapter<Meal> dailyMealArrayAdapter; // initialize a customized ArrayAdapter for future use
    private DatabaseController databaseController;
    private final String DAILY_MEAL_PLAN_COLLECTION_NAME = "Daily Meal Plans";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_meal_plan);


        Button addDailyMealButton = findViewById(R.id.add_daily_meal_button); // used to add a new meal in when clicking
        ListView dailyMealListView = findViewById(R.id.daily_meals_listView); // display the meal items
        dailyMealArrayAdapter = new DailyMealPlanCustomList(getApplicationContext(), dailyMealPlan.getDailyMealDataList());
        dailyMealListView.setAdapter(dailyMealArrayAdapter);
        FirebaseFirestore appDatabase = FirebaseFirestore.getInstance(); // instantiate


        addDailyMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMealIntent = new Intent(ViewDailyMealPlanActivity.this, AddDailyMealActivity.class);
                startActivity(addMealIntent);
            }
        });

        dailyMealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal clickedMeal = (Meal) dailyMealArrayAdapter.getItem(position);
            }
        });
    }

}