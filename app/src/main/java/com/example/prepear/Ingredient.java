/**
 * Class Name: Ingredient
 * Version: 1.0
 * Create Date: Oct 25th, 2022
 * Last Edit Date: Nov 3rd, 2022
 * Author: Shihao Liu, Marafi Mergani,Jingyi Xu
 * Copyright Notice:
 */

package com.example.prepear;

import java.io.Serializable;

/**
 * This class creates Ingredient objects and allows setting and accessing the attributes
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
     * @param briefDescription a {@link String} for the description entered
     * @param ingredientCategory a {@link String} for the category entered
     * @param amount an {@link Number}numerical value for the amount of this ingredient
     * @param unit the {@link String} unit to used to measure and scale this ingredient
     */
    public Ingredient(String briefDescription, String amount, String unit, String ingredientCategory) {
        this.description = briefDescription;
        this.amount = amount;
        this.amountValue = Double.parseDouble(amount); // initialize actual amount's numerical value
        this.unit = unit;
        this.category = ingredientCategory;
    }

    /**
     * This method returns the description of the {@link Ingredient}
     * @return briefDescription a {@link String} for the description entered
     */
    public String getBriefDescription() {
        return this.description;
    }

    /**
     * This method sets the description of the  {@link Ingredient}
     * @param  description a {@link String} for the description entered
     */
    public void setBriefDescription(String description) {
        this.description = description;
    }

    /**
     * This method returns the amount of the  {@link Ingredient}
     * @return a {@link Double} amount for the amount entered
     */
    public double getAmountValue() {
        return this.amountValue;
    }

    /**
     * This method sets the amount of the  {@link Ingredient}
     * @param  amount an double (numerical) value for the amount entered
     */
    public void setAmountValue(double amount) {
        this.amountValue = amount;
    }

    /**
     * This method returns a String object as the unit to used to measure and scale this  {@link Ingredient}
     * @return a {@link String}unit used to measure and scale this  {@link Ingredient}
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * This method sets a String object as the unit to used to measure and scale this  {@link Ingredient}
     * @param  unit the {@link String} unit to used to measure and scale this  {@link Ingredient}
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * This method returns the category of the  {@link Ingredient}
     * @return a {@link String} ingredientCategory for the category entered
     */
    public String getIngredientCategory() {
        return this.category;
    }

    /**
     * This method sets the category of the i{@link Ingredient}
     * @param  category a {@link String} for the category entered
     */
    public void setIngredientCategory(String category) {
        this.category = category;
    }

    /**
     * This method sets the a {@link String} amount of the {@link Ingredient}
     * @param amount  a {@link String} amount user for amount entered
     */
    public void setAmountString(String amount) {
        this.amount = amount;
    }

    /**
     * This method get the {@link String} amount of the {@link Ingredient}
     * @return a {@link String} amount for updating the {@link Ingredient}
     */
    public String getAmountString(){
        return this.amount;
    }
}
