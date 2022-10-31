/*
 * Class Name: IngredientInStorage
 * Version: 1.0
 * Date: Oct 25th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 * */

package com.example.prepear;

/**
 * This class defines {@link IngredientInStorage} objects which extends Ingredient class
 * and allows setting and accessing the attributes
 * @author: Marafi Mergani
 * @version: 1
 */
public class IngredientInStorage extends Ingredient{
    // initialize additional class attributes
    // <access_identifier> variableName;
    private String bestBeforeDate;
    private String location;

    /**
     * This constructor creates an {@link IngredientInStorage} object with the given attributes
     * using the Ingredient class constructor
     * @param description a String for the description entered
     * @param category a String for the category entered
     * @param date a String for the date picked
     * @param location a String for the location entered
     * @param amount an int for the number of ingredients
     * @param unit aa int for the unit cost of the ingredient
     */
    public IngredientInStorage(String description, String category, String date, String location, int amount, String unit){
        super(description, amount, unit, category);
        this.bestBeforeDate = date;
        this.location = location;
    }

    /**
     * This method returns the best before date of the ingredient
     * @return bestBeforeDate a String for the date entered
     */
    public String getBestBeforeDate() {
        return this.bestBeforeDate;
    }

    /**
     * This method sets the best before date of the ingredient
     * @param  bestBeforeDate a String for the date entered
     */
    public void setBestBeforeDate(String bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * This method returns the location of the ingredient
     * @return  location a String for the location entered
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * This method sets the location of the ingredient
     * @param  location a String for the location entered
     */
    public void setLocation(String location) {
        this.location = location;
    }
}