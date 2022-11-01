/*
* Class Name: Ingredient
* Version: 1.0
* Date: Oct 25th, 2022
* Author: Shihao Liu
* Copyright Notice:
* */

package com.example.prepear;


/**/


import java.io.Serializable;

/**
 * This class creates ingredient objects and allows setting and accessing the attributes
 * @author: Marafi Mergani
 * @version: 1
 */

public class Ingredient implements Serializable {
    // initialize class attributes
    // <access_identifier> variableName;
    private String briefDescription;
    private String amount;
    private double amountValue; // actual value of amount
    private String unit;
    private String ingredientCategory;

    /**
     * This constructor creates an {@link Ingredient} object with the given attributes
     * using the Ingredient class constructor
     * @param briefDescription a String for the description entered
     * @param ingredientCategory a String for the category entered
     * @param amount an int for the number of ingredients
     * @param unit aa int for the unit cost of the ingredient
     */
    public Ingredient(String briefDescription, String amount, String unit, String ingredientCategory) {
        this.briefDescription = briefDescription;
        this.amount = amount;
        this.amountValue = Double.parseDouble(amount);
        this.unit = unit;
        this.ingredientCategory = ingredientCategory;
    }

    /**
     * This method returns the description of the ingredient
     * @return briefDescription a String for the description entered
     */
    public String getBriefDescription() {
        return this.briefDescription;
    }

    /**
     * This method sets the description of the ingredient
     * @param  breifDescription a String for the description entered
     */
    public void setBriefDescription(String breifDescription) {
        this.briefDescription = breifDescription;
    }

    /**
     * This method returns the amount of the ingredient
     * @return amount an int for the amount entered
     */
    public double getAmount() {
        return this.amountValue;
    }

    /**
     * This method sets the amount of the ingredient
     * @param  amount an int for the amount entered
     */
    public void setAmount(double amount) {
        this.amountValue = amount;
    }

    /**
     * This method returns the unit cost of the ingredient
     * @return unit an int for the unit cost
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * This method sets the unit cost of the ingredient
     * @param  unit an int for the unit cost entered
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * This method returns the category of the ingredient
     * @return ingredientCategory a String for the category entered
     */
    public String getIngredientCategory() {
        return this.ingredientCategory;
    }

    /**
     * This method sets the category of the ingredient
     * @param  ingredientCategory a String for the category entered
     */
    public void setIngredientCategory(String ingredientCategory) {
        this.ingredientCategory = ingredientCategory;
    }
}
