/**
 * Recipe
 *
 * Version 1.0
 *
 * Date
 *
 * Copyright
 */
package com.example.prepear;

import java.io.Serializable;

import io.grpc.internal.DnsNameResolver;

/**
 * This Class creates an object to represent an ingredient used in recipe with its brief description
 * {@link String}, amount {@link Integer}, unit {@link String}, category of ingredient {@link String}
 * and ingredient's id in database {@link String}
 */
public class IngredientInRecipe implements Serializable {
    /**
     * This variable is private and stores the brief description of the ingredient as {@link String}
     */
    private String briefDescription;
    /**
     * This variable is private and stores the amount of the ingredient as {@link Integer}
     */
    private Integer amount;
    /**
     * This variable is private and stores the unit of the ingredient as {@link String}
     */
    private String unit;
    /**
     * This variable is private and stores the category of the ingredient as {@link String}
     */
    private String ingredientCategory;
    /**
     * This variable is private and stores the id of the ingredient in database as {@link String}
     */
    private String id;

    /**
     * This is the constructor to create the object of IngredientInRecipe
     * @param briefDescription This is the brief description of the ingredient which is of type {@link String}
     * @param amount This is the amount of the ingredient which is of type {@link Integer}
     * @param unit This is the unit of the ingredient which is of type {@link String}
     * @param ingredientCategory This is the category of the ingredient whci is of type {@link String}
     */
    public IngredientInRecipe(String briefDescription, int amount, String unit, String ingredientCategory) {
        this.briefDescription = briefDescription;
        this.amount = amount;
        this.unit = unit;
        this.ingredientCategory = ingredientCategory;
    }

    /**
     * This function returns the brief description of the ingredient
     * @return The return is of type {@link String}
     */
    public String getBriefDescription() {
        return this.briefDescription;
    }

    /**
     * This function sets the brief description of the ingredient
     * @param briefDescription This is the brief description of the ingredient which is of type
     *                         {@link String}
     */
    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    /**
     * This function returns the amount of the ingredient
     * @return The return is of type {@link Integer}
     */
    public Integer getAmount() {
        return this.amount;
    }

    /**
     * This function sets the amount of the ingredient
     * @param amount This is the amount of the ingredient, which is of type {@link Integer}
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * This function returns the unit of the ingredient
     * @return The return is of type {@link String}
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * This function sets the unit of the ingredient
     * @param unit This is the uni to the ingredient, which is of type {@link String}
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * This function returns the category of the ingredient
     * @return The return is of type {@link String}
     */
    public String getIngredientCategory() {
        return this.ingredientCategory;
    }

    /**
     * This function sets the ingredient category
     * @param ingredientCategory This is the ingredient's category, which is of type {@link String}
     */
    public void setIngredientCategory(String ingredientCategory) {
        this.ingredientCategory = ingredientCategory;
    }

    /**
     * This function returns the id of the ingredients in the database
     * @return The return is of type {@link String}
     */
    public String getId() {
        return this.id;
    }

    /**
     * This function sets the id of the ingredient in database
     * @param id This is the id of the ingredient in database, which is of type {@link String}
     */
    public void setId(String id) {
        this.id = id;
    }
}
