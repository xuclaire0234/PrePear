/**
 * Class Name: Ingredient
 * Version: 1.0
 * Create Date: Oct 25th, 2022
 * Last Edit Date: Nov 3rd, 2022
 * Author: Shihao Liu, Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import java.io.Serializable;

/**
 * This class creates ingredient objects and allows setting and accessing the attributes
 */

public class Ingredient implements Serializable {
    // initialize class attributes
    private String description;
    private String amount;
    private double amountValue; // actual numerical value of amount
    private String unit;
    private String category;

    /**
     * This constructor creates an {@link Ingredient} object with the given attributes
     * @param briefDescription a String for the description entered
     * @param ingredientCategory a String for the category entered
     * @param amount an numerical value for the amount of this ingredient
     * @param unit the unit to used to measure and scale this ingredient
     */
    public Ingredient(String briefDescription, String amount, String unit, String ingredientCategory) {
        this.description = briefDescription;
        this.amount = amount;
        this.amountValue = Double.parseDouble(amount); // initialize actual amount's numerical value
        this.unit = unit;
        this.category = ingredientCategory;
    }

    /**
     * This method returns the description of the ingredient
     * @return briefDescription a String for the description entered
     */
    public String getBriefDescription() {
        return this.description;
    }

    /**
     * This method sets the description of the ingredient
     * @param  description a String for the description entered
     */
    public void setBriefDescription(String description) {
        this.description = description;
    }

    /**
     * This method returns the amount of the ingredient
     * @return amount an int for the amount entered
     */
    public double getAmountValue() {
        return this.amountValue;
    }

    /**
     * This method sets the amount of the ingredient
     * @param  amount an double (numerical) value for the amount entered
     */
    public void setAmountValue(double amount) {
        this.amountValue = amount;
    }

    /**
     * This method returns a String object as the unit to used to measure and scale this ingredient
     * @return unit the unit to used to measure and scale this ingredient
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * This method sets a String object as the unit to used to measure and scale this ingredient
     * @param  unit the unit to used to measure and scale this ingredient
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * This method returns the category of the ingredient
     * @return ingredientCategory a String for the category entered
     */
    public String getIngredientCategory() {
        return this.category;
    }

    /**
     * This method sets the category of the ingredient
     * @param  category a String for the category entered
     */
    public void setIngredientCategory(String category) {
        this.category = category;
    }

    public void setAmountString(String amount) {
        this.amount = amount;
    }

    public String getAmountString(){
        return this.amount;
    }
}
