package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Test class for Recipe class.
 */
public class RecipeTest {
    Recipe recipe = new Recipe("image", "Tomato Soup", 30, 3, "food", "none");

    /**
     * Testing getter and setter for Recipe class
     */
    @Test
    @DisplayName("This function is for testing getImageURI")
    void testGetImageURI() {
        assertEquals("image", recipe.getImageURI());
    }

    @Test
    @DisplayName("This function is for testing setImageURI")
    void testSetImageURI() {
        recipe.setImageURI("image1");
        assertEquals("image1", recipe.getImageURI());
    }
    @Test
    @DisplayName("This function is for testing getTitle")
    void testGetTitle() {
        assertEquals("Tomato Soup", recipe.getTitle());
    }
    @Test
    @DisplayName("This function is for testing setTitle")
    void testSetTitle() {
        recipe.setTitle("Tomato Stew");
        assertEquals("Tomato Stew", recipe.getTitle());
    }
    @Test
    @DisplayName("This function is for testing getPreparationTime")
    void testGetPreparationTime() {
        assertEquals(30, recipe.getPreparationTime());
    }
    @Test
    @DisplayName("This function is for testing setPreparationTime")
    void testSetPreparationTime() {
        recipe.setPreparationTime(20);
        assertEquals(20, recipe.getPreparationTime());
    }
    @Test
    @DisplayName("This function is for testing getNumberOfServings")
    void testGetNumberOfServings() {
        assertEquals(3, recipe.getNumberOfServings());
    }
    @Test
    @DisplayName("This function is for testing setNumberOfServings")
    void testSetNumberOfServings() {
        recipe.setNumberOfServings(5);
        assertEquals(5, recipe.getNumberOfServings());
    }
    @Test
    @DisplayName("This function is for testing getRecipeCategory")
    void testGetRecipeCategory() {
        assertEquals("food", recipe.getRecipeCategory());
    }
    @Test
    @DisplayName("This function is for testing setRecipeCategory")
    void testSetRecipeCategory() {
        recipe.setRecipeCategory("Meat");
        assertEquals("Meat", recipe.getRecipeCategory());
    }

}