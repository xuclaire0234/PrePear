package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ViewDailyMealPlanActivity extends AppCompatActivity {
    private Button addDailyMealButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_meal_plan);
    }
}