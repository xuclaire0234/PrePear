/**
 * Class Name: IngredientInStorageTest
 * Version: 1.0
 * Create Date: Nov 4th, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This class creates ingredientInStorage Object and use it to test
 * the class's getters and setters
 */
public class IngredientInStorageTest {
    private IngredientInStorage ingredient;

    @BeforeEach
    /**
     * This method creates an {@link IngredientInStorage} object to use it in the test methods
     */
    private void createIngredient() {
        ingredient = new IngredientInStorage("Tomato", "Vegetables",
                "2022-11-03", "Fridge", "4", "g", null);
    }

    @Test
    @DisplayName("This function is for testing ingredient in storage getters")
    /**
     * This method tests the IngredientInStorage Class getters
     */
    void testGetters() {
        /* check that ingredient getters return the correct attributes */
        assertEquals(ingredient.getBestBeforeDate(), "2022-11-03");
        assertEquals(ingredient.getLocation(), "Fridge");
        assertEquals(ingredient.getDocumentId(), null);
        assertEquals(ingredient.getClass(), IngredientInStorage.class);

    }

    @Test
    @DisplayName("This function is for testing ingredient in storage setters")
    /**
     * This method tests the IngredientInStorage Class setters
     */
    void testSetters() {
        /* set attributes of ingredient to new values */
        ingredient.setBestBeforeDate("2000-10-01");
        ingredient.setLocation("Freezer");
        /* check that the attributes of the ingredient got updated*/
        assertEquals(ingredient.getBestBeforeDate(), "2000-10-01");
        assertEquals(ingredient.getLocation(), "Freezer");
        assertEquals(ingredient.getClass(), IngredientInStorage.class);

    }
}