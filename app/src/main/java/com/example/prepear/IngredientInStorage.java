/**
 * Class Name: IngredientInStorage
 * Version: 1.0
 * Create Date: Oct 25th, 2022
 * Last Edit Date: Nov 3rd, 2022
 * Author: Shihao Liu, Marafi Mergani, Jingyi Xu
 * Copyright Notice:
 */

package com.example.prepear;

import java.io.Serializable;

/**
 * This class defines {@link IngredientInStorage} objects which extends Ingredient class
 * and allows setting and accessing the attributes
 * @author: Shihao Liu, Marafi Mergani
 * @version: 1
 */
public class IngredientInStorage extends Ingredient implements Serializable {
    // initialize class attributes
    private String bestBeforeDate;
    private String location;
    // On below: unique id for storing as a document with its detailed information combined into a Map object inside Ingredient Storage collection
    private String documentId;
    private int iconCode;

    /**
     * This constructor creates an {@link IngredientInStorage} object with the given attributes
     * using the Ingredient class constructor
     * @param description a {@link String} for the entered description for {@link IngredientInStorage}
     * @param category a {@link String} for the selected category for this {@link IngredientInStorage}
     * @param date a {@link String} for the picked best before date for this {@link IngredientInStorage}
     * @param location a {@link String} for the entered location  for this {@link IngredientInStorage}
     * @param amount a {@link String} for the amount of this {@link IngredientInStorage}
     * @param unit the {@link String} unit used to measure and scale this {@link IngredientInStorage}
     * @param iconCode a {@link Integer} store the icon image name that user selected for this {@link IngredientInStorage}
     */
    public IngredientInStorage(String description, String category, String date, String location, String amount, String unit, String documentId,int iconCode){
        super(description, amount, unit, category);
        this.bestBeforeDate = date;
        this.location = location;
        this.documentId = documentId;
        this.iconCode = iconCode;
    }

    /**
     * This method returns the best before date of the {@link IngredientInStorage}
     * @return bestBeforeDate a {@link String}  for the date entered
     */
    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * This method sets the best before date of the {@link IngredientInStorage}
     * @param  bestBeforeDate a {@link String}  for the date entered
     */
    public void setBestBeforeDate(String bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * This method returns the location of the {@link IngredientInStorage}
     * @return  location: a {@link String}  for the location entered
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method sets the location of the {@link IngredientInStorage}
     * @param  location a {@link String}  for the location entered
     */
    public void setLocation(String location) {
        this.location = location;
    }


    /**
     * @return documentId a {@link String}  for the {@link IngredientInStorage}'s document id inside the storage Collection
     */
    public String getDocumentId() {
        return this.documentId;
    }

    /**
     * This method sets the document id of the {@link IngredientInStorage} from "Ingredient Storage" Collection
     * @param  documentId a {@link String}  for the the {@link IngredientInStorage} document id inside the storage Collection auto-generated
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * This method gets the icon code of the {@link IngredientInStorage}
     * @return iconCode: a {@link Integer} for the user selected icon
     */
    public int getIconCode() {
        return iconCode;
    }

    /**
     * This method sets the icon code of the {@link IngredientInStorage}
     * @param iconCode a {@link Integer} for the user selected icon image
     */
    public void setIconCode(int iconCode) {
        this.iconCode = iconCode;
    }
}