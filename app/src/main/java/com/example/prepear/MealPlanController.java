/**
 * Class Name: MealPlanController
 * Version Information: Version 1.0
 * Date: Nov 19th, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import java.util.ArrayList;

public class MealPlanController {
    private ArrayList<DailyMealPlan> MealPlanDataList;

    /**
     * This is the class constructor which creates MealPlanController objects
     * @param MealPlanDataList {@link ArrayList<DailyMealPlan>} the list of meal plans to handle
     */
    public MealPlanController(ArrayList<DailyMealPlan> MealPlanDataList){
        this.MealPlanDataList = MealPlanDataList;
    }

    /**
     * This method return the meal plan at the given index
     * @param index an {@link Integer} position of the meal in the meal plan data list
     * @return DailyMealPlan object at the specified index
     */
    public DailyMealPlan getMealPlan(int index){
        return this.MealPlanDataList.get(index);
    }

    /**
     * This method adds a meal plan to the data list
     * @param mealToAdd {@link DailyMealPlan} object to add
     */
    public void addMealPlan(DailyMealPlan mealToAdd) {
        this.MealPlanDataList.add(mealToAdd);
    }

    /**
     * This method returns the size of the meal plan data list
     * @return the {@link Integer} size of the list
     */
    public int getSize(){
        return this.MealPlanDataList.size();
    }

    /**
     * This method removes the meal plan at the position specified by the index
     * @param index the {@link Integer} position of the meal plan to remove
     */
    public DailyMealPlan removeMealPlan(int index) {
        if (this.MealPlanDataList.get(index) != null) {
            return this.MealPlanDataList.remove(index);
        }
        return null;
    }

    /**
     * This function returns the list of meal plans
     * @return The return is of type {@link ArrayList}
     */
    public ArrayList<DailyMealPlan> getDailyMealPlans() {
        return this.MealPlanDataList;
    }
}