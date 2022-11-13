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

/**
 * Test class for Recipe class.
 */
public class RecipeControllerTest {

    private RecipeController recipeController;

    /**
     * Before each run, initialize a recipeController
     */
    @BeforeEach
    private void mockRecipeController() {
        recipeController = new RecipeController();
    }

    /**
     * This function returns a mockRecipe with full attributes
     * @return the return is of type {@link Recipe}
     */
    private Recipe mockRecipeWithAllAttributes() {
        return new Recipe("http://","Cookie",15,3,"snacks","None");
    }

    /**
     * This function returns a mockRecipe without attributes ImageUri
     * @return the return is of type {@link Recipe}
     */
    private Recipe mockRecipeWithoutImageUri() {
        return new Recipe(null,"Egg Stew",30,1,"stew",null);
    }

    /**
     * This function returns a mockRecipe without attributes comment
     * @return the return is of type {@link Recipe}
     */
    private Recipe mockRecipeWithoutComment() {
        return new Recipe("http://", "Smashed Potato",12,2,"main",null);
    }

    /**
     * Testing add recipe to recipe controller
     */
    @Test
    @DisplayName("This function is for testing add")
    void addRecipeTest() {
        /* check if the controller is empty now */
        assertEquals(0, recipeController.getRecipes().size());

        /* check add recipe with full attributes to controller */
        Recipe newRecipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(newRecipe);
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains
        // the newest added recipe

        /* check add recipe missing image uri to controller */
        newRecipe = mockRecipeWithoutComment();
        recipeController.addRecipe(newRecipe);
        assertEquals(2,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains
        // the newest added recipe

        /* check add recipe missing comment to controller */
        newRecipe = mockRecipeWithoutImageUri();
        recipeController.addRecipe(newRecipe);
        assertEquals(3,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains
        // the newest added recipe
    }

    /**
     * Testing delete recipe in recipe controller
     */
    @Test
    @DisplayName("This function is for testing delete")
    void deleteRecipeTest() {
        /* add a recipe to the controller */
        Recipe newRecipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(newRecipe);
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertTrue(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains
        // the newest added recipe

        /* test to delete a recipe that's not in the controller */
        Recipe anotherRecipe = mockRecipeWithoutComment();
        recipeController.deleteRecipe(anotherRecipe); // the controller should not changed in this case
        assertEquals(1,recipeController.getRecipes().size()); // check the length of controller
        assertFalse(recipeController.getRecipes().contains(anotherRecipe)); // controller should not contain
        // the recipe that is not in the controller

        /* test to delete a recipe that's in the controller */
        recipeController.deleteRecipe(newRecipe);
        assertEquals(0,recipeController.getRecipes().size()); // check the length of controller
        assertFalse(recipeController.getRecipes().contains(newRecipe)); // check if the controller contains
        // the deleted recipe
    }

    /**
     * Testing edit recipe controller
     */
    @Test
    @DisplayName("This function is for testing edit")
    void editRecipeTest() {
        /* add a recipe to the controller */
        Recipe recipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(recipe);
        assertTrue(recipeController.getRecipes().contains(recipe));


        /* test edit this recipe with recipe object given */
        Recipe newRecipe = mockRecipeWithoutImageUri();
        recipeController.editRecipe(recipe, newRecipe);
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));


        /* test edit this recipe with recipe index given */
        Integer index = recipeController.getRecipeIndex(newRecipe);
        newRecipe = mockRecipeWithoutComment();
        recipeController.editRecipe(index, newRecipe);
        assertTrue(recipeController.getRecipes().contains(newRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));

        /* test to edit an recipe which is not exist in the controller */
        /* the controller should not changed in this case */
        Recipe anotherRecipe = new Recipe(null,"Stewed Beef",35,3,"Stew","None");
        recipeController.editRecipe(recipe, anotherRecipe);
        assertFalse(recipeController.getRecipes().contains(anotherRecipe));
        assertFalse(recipeController.getRecipes().contains(recipe));
    }

    /**
     * Testing get count of recipe function of recipe controller
     */
    @Test
    @DisplayName("This function is for testing count")
    void countRecipeTest() {

        /* add a recipe to the recipe controller and check the count number */
        Recipe recipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(recipe);
        recipe = mockRecipeWithoutComment();
        assertEquals(1,recipeController.countRecipes());

        /* add a recipe to the recipe controller and check the count number */
        recipeController.addRecipe(recipe);
        assertEquals(2,recipeController.countRecipes());
    }

    /**
     * Testing the get recipe at certain index function of recipe controller
     */
    @Test
    @DisplayName("This function is for getRecipeAt")
    void getRecipeAtTest() {

        /* add a recipes to the recipe controller */
        Recipe recipe1 = mockRecipeWithAllAttributes();
        Recipe recipe2 = mockRecipeWithoutComment();
        Recipe recipe3 = mockRecipeWithoutImageUri();
        recipeController.addRecipe(recipe1);
        recipeController.addRecipe(recipe2);
        recipeController.addRecipe(recipe3);

        /* check if the recipe at one index to test if the recipes match with the sequence the recipes were added */
        Recipe gotRecipe = recipeController.getRecipeAt(2);
        assertEquals(gotRecipe,recipe3);

        /* delete one recipe and check if the sequence is changed */
        recipeController.deleteRecipe(recipe1);
        gotRecipe = recipeController.getRecipeAt(1);
        assertEquals(gotRecipe,recipe3);

        /* check to make sure we could not get item out of index */
        gotRecipe = recipeController.getRecipeAt(3);
        assertEquals(null,gotRecipe);
    }

    /**
     * Testing the get recipe index function of recipe controller
     */
    @Test
    @DisplayName("This function is for getRecipeIndex")
    void getRecipeIndexTest() {
        /* add a recipe to the controller */
        Recipe PrevRecipe = mockRecipeWithAllAttributes();
        recipeController.addRecipe(PrevRecipe);

        /* check if the index is correct */
        assertEquals(0,recipeController.getRecipeIndex(PrevRecipe));

        /* add another recipe to the controller */
        Recipe CurrentRecipe = mockRecipeWithoutComment();
        recipeController.addRecipe(CurrentRecipe);

        /* check if the index is correct */
        assertEquals(1,recipeController.getRecipeIndex(CurrentRecipe));

        /* delete one recipe from controller */
        recipeController.deleteRecipe(PrevRecipe);

        /* check if the index is changed for the other recipe */
        assertEquals(0,recipeController.getRecipeIndex(CurrentRecipe));

        /* check the index output for not existing recipe */
        assertEquals(-1,recipeController.getRecipeIndex(PrevRecipe));
    }

    /**
     * This test is to test the clear all recipe function in controller
     */
    @Test
    @DisplayName("This function is for clearAllRecipes")
    void clearAllRecipesTest() {
        /* add recipes to the controller */
        Recipe recipe1 = mockRecipeWithAllAttributes();
        Recipe recipe2 = mockRecipeWithoutComment();
        Recipe recipe3 = mockRecipeWithoutImageUri();
        recipeController.addRecipe(recipe1);
        recipeController.addRecipe(recipe2);
        recipeController.addRecipe(recipe3);

        /* check if the recipes all exist */
        assertEquals(3,recipeController.countRecipes());


        /* check if the recipes all exist after clear all recipes */
        recipeController.clearAllRecipes();
        assertEquals(0,recipeController.countRecipes());
    }

    /**
     * Testing the sort recipe function in recipe controller
     */
    @Test
    @DisplayName("This function is for sortRecipe")
    void sortRecipeTest() {
        /* add recipes with different title, preparation time, number of servings and category to controller */
        Recipe recipe1 = mockRecipeWithAllAttributes();
        Recipe recipe2 = mockRecipeWithoutComment();
        Recipe recipe3 = mockRecipeWithoutImageUri();
        recipeController.addRecipe(recipe1);
        recipeController.addRecipe(recipe2);
        recipeController.addRecipe(recipe3);

        /* test sort by title and check the sequence */
        recipeController.sortRecipe(0);
        assertEquals(recipeController.getRecipeAt(0).getTitle(),"Cookie");
        assertEquals(recipeController.getRecipeAt(1).getTitle(),"Egg Stew");
        assertEquals(recipeController.getRecipeAt(2).getTitle(),"Smashed Potato");

        /* test sort by preparation time and check the sequence */
        recipeController.sortRecipe(1);
        assertEquals(recipeController.getRecipeAt(0).getPreparationTime(),12);
        assertEquals(recipeController.getRecipeAt(1).getPreparationTime(),15);
        assertEquals(recipeController.getRecipeAt(2).getPreparationTime(),30);

        /* test sort by number of servings and check the sequence */
        recipeController.sortRecipe(2);
        assertEquals(recipeController.getRecipeAt(0).getNumberOfServings(),1);
        assertEquals(recipeController.getRecipeAt(1).getNumberOfServings(),2);
        assertEquals(recipeController.getRecipeAt(2).getNumberOfServings(),3);

        /* test sort by recipe category and check the sequence */
        recipeController.sortRecipe(3);
        assertEquals(recipeController.getRecipeAt(0).getRecipeCategory(),"main");
        assertEquals(recipeController.getRecipeAt(1).getRecipeCategory(),"snacks");
        assertEquals(recipeController.getRecipeAt(2).getRecipeCategory(),"stew");
    }


}