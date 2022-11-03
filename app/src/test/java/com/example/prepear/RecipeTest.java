package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class RecipeTest {
    private CustomRecipeList recipeList;
    @BeforeEach
    private void mockCustomRecipeList() {
        // recipeList = new CustomRecipeList(".", mockRecipe());
        recipeList.add(mockRecipe());
    }
    @BeforeEach
    private Recipe mockRecipe() {
        Recipe recipe = new Recipe("image", "Tomato Soup", 30, 3, "food", "none");
        return recipe;
    }
    @Test
    @DisplayName("This function is for testing getImageURI")
    void testGetImageURI() {
        assertEquals("image", mockRecipe().getImageURI());
    }
    @Test
    @DisplayName("This function is for testing setImageURI")
    void testSetImageURI() {
        mockRecipe().setImageURI("image1");
        assertEquals("image1", mockRecipe().getImageURI());
    }
    @Test
    @DisplayName("This function is for testing getTitle")
    void testGetTitle() {
        assertEquals("Tomato Soup", mockRecipe().getTitle());
    }
    @Test
    @DisplayName("This function is for testing setTitle")
    void testSetTitle() {
        mockRecipe().setTitle("Tomato Stew");
        assertEquals("Tomato Stew", mockRecipe().getTitle());
    }
    @Test
    @DisplayName("This function is for testing getPreparationTime")
    void testGetPreparationTime() {
        assertEquals(30, Optional.ofNullable(mockRecipe().getPreparationTime()));
    }
    @Test
    @DisplayName("This function is for testing setPreparationTime")
    void testSetPreparationTime() {
        mockRecipe().setPreparationTime(20);
        assertEquals(20, Optional.ofNullable(mockRecipe().getPreparationTime()));
    }
    @Test
    @DisplayName("This function is for testing getNumberOfServings")
    void testGetNumberOfServings() {
        assertEquals(3, Optional.ofNullable(mockRecipe().getNumberOfServings()));
    }
    @Test
    @DisplayName("This function is for testing setNumberOfServings")
    void testSetNumberOfServings() {
        mockRecipe().setNumberOfServings(5);
        assertEquals(5, Optional.ofNullable(mockRecipe().getNumberOfServings()));
    }
    @Test
    @DisplayName("This function is for testing getRecipeCategory")
    void testGetRecipeCategory() {
        assertEquals("food", mockRecipe().getRecipeCategory());
    }
    @Test
    @DisplayName("This function is for testing setRecipeCategory")
    void testSetRecipeCategory() {
        mockRecipe().setRecipeCategory("Meat");
        assertEquals("Meat", mockRecipe().getRecipeCategory());
    }

}
