package com.example.prepear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MealPlanCustomList extends ArrayAdapter<DailyMealPlan> {

    private ArrayList<DailyMealPlan> dailyMealPlans; // holds for ingredients in storage entries
    private Context context; // holds for ViewIngredientStorage activity's context

    /* constructor comment */
    public MealPlanCustomList(Context contextParameter,
                              ArrayList<DailyMealPlan> mealsParameter) {
        super(contextParameter, 0, mealsParameter);
        this.context = contextParameter;
        this.dailyMealPlans = mealsParameter;
    }

    @NonNull
    @Override
    /*method comment*/
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView; // get the reference to the current convertView object

        if (view == null) {
            // if the convertView holds nothing, then inflate the ingredient_info.xml
            view = LayoutInflater.from(context).inflate(R.layout.content_meal_plan, parent, false);
        }
        // on below line get the current meal plan entry,
        // and extract its information from the meals list
        DailyMealPlan plan = dailyMealPlans.get(position);
        // and set this meal plan description,
        // best before date and unit count to the corresponding Textview object for displaying on the activity
        TextView mealDate = view.findViewById(R.id.meal_date);
        mealDate.setText(plan.getCurrentDailyMealPlanDate());

        return view; // return this view
    }
}

