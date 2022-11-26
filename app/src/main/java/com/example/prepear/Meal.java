/**
 * Classname: Meal
 * Version Information: 2.0.0
 * Date: 11/16/2022
 * Author: Jiayin He, Shihao Liu
 * Copyright Notice:
 */

package com.example.prepear;

import java.io.Serializable;

/**
 * This class creates Meal objects and allows to initialize, set and access the attributes.
 */
public class Meal implements Serializable {
    // On below part: initialize Meal class attributes
    private String mealID;  // represents the meal's own ID
    private String mealType;    // represents the meal type, either an in-storage ingredient or a dish cooked based on a recipe
    private String documentID;  // contains the same Document ID as its corresponding in-storage ingredient / recipe has inside database
    private double customizedAmount = 0;    // represents user-entered customized amount
    private Integer customizedNumberOfServings = 0; // represents user-entered customized number of servings
    private Integer eatHour = 24;   // represents the hour of the day user defined to eat the meal
    private Integer eatMinute = 24; // represents the minute of the day user defined to eat the meal

    /**
     * This method creates a object which is of type {@link Meal}.
     * @param mealType the type of the meal which is of type {@link String}
     * @param documentID the document id of the associated ingredient/recipe in ingredientInStorage/RecipeList
     */
    public Meal(String mealType, String documentID, String mealID) {
        this.mealType = mealType;
        this.documentID = documentID;
        this.mealID = mealID;
    }

    /**
     * This method gets the id of the meal.
     * @return the id of the meal to be set
     */
    public String getMealID() {
        return mealID;
    }

    /**
     * This method sets the id of the meal.
     * @param mealID the id of the meal to be set
     */
    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    /**
     * This method gets the meal type.
     * @return the type of the meal
     */
    public String getMealType() {
        return this.mealType;
    }

    /**
     * This method sets the meal type.
     * @param mealType the type of the meal to be set
     */
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    /**
     * This method gets the document id of the meal.
     * @return the document id of the meal
     */
    public String getDocumentID() {
        return this.documentID;
    }

    /**
     * This method sets the document id of the meal.
     * @param documentID the document id of the meal to be set
     */
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    /**
     * This method gets the customized amount of the ingredient type meal.
     * @return the customized amount of the ingredient type meal
     */
    public double getCustomizedAmount() {
        return this.customizedAmount;
    }

    /**
     * This method sets the customized amount of the ingredient type meal.
     * @param customizedAmount the customized amount of the ingredient type meal to be set
     */
    public void setCustomizedAmount(double customizedAmount) {
        this.customizedAmount = customizedAmount;
    }

    /**
     * This method gets the customized number of servings of the recipe type meal.
     * @return the customized number of servings of the recipe type meal
     */
    public Integer getCustomizedNumberOfServings() {
        return this.customizedNumberOfServings;
    }

    /**
     * This method sets the customized number of servings of the recipe type meal.
     * @param customizedNumberOfServings the customized number of servings of the recipe type meal to be set
     */
    public void setCustomizedNumberOfServings(Integer customizedNumberOfServings) {
        this.customizedNumberOfServings = customizedNumberOfServings;
    }

    /**
     * This method does adding extra meal amount for the existing ingredient type meal
     * @param extraCustomizedAmount the amount that the user want to add extra on the same ingredient type meal object
     */
    public void addExtraCustomizedAmount(double extraCustomizedAmount) {
        this.customizedAmount += extraCustomizedAmount;
        this.customizedAmount = Math.round(this.customizedAmount * 100.0) / 100.0;
    }

    /**
     * This method gets the hour of the day user defined to eat the meal
     * @return the hour of the day user defined to eat the meal
     */
    public Integer getEatHour() {
        return eatHour;
    }

    /**
     * This method sets the hour of the day user defined to eat the meal.
     * @param eatHour the hour of the day user defined to eat the meal to be set
     */
    public void setEatHour(Integer eatHour) {
        this.eatHour = eatHour;
    }

    /**
     * This method gets the minute of the day user defined to eat the meal.
     * @return the minute of the day user defined to eat the meal
     */
    public Integer getEatMinute() {
        return eatMinute;
    }

    /**
     * This method sets the minute of the day user defined to eat the meal.
     * @param eatMinute the minute of the day user defined to eat the meal to be set
     */
    public void setEatMinute(Integer eatMinute) {
        this.eatMinute = eatMinute;
    }
}