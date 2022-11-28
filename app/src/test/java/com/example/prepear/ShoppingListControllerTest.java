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

import com.example.prepear.ui.ShoppingList.ShoppingListController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ShoppingListControllerTest {
    // define the mock controller to be tested
    private ShoppingListController shoppingListController;

    /**
     * Before each test, initialize the controller
     */
    @BeforeEach
    private void mockShoppingList() {
        shoppingListController = new ShoppingListController();
    }

    /**
     * This function create a mock ingredients to be test
     * @return the return is of type {@link IngredientInRecipe}
     */
    private IngredientInRecipe mockIngredient1() {
        return new IngredientInRecipe("ingredient1","10.0","kg","milk");
    }

    /**
     * This function create a mock ingredients to be test
     * @return the return is of type {@link IngredientInRecipe}
     */
    private IngredientInRecipe mockIngredient2() {
        return new IngredientInRecipe("ingredient2","10.0","kg","egg");
    }

    /**
     * Testing the add function of the controller
     */
    @Test
    @DisplayName("This function is for testing add")
    void addIngredientTest() {
        // before added anything, the controller is expected to be empty
        assertEquals(0, shoppingListController.getIngredients().size());

        // add the first ingredient into the controller
        IngredientInRecipe ingredient1 = mockIngredient1();
        shoppingListController.add(ingredient1);
        // check if the controller has that ingredient
        assertEquals(1, shoppingListController.getIngredients().size());

        // add the second ingredient into the controller
        IngredientInRecipe ingredient2 = mockIngredient1();
        shoppingListController.add(ingredient2);
        // check if the controller has that ingredient
        assertEquals(2, shoppingListController.getIngredients().size());

        // add the repeated ingredient into the controller
        shoppingListController.add(ingredient2);
        // the controller should not contained the repeated ingredient
        assertEquals(2, shoppingListController.getIngredients().size());
    }

    /**
     * This function is to test the clear function for the controller
     */
    @Test
    @DisplayName("This function is for testing clear")
    void clearTest() {
        // create two mock ingredients to be added to the controller
        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();
        // before added anything, the controller is expected to be empty
        assertEquals(0, shoppingListController.getIngredients().size());

        // clear before add anything. Since nothing is added, the controller is expected to be empty
        shoppingListController.clear();
        assertEquals(0, shoppingListController.getIngredients().size());

        // add the two ingredients into the controller
        shoppingListController.add(ingredient1);
        shoppingListController.add(ingredient2);
        // Since two ingredients are added, the controller is expected to have two elements
        assertEquals(2, shoppingListController.getIngredients().size());

        // clear the controller
        shoppingListController.clear();
        // since the controller has been clear, no ingredients should be left in the controller
        assertEquals(0, shoppingListController.getIngredients().size());
        assertFalse(shoppingListController.getIngredients().contains(ingredient1));
        assertFalse(shoppingListController.getIngredients().contains(ingredient2));
    }

    /**
     * This function test the count function in controller
     */
    @Test
    @DisplayName("This function is for testing count")
    void countTest() {
        // test if the result of count function is correct before added anything
        assertEquals(shoppingListController.countIngredients(), shoppingListController.getIngredients().size());

        // add two ingredients into the controller
        IngredientInRecipe ingredient = mockIngredient1();
        shoppingListController.add(ingredient);
        ingredient = mockIngredient2();
        shoppingListController.add(ingredient);

        // test if the result of count function is correct after added two ingredients
        assertEquals(shoppingListController.countIngredients(), shoppingListController.getIngredients().size());

        // test if the result of count function is correct after clear countroller
        shoppingListController.clear();
        assertEquals(shoppingListController.countIngredients(), shoppingListController.getIngredients().size());
    }

    /**
     * This function tests the get function of the controller
     */
    @Test
    @DisplayName("This function is for testing get")
    void getTest() {
        //  test the get function before adding anythings
        ArrayList<IngredientInRecipe> ingredientList;
        ingredientList = shoppingListController.getIngredients();
        assertEquals(0,ingredientList.size());

        // test the get function when one ingredients is added
        IngredientInRecipe ingredient = mockIngredient1();
        shoppingListController.add(ingredient);
        ingredientList = shoppingListController.getIngredients();
        assertTrue(ingredientList.contains(ingredient));

        // test the get function when another one ingredients is added
        ingredient = mockIngredient2();
        shoppingListController.add(ingredient);
        ingredientList = shoppingListController.getIngredients();
        assertTrue(ingredientList.contains(ingredient));

        // test the get function when another one ingredients after clear
        shoppingListController.clear();
        ingredientList = shoppingListController.getIngredients();
        assertEquals(0,ingredientList.size());
        assertFalse(ingredientList.contains(ingredient));
    }

    /**
     * This function test the function that get the ingredients by index
     */
    @Test
    @DisplayName("This function is for testing get ingredient by index")
    void getIndexTest() {
        // add one ingredients to the controller and test if it is added successfully
        IngredientInRecipe ingredient = mockIngredient1();
        shoppingListController.add(ingredient);
        assertEquals(1,shoppingListController.countIngredients());

        // check if the function get the ingredient at index 0 correctly
        assertTrue(ingredient.equals(shoppingListController.getIngredientAt(0)));

        // add another one ingredients to the controller
        ingredient = mockIngredient2();
        shoppingListController.add(ingredient);

        // check if the function get the ingredient at index 1 correctly
        assertTrue(ingredient.equals(shoppingListController.getIngredientAt(1)));

        // check if the function could get ingredient out of range
        assertEquals(shoppingListController.getIngredientAt(12),null);

        // clear all the ingredients from controller
        shoppingListController.clear();

        // check if the function could get ingredient out of range
        assertFalse(ingredient.equals(shoppingListController.getIngredientAt(0)));
        assertEquals(shoppingListController.getIngredientAt(12),null);
    }

    /**
     * This function test the 
     */
    @Test
    @DisplayName("This function is for testing test delete")
    void deleteTest() {
        assertEquals(0,shoppingListController.countIngredients());

        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();

        shoppingListController.add(ingredient1);
        assertEquals(1,shoppingListController.countIngredients());
        shoppingListController.deleteIngredient(ingredient2);
        assertEquals(1,shoppingListController.countIngredients());
        shoppingListController.deleteIngredient(ingredient1);
        assertEquals(0,shoppingListController.countIngredients());

        shoppingListController.add(ingredient1);
        shoppingListController.add(ingredient2);
        assertEquals(2,shoppingListController.countIngredients());

        assertTrue(shoppingListController.getIngredients().contains(ingredient1));
        shoppingListController.deleteIngredient(ingredient1);
        assertEquals(1,shoppingListController.countIngredients());
        assertFalse(shoppingListController.getIngredients().contains(ingredient1));


        shoppingListController.deleteIngredient(ingredient1);
        assertEquals(1,shoppingListController.countIngredients());

        assertTrue(shoppingListController.getIngredients().contains(ingredient2));
        shoppingListController.deleteIngredient(ingredient2);
        assertFalse(shoppingListController.getIngredients().contains(ingredient2));

        assertEquals(0,shoppingListController.countIngredients());
        shoppingListController.deleteIngredient(ingredient2);
        assertEquals(0,shoppingListController.countIngredients());
    }

    @Test
    @DisplayName("This function is for testing test sort")
    void TestSort() {
        assertEquals(0,shoppingListController.countIngredients());
        shoppingListController.sortIngredient(0);

        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();

        shoppingListController.add(ingredient1);
        shoppingListController.add(ingredient2);

        shoppingListController.sortIngredient(1);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);


        shoppingListController.sortIngredient(0);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);

        shoppingListController.sortIngredient(3);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);
    }

    @Test
    @DisplayName("This function is for testing test reverse")
    void testReverse() {
        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();

        shoppingListController.add(ingredient1);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);


        shoppingListController.add(ingredient2);

        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);

        shoppingListController.sortIngredient(0);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);
        shoppingListController.reverseOrder();

        shoppingListController.sortIngredient(1);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);

    }

}
