/**
 * Classname: IngredientInRecipe
 * Version Information: 1.0.0
 * Date: 11/2/2022
 * Author: Yingyue Cao
 * Copyright notice:
 */
package com.example.prepear;

import java.io.Serializable;

import io.grpc.internal.DnsNameResolver;

/**
 * This Class creates an object to represent an ingredient used in recipe with its brief description
 * {@link String}, amount {@link Integer}, unit {@link String}, category of ingredient {@link String}
 * and ingredient's id in database {@link String}
 */
public class IngredientInRecipe extends Ingredient implements Serializable {

    /**
     * This variable is private and stores the id of the ingredient in database as {@link String}
     */
    private String id;

    /**
     * This constructor creates an {@link Ingredient} object with the given attributes
     * using the Ingredient class constructor
     *
     * @param description a String for the description entered
     * @param amount      an int for the number of ingredients
     * @param unit        aa int for the unit cost of the ingredient
     * @param category    a String for the category entered
     */
    public IngredientInRecipe(String description, String amount, String unit, String category) {
        super(description, amount, unit, category);
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