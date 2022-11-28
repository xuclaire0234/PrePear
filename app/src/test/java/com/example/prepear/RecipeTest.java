/**
 * Classname: RecipeTest
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
 * Test class for recipe part. All the unit tests are written here.
 */
public class RecipeTest {
    Recipe recipe = new Recipe("image", "Tomato Soup", 30, 3, "food", "none");

    @Test
    @DisplayName("This function is for testing getImageURI")
    /**
     * Test getter for Recipe class ImageURI attribute.
     */
    void testGetImageURI() {
        assertEquals("image", recipe.getImageURI());
    }

    @Test
    @DisplayName("This function is for testing setImageURI")
    /**
     * Test setter for Recipe class ImageURI attribute.
     */
    void testSetImageURI() {
        recipe.setImageURI("image1");
        assertEquals("image1", recipe.getImageURI());
    }

    @Test
    @DisplayName("This function is for testing getTitle")
    /**
     * Test getter for Recipe class title attribute.
     */
    void testGetTitle() {
        assertEquals("Tomato Soup", recipe.getTitle());
    }

    @Test
    @DisplayName("This function is for testing setTitle")
    /**
     * Test setter for Recipe class title attribute.
     */
    void testSetTitle() {
        recipe.setTitle("Tomato Stew");
        assertEquals("Tomato Stew", recipe.getTitle());
    }

    @Test
    @DisplayName("This function is for testing getPreparationTime")
    /**
     * Test getter for Recipe class preparation time attribute.
     */
    void testGetPreparationTime() {
        assertEquals(30, recipe.getPreparationTime());
    }

    @Test
    @DisplayName("This function is for testing setPreparationTime")
    /**
     * Test setter for Recipe class preparation time attribute.
     */
    void testSetPreparationTime() {
        recipe.setPreparationTime(20);
        assertEquals(20, recipe.getPreparationTime());
    }

    @Test
    @DisplayName("This function is for testing getNumberOfServings")
    /**
     * Test getter for Recipe class number of servings attribute.
     */
    void testGetNumberOfServings() {
        assertEquals(3, recipe.getNumberOfServings());
    }

    @Test
    @DisplayName("This function is for testing setNumberOfServings")
    /**
     * Test setter for Recipe class number of servings attribute.
     */
    void testSetNumberOfServings() {
        recipe.setNumberOfServings(5);
        assertEquals(5, recipe.getNumberOfServings());
    }

    @Test
    @DisplayName("This function is for testing getRecipeCategory")
    /**
     * Test getter for Recipe class recipe category attribute.
     */
    void testGetRecipeCategory() {
        assertEquals("food", recipe.getRecipeCategory());
    }

    @Test
    @DisplayName("This function is for testing setRecipeCategory")
    /**
     * Test setter for Recipe class recipe category attribute.
     */
    void testSetRecipeCategory() {
        recipe.setRecipeCategory("Meat");
        assertEquals("Meat", recipe.getRecipeCategory());
    }
}