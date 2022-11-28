/**
 * Classname: DailyMealPlan
 * Version Information: 1.0.0
 * Date: 11/26/2022
 * Author: Shihao Liu, Jiayin He
 * Copyright notice:
 */

package com.example.prepear;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to create Daily Meal Plan objects as the storage of every-day meals,
 * and to be also used as a Daily Meal Plan Controller class to manipulate meal data
 * */
public class DailyMealPlan implements Serializable {
    private ArrayList<Meal> dailyMealDataList; // contains today's all meals (Meal objects)
    String currentDailyMealPlanDate; // represents this current daily meal plan's date

    /**
     * This method constructs the new daily meal plan.
     * @param currentDailyMealPlanDate the date the daily meal plan belongs to
     * @param firstMeal the first meal being added to the daily meal plan
     */
    public DailyMealPlan(String currentDailyMealPlanDate, Meal firstMeal) {
        this.currentDailyMealPlanDate = currentDailyMealPlanDate;
        this.dailyMealDataList = new ArrayList<>();
        dailyMealDataList.add(firstMeal);
    }
    public DailyMealPlan (String currentDailyMealPlanDate) {
        this.currentDailyMealPlanDate = currentDailyMealPlanDate;
        this.dailyMealDataList = new ArrayList<>();
    }

    /**
     * This method gets all meals inside this one-day daily meal plan.
     * @return all meals inside this one-day daily meal plan
     */
    public ArrayList<Meal> getDailyMealDataList() {
        return this.dailyMealDataList;
    }

    /**
     * This method sets all meals inside this one-day daily meal plan.
     * @param dailyMealDataList an one-day daily meal plan which stores all meals for this day
     */
    public void setDailyMealDataList(ArrayList<Meal> dailyMealDataList) {
        this.dailyMealDataList = dailyMealDataList;
    }

    /**
     * This method gets the date for this current daily meal plan.
     * @return the date for this current daily meal plan
     */
    public String getCurrentDailyMealPlanDate() {
        return this.currentDailyMealPlanDate;
    }

    /**
     * This method sets the date for this current daily meal plan
     * @param currentDailyMealPlanDate the date for this current daily meal plan to be set
     */
    public void setCurrentDailyMealPlanDate(String currentDailyMealPlanDate) {
        this.currentDailyMealPlanDate = currentDailyMealPlanDate;
    }

    /**
     * This method empties the daily meal plan.
     */
    public void emptyDailyMealDataList(){
        this.dailyMealDataList.clear();
    }
}