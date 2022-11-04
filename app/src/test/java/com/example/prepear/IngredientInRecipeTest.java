/**
 * Classname: IngredientInRecipeTest
 * Version Information: 1.0.0
 * Date: 11/3/2022
 * Author: Jamie Lee
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for ingredient in recipe part. All the unit tests are written here.
 */
public class IngredientInRecipeTest {
    IngredientInRecipe ingredientInRecipe = new IngredientInRecipe("Egg Salad",
            "4", "kg", "breakfast");

    @Test
    @DisplayName("This function is for testing getBriefDescription")
    /**
     * Test getter for ingredient in recipe class brief description attribute.
     */
    void testGetBriefDescription() {
        assertEquals("Egg Salad", ingredientInRecipe.getBriefDescription());
    }

    @Test
    @DisplayName("This function is for testing setBriefDescription")
    /**
     * Test setter for ingredient in recipe class brief description attribute.
     */
    void testSetDescription() {
        ingredientInRecipe.setBriefDescription("Egg Stew");
        assertEquals("Egg Stew", ingredientInRecipe.getBriefDescription());
    }

    @Test
    @DisplayName("This function is for testing getAmount")
    /**
     * Test getter for ingredient in recipe class amount attribute.
     */
    void testGetAmount() {
        assertEquals("4", ingredientInRecipe.getAmountString());
    }

    @Test
    @DisplayName("This function is for testing setAmount")
    /**
     * Test setter for ingredient in recipe class amount attribute.
     */
    void testSetAmount() {
        ingredientInRecipe.setAmountString("10");
        assertEquals("10", ingredientInRecipe.getAmountString());
    }

    @Test
    @DisplayName("This function is for testing getUnit")
    /**
     * Test getter for ingredient in recipe class unit attribute.
     */
    void testGetUnit() {
        assertEquals("kg", ingredientInRecipe.getUnit());
    }

    @Test
    @DisplayName("This function is for testing setUnit")
    /**
     * Test setter for ingredient in recipe class unit attribute.
     */
    void testSetUnit() {
        ingredientInRecipe.setUnit("lb");
        assertEquals("lb", ingredientInRecipe.getUnit());
    }

    @Test
    @DisplayName("This function is for testing getCategory")
    /**
     * Test getter for ingredient in recipe class ingredient category attribute.
     */
    void testGetCategory() {
        assertEquals("breakfast", ingredientInRecipe.getIngredientCategory());
    }

    @Test
    @DisplayName("This function is for testing setCategory")
    /**
     * Test setter for ingredient in recipe class ingredient category attribute.
     */
    void testSetCategory() {
        ingredientInRecipe.setIngredientCategory("lunch");
        assertEquals("lunch", ingredientInRecipe.getIngredientCategory());
    }
}