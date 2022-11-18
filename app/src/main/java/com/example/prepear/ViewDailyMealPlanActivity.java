package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewDailyMealPlanActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // On below part: initialize activity class attributes
    private String currentDailyMealPlanDate; // storing current daily meal plan's date
    private ArrayList<Meal> dailyMealDataList; // contains all meals inside this current daily meal plan
    private ArrayAdapter<Meal> dailyMealArrayAdapter; // initialize a customized ArrayAdapter for future use
    private final String DAILY_MEAL_PLAN_COLLECTION_NAME = "Daily Meal Plans";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_meal_plan);


        Button addDailyMealButton = findViewById(R.id.add_daily_meal_button); // used to add a new meal in when clicking
        ListView dailyMealListView = findViewById(R.id.daily_meals_listView); // display the meal items
        dailyMealArrayAdapter = new DailyMealPlanCustomList(getApplicationContext(), dailyMealDataList);
        FirebaseFirestore appDatabase = FirebaseFirestore.getInstance(); // instantiate
        CollectionReference dailyMealPlanCollectionReference = appDatabase.collection(DAILY_MEAL_PLAN_COLLECTION_NAME);

        addDailyMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}