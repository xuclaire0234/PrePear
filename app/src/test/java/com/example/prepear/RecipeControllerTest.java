package com.example.prepear;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RecipeControllerTest {
    private RecipeController recipeController;

    @BeforeEach
    private void mockRecipeController() {
        recipeController = new RecipeController();
    }

    private Recipe mockRecipeWithAllAttributes() {
        return new Recipe("http://","Poutine",30,3,"sides","None");
    }

    private Recipe mockRecipeWithoutImageUri() {
        return new Recipe(null,"Poutine",30,3,"sides",null);
    }

    private Recipe mockRecipeWithoutComment() {
        return new Recipe("http://", "Poutine",30,3,"sides",null);
    }

    @Test
    @DisplayName("This function is for testing add")
    void addRecipeTest() {
        assertEquals(0, recipeController.getRecipes().size());

        Recipe newRecipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(newRecipe);
        assertEquals(1,recipeController.getRecipes().size());
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        newRecipe = mockRecipeWithoutComment();
        recipeController.addRecipe(newRecipe);
        assertEquals(2,recipeController.getRecipes().size());
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        newRecipe = mockRecipeWithoutImageUri();
        recipeController.addRecipe(newRecipe);
        assertEquals(3,recipeController.getRecipes().size());
        assertTrue(recipeController.getRecipes().contains(newRecipe));
    }

    @Test
    @DisplayName("This function is for testing delete")
    void deleteRecipeTest() {
        Recipe newRecipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(newRecipe);
        assertEquals(1,recipeController.getRecipes().size());
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        recipeController.deleteRecipe(newRecipe);
        assertEquals(0,recipeController.getRecipes().size());
        assertFalse(recipeController.getRecipes().contains(newRecipe));
    }

    @Test
    @DisplayName("This function is for testing edit")
    void editRecipeTest() {
        Recipe recipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(recipe);
        assertTrue(recipeController.getRecipes().contains(recipe));
        Recipe newRecipe = mockRecipeWithoutImageUri();
        recipeController.editRecipe(recipe,newRecipe);
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));

        Integer index = recipeController.getRecipeIndex(newRecipe);
        newRecipe = mockRecipeWithoutComment();
        recipeController.editRecipe(index,newRecipe);
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));
    }
}
