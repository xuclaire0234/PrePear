/**
 * Class Name: MealPlanCustomList
 * Version Information: Version 1.0
 * Date: Nov 5th, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

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

/**
 * This class defines the custom Meal plan list that shows the listview of all daily meal plans
 */
public class MealPlanCustomList extends ArrayAdapter<DailyMealPlan> {
    private ArrayList<DailyMealPlan> dailyMealPlans; // holds daily meal plan entries
    private Context context; // holds the MealPlanFragment context

    /**
     * This is the class constructor which creates MealPlanCustomList objects
     * @param contextParameter this is the app context which is of type {@link Context}
     * @param mealsParameter this is the {@link ArrayList<DailyMealPlan>} which stores the meal plans
     */
    public MealPlanCustomList(Context contextParameter,
                              ArrayList<DailyMealPlan> mealsParameter) {
        super(contextParameter, 0, mealsParameter);
        this.context = contextParameter;
        this.dailyMealPlans = mealsParameter;
    }

    /**
     * This function sets the style of the listView to view meal plans
     * @param position This is the index of the meal plan needed to view, which is of type {@link Integer}
     * @param convertView This is the convert view which is of type {@link View}
     * @param parent This is the parent view which is of type {@link ViewGroup}
     * @return The return is of type {@link View}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView; // get the reference to the current convertView object
        if (view == null) {
            // if the convertView holds nothing, then inflate the content_meal_plan.xml
            view = LayoutInflater.from(context).inflate(R.layout.content_meal_plan, parent, false);
        }
        /*
         on below line get the current meal plan entry,
         and extract its information from the daily meal plans list
         */
        DailyMealPlan plan = dailyMealPlans.get(position);
        /* and set this meal plan date to the corresponding Textview object for displaying on the fragment
         */
        TextView mealDate = view.findViewById(R.id.meal_date);
        mealDate.setText(plan.getCurrentDailyMealPlanDate());

        return view; // return this view
    }
}

