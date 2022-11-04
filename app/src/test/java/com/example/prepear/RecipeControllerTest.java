/**
 * Classname: RecipeControllerTest
 * Version Information: 1.0.0
 * Date: 11/3/2022
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

    @BeforeEach
    private void mockRecipeController() {
        recipeController = new RecipeController();
    }

    private Recipe mockRecipeWithAllAttributes() {
        return new Recipe("http://","Cookie",15,3,"snacks","None");
    }

    private Recipe mockRecipeWithoutImageUri() {
        return new Recipe(null,"Egg Stew",30,1,"stew",null);
    }

    private Recipe mockRecipeWithoutComment() {
        return new Recipe("http://", "Smashed Potato",12,2,"main",null);
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

        Recipe anotherRecipe = mockRecipeWithoutComment();
        recipeController.deleteRecipe(anotherRecipe);
        assertEquals(1,recipeController.getRecipes().size());
        assertFalse(recipeController.getRecipes().contains(anotherRecipe));


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
        recipeController.editRecipe(recipe, newRecipe);
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));

        Integer index = recipeController.getRecipeIndex(newRecipe);
        newRecipe = mockRecipeWithoutComment();
        recipeController.editRecipe(index, newRecipe);
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));

        Recipe anotherRecipe = new Recipe(null,"Stewed Beef",35,3,"Stew","None");
        recipeController.editRecipe(recipe, anotherRecipe);
        assertFalse(recipeController.getRecipes().contains(anotherRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));
    }

    @Test
    @DisplayName("This function is for testing count")
    void countRecipeTest() {
        Recipe recipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(recipe);
        recipe = mockRecipeWithoutComment();
        assertEquals(1,recipeController.countRecipes());

        recipeController.addRecipe(recipe);
        assertEquals(2,recipeController.countRecipes());
    }

    @Test
    @DisplayName("This function is for getRecipeAt")
    void getRecipeAtTest() {
        Recipe recipe1 = mockRecipeWithAllAttributes();
        Recipe recipe2 = mockRecipeWithoutComment();
        Recipe recipe3 = mockRecipeWithoutImageUri();

        recipeController.addRecipe(recipe1);
        recipeController.addRecipe(recipe2);
        recipeController.addRecipe(recipe3);

        Recipe gotRecipe = recipeController.getRecipeAt(2);
        assertEquals(gotRecipe,recipe3);

        recipeController.deleteRecipe(recipe1);
        gotRecipe = recipeController.getRecipeAt(1);
        assertEquals(gotRecipe,recipe3);

        gotRecipe = recipeController.getRecipeAt(3);
        assertEquals(null,gotRecipe);
    }

    @Test
    @DisplayName("This function is for getRecipeIndex")
    void getRecipeIndexTest() {
        Recipe PrevRecipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(PrevRecipe);
        assertEquals(0,recipeController.getRecipeIndex(PrevRecipe));

        Recipe CurrentRecipe = mockRecipeWithoutComment();
        recipeController.addRecipe(CurrentRecipe);
        assertEquals(1,recipeController.getRecipeIndex(CurrentRecipe));

        recipeController.deleteRecipe(PrevRecipe);
        assertEquals(0,recipeController.getRecipeIndex(CurrentRecipe));
        assertEquals(-1,recipeController.getRecipeIndex(PrevRecipe));
    }

    @Test
    @DisplayName("This function is for clearAllRecipes")
    void clearAllRecipesTest() {
        Recipe recipe1 = mockRecipeWithAllAttributes();
        Recipe recipe2 = mockRecipeWithoutComment();
        Recipe recipe3 = mockRecipeWithoutImageUri();

        recipeController.addRecipe(recipe1);
        recipeController.addRecipe(recipe2);
        recipeController.addRecipe(recipe3);
        assertEquals(3,recipeController.countRecipes());

        recipeController.clearAllRecipes();
        assertEquals(0,recipeController.countRecipes());
    }

    @Test
    @DisplayName("This function is for sortRecipe")
    void sortRecipeTest() {
        Recipe recipe1 = mockRecipeWithAllAttributes();
        Recipe recipe2 = mockRecipeWithoutComment();
        Recipe recipe3 = mockRecipeWithoutImageUri();

        recipeController.addRecipe(recipe1);
        recipeController.addRecipe(recipe2);
        recipeController.addRecipe(recipe3);

        recipeController.sortRecipe(0);
        assertEquals(recipeController.getRecipeAt(0).getTitle(),"Cookie");
        assertEquals(recipeController.getRecipeAt(1).getTitle(),"Egg Stew");
        assertEquals(recipeController.getRecipeAt(2).getTitle(),"Smashed Potato");

        recipeController.sortRecipe(1);
        assertEquals(recipeController.getRecipeAt(0).getPreparationTime(),12);
        assertEquals(recipeController.getRecipeAt(1).getPreparationTime(),15);
        assertEquals(recipeController.getRecipeAt(2).getPreparationTime(),30);

        recipeController.sortRecipe(2);
        assertEquals(recipeController.getRecipeAt(0).getNumberOfServings(),1);
        assertEquals(recipeController.getRecipeAt(1).getNumberOfServings(),2);
        assertEquals(recipeController.getRecipeAt(2).getNumberOfServings(),3);

        recipeController.sortRecipe(3);
        assertEquals(recipeController.getRecipeAt(0).getRecipeCategory(),"main");
        assertEquals(recipeController.getRecipeAt(1).getRecipeCategory(),"snacks");
        assertEquals(recipeController.getRecipeAt(2).getRecipeCategory(),"stew");
    }


}
