/**
 * Class Name: IngredientInStorage
 * Version: 1.0
 * Create Date: Oct 25th, 2022
 * Last Edit Date: Nov 3rd, 2022
 * Author: Shihao Liu, Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

/**
 * This class defines {@link IngredientInStorage} objects which extends Ingredient class
 * and allows setting and accessing the attributes
 * @author: Shihao Liu, Marafi Mergani
 * @version: 1
 */
public class IngredientInStorage extends Ingredient {
    // initialize class attributes
    private String bestBeforeDate;
    private String location;
    // On below: unique id for storing as a document with its detailed information combined into a Map object inside Ingredient Storage collection
    private String documentId;
    private int iconCode;

    /**
     * This constructor creates an {@link IngredientInStorage} object with the given attributes
     * using the Ingredient class constructor
     * @param description a String for the entered description for this in-storage ingredient
     * @param category a String for the selected category for this in-storage ingredient
     * @param date a String for the picked best before date for this in-storage ingredient
     * @param location a String for the entered location  for this in-storage ingredient
     * @param amount an numerical value for the amount of this in-storage ingredient
     * @param unit the unit to used to measure and scale this in-storage ingredient
     * @param iconCode store the icon image name that user selected for this in-storage ingredient
     */
    public IngredientInStorage(String description, String category, String date, String location, String amount, String unit, String documentId,int iconCode){
        super(description, amount, unit, category);
        this.bestBeforeDate = date;
        this.location = location;
        this.documentId = documentId;
        this.iconCode = iconCode;
    }

    /**
     * This method returns the best before date of the ingredient
     * @return bestBeforeDate a String for the date entered
     */
    public String getBestBeforeDate() {
        return bestBeforeDate;
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
     * @return  location: a String for the location entered
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method sets the location of the ingredient
     * @param  location a String for the location entered
     */
    public void setLocation(String location) {
        this.location = location;
    }


    /**
     * @return documentId a String for the ingredient's document id inside the storage Collection
     */
    public String getDocumentId() {
        return this.documentId;
    }

    /**
     * This method sets the document id of the ingredient from "Ingredient Storage" Collection
     * @param  documentId a String for the the ingredient's document id inside the storage Collection auto-generated
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * This method gets the icon code of the ingredient
     * @return iconCode: an int for the user selected icon
     */
    public int getIconCode() {
        return iconCode;
    }

    /**
     * This method sets the icon code of the ingredient
     * @param iconCode a string for the user selected icon image
     */
    public void setIconCode(int iconCode) {
        this.iconCode = iconCode;
    }
}