/**
 * Classname: IngredientInRecipeTest
 * Version Information: 1.0.0
 * Date: 11/23/2022
 * Author: Yingyue Cao
 * Copyright Notice:
 */
package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RecipeControllerTest {
    private RecipeController recipeController;

    /**
     * Before each run, initialize an RecipeController
     */
    @BeforeEach
    private void mockRecipeController() {
        recipeController = new RecipeController();
    }
    /**
     * This function returns a mockRecipe
     * @return the return is of type {@link Recipe}
     */
    private Recipe mockRecipe() {
        return new Recipe("","Tomato Soup",30,
                3,"dinner","none");
    }
    /**
     * This function returns a different mockRecipe
     * @return the return is of type {@link Recipe}
     */
    private Recipe differentMockRecipe() {
        return new Recipe("","Milk Shake",60,
                4,"drinks","great");
    }
    /**
     * Testing add recipe to recipe controller
     */
    @Test
    @DisplayName("This function is for testing add")
    void addRecipeTest() {
        /* check if the controller is empty now */
        assertEquals(0, recipeController.getRecipes().size());

        /* check add ingredient to controller */
        Recipe newRecipe = mockRecipe();
        recipeController.addRecipe(newRecipe);
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains
    }
    /**
     * Testing remove recipe in recipe controller
     */
    @Test
    @DisplayName("This function is for testing delete")
    void removeRecipeTest() {
        /* add an recipe to the controller */

        Recipe newRecipe = mockRecipe();
        recipeController.addRecipe(newRecipe);
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains

        /* test to delete an ingredient that's not in the controller */
        Recipe differentRecipe = differentMockRecipe();
        recipeController.deleteRecipe(differentRecipe); // the controller should not changed in this case
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertFalse(recipeController.getRecipes().contains(differentRecipe)); // controller should not contain

        /* test to delete an ingredient that's in the controller */
        recipeController.deleteRecipe(newRecipe);
        assertEquals(0,recipeController.getRecipes().size()); // check the length of controller
        assertFalse(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains
    }
    /**
     * Testing edit recipe controller
     */
    @Test
    @DisplayName("This function is for testing edit")
    void editRecipeTest() {
        // add an recipe to the controller
        Recipe recipe = mockRecipe();
        recipeController.addRecipe(recipe);
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(recipe)); // check if the controller contains

        // test editing the given recipe with a new recipe
        Recipe differentRecipe = differentMockRecipe();
        recipeController.editRecipe(recipe, differentRecipe);
        assertTrue(recipeController.getRecipes().contains(differentRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));
    }
    /**
     * Testing count in recipe controller
     */
    @Test
    @DisplayName("This function is for testing count")
    void countRecipeTest() {
        // add an recipe to the controller
        Recipe recipe = mockRecipe();
        Recipe differentRecipe = differentMockRecipe();
        recipeController.addRecipe(recipe);
        assertEquals(1,recipeController.countRecipes()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(recipe)); // check if the controller contains

        // test editing the given recipe with a new recipe
        recipeController.addRecipe(differentRecipe);
        assertEquals(2,recipeController.countRecipes()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(differentRecipe));
    }
    /**
     * Testing get recipe at given index in recipe controller
     */
    @Test
    @DisplayName("This function is for testing getting recipe at given index")
    void getRecipeAtTest() {
        // add an recipe to the controller
        Recipe recipe = mockRecipe();
        recipeController.addRecipe(recipe);
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(recipe)); // check if the controller contains

        // test getting recipe at given index
        assertEquals(recipe,recipeController.getRecipeAt(0)); // check the length of controller
    }
    /**
     * Testing get index for given recipe in recipe controller
     */
    @Test
    @DisplayName("This function is for testing getting recipe at given index")
    void getRecipeIndexTest() {
        // add an recipe to the controller
        Recipe recipe = mockRecipe();
        recipeController.addRecipe(recipe);
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(recipe)); // check if the controller contains

        // test getting index at given recipe
        assertEquals(0,recipeController.getRecipeIndex(recipe)); // check the length of controller
    }
    /**
     * Testing clearing all recipes in recipe controller
     */
    @Test
    @DisplayName("This function is for testing clear all recipes")
    void clearAllRecipesTest() {
        // add an recipe to the controller
        Recipe recipe = mockRecipe();
        Recipe differentRecipe = differentMockRecipe();
        recipeController.addRecipe(recipe);
        recipeController.addRecipe(differentRecipe);
        assertEquals(2,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(recipe)); // check if the controller contains

        // test clearing all recipes in controller
        recipeController.clearAllRecipes();
        assertEquals(0,recipeController.getRecipes().size()); // check the length of controller
    }
}
