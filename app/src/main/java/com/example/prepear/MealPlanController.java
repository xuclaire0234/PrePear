package com.example.prepear;

import java.util.ArrayList;

public class MealPlanController {
    private ArrayList<DailyMealPlan> MealPlanDataList;
    /**
     * This function initializes the ingredient controller.
     */
    public MealPlanController(ArrayList<DailyMealPlan> MealPlanDataList){
        this.MealPlanDataList = MealPlanDataList;
    }

    public MealPlanController(){
        this.MealPlanDataList = new ArrayList<DailyMealPlan>();
    }
    public DailyMealPlan getMealPlan(int index){
        return this.MealPlanDataList.get(index);
    }

    /**
     * This function add a new ingredient to the ingredient data list
     */
    public void addMealPlan(DailyMealPlan mealToAdd) {
        this.MealPlanDataList.add(mealToAdd);
    }

    public int getSize(){
        return this.MealPlanDataList.size();
    }

    /**
     * This function delete existed ingredient in the ingredient data list
     */
    public void removeMealPlan(int index) {
        this.MealPlanDataList.remove(index);
    }

    /**
     * This function returns the list of ingredients
     * @return The return is of type {@link ArrayList}
     */
    public ArrayList<DailyMealPlan> getMealPlans() {
        return this.MealPlanDataList;
    }
}
