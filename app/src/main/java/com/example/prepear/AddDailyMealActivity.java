package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class AddDailyMealActivity extends AppCompatActivity {
    private String mealNameStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_meal);

        TextView addedMealNameTextView = findViewById(R.id.meal_name_text);
        TextInputEditText addedMealAmountEditText = findViewById(R.id.meal_amount_input);
        Button addMealButton = findViewById(R.id.add_meal_in_plan_button);
        Button pickIngredientTypeMealButton = findViewById(R.id.pick_in_storage_meal_button);
        Button pickRecipeTypeMealButton = findViewById(R.id.pick_recipe_meal_button);

        

    }


}