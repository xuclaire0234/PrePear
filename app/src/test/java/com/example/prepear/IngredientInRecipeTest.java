package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for IngredientInRecipe
 */
public class IngredientInRecipeTest {
    IngredientInRecipe ingredientInRecipe = new IngredientInRecipe("Egg Salad", "4", "kg", "breakfast");

    /**
     * Testing getter and setter for IngredientInRecipe class
     */
    @Test
    @DisplayName("This function is for testing getBriefDescription")
    void testGetBriefDescription() {
        assertEquals("Egg Salad", ingredientInRecipe.getBriefDescription());
    }
    @Test
    @DisplayName("This function is for testing setBriefDescription")
    void testSetDescription() {
        ingredientInRecipe.setBriefDescription("Egg Stew");
        assertEquals("Egg Stew", ingredientInRecipe.getBriefDescription());
    }
    @Test
    @DisplayName("This function is for testing getAmount")
    void testGetAmount() {
        assertEquals("4", ingredientInRecipe.getAmountString());
    }
    @Test
    @DisplayName("This function is for testing setAmount")
    void testSetAmount() {
        ingredientInRecipe.setAmountString("10");
        assertEquals("10", ingredientInRecipe.getAmountString());
    }
    @Test
    @DisplayName("This function is for testing getUnit")
    void testGetUnit() {
        assertEquals("kg", ingredientInRecipe.getUnit());
    }
    @Test
    @DisplayName("This function is for testing setUnit")
    void testSetUnit() {
        ingredientInRecipe.setUnit("lb");
        assertEquals("lb", ingredientInRecipe.getUnit());
    }
    @Test
    @DisplayName("This function is for testing getCategory")
    void testGetCategory() {
        assertEquals("breakfast", ingredientInRecipe.getIngredientCategory());
    }
    @Test
    @DisplayName("This function is for testing setCategory")
    void testSetCategory() {
        ingredientInRecipe.setIngredientCategory("lunch");
        assertEquals("lunch", ingredientInRecipe.getIngredientCategory());
    }
}