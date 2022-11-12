/**
 * Classname: IngredientControllerTest
 * Version Information: 1.0.0
 * Date: 11/10/2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class IngredientControllerTest {
    private IngredientController ingredientController;

    /**
     * Before each run, initialize an IngredientController
     */
    @BeforeEach
    private void mockIngredientController() {
        ingredientController = new IngredientController();
    }

    /**
     * This function returns a mockIngredient
     * @return the return is of type {@link IngredientInStorage}
     */
    private IngredientInStorage mockIngredient() {
        return new IngredientInStorage("mockIngredient","Fruits","2022-11-12",
                "Fridge","3","Kg", null,0);
    }

    /**
     * This function returns a different mock ingredient
     * @return the return is of type {@link IngredientInStorage}
     */
    private IngredientInStorage differentMockIngredient() {
        return new IngredientInStorage("mockIngredient2","Fruits","2022-11-12",
                "Fridge","3","Kg", null,0);
    }

    /**
     * Testing add ingredient to ingredient controller
     */
    @Test
    @DisplayName("This function is for testing add")
    void addIngredientTest() {
        /* check if the controller is empty now */
        assertEquals(0, ingredientController.getIngredients().size());

        /* check add ingredient to controller */
        IngredientInStorage newIngredient = mockIngredient();
        ingredientController.addIngredient(newIngredient);
        assertEquals(1,ingredientController.getIngredients().size()); // check the length of controller
        assertTrue(ingredientController.getIngredients().contains(newIngredient)); // check if the controller contains
    }

    /**
     * Testing remove ingredient in ingredient controller
     */
    @Test
    @DisplayName("This function is for testing delete")
    void removeIngredientTest() {
        /* add an ingredient to the controller */

        IngredientInStorage newIngredient = mockIngredient();
        ingredientController.addIngredient(newIngredient);
        assertEquals(1,ingredientController.getIngredients().size()); // check the length of controller
        assertTrue(ingredientController.getIngredients().contains(newIngredient)); // check if the controller contains

        /* test to delete an ingredient that's not in the controller */
        IngredientInStorage anotherIngredient = differentMockIngredient();
        ingredientController.removeIngredient(anotherIngredient); // the controller should not changed in this case
        assertEquals(1,ingredientController.getIngredients().size()); // check the length of controller
        assertFalse(ingredientController.getIngredients().contains(anotherIngredient)); // controller should not contain

        /* test to delete an ingredient that's in the controller */
        ingredientController.removeIngredient(newIngredient);
        assertEquals(0,ingredientController.getIngredients().size()); // check the length of controller
        assertFalse(ingredientController.getIngredients().contains(newIngredient)); // check if the controller contains
    }

    /**
     * Testing replace ingredient controller
     */
    @Test
    @DisplayName("This function is for testing edit")
    void replaceIngredientTest() {
        /* add an ingredient to the controller */
        IngredientInStorage Ingredient = mockIngredient();
        ingredientController.addIngredient(Ingredient);
        assertEquals(1,ingredientController.getIngredients().size()); // check the length of controller
        assertTrue(ingredientController.getIngredients().contains(Ingredient)); // check if the controller contains

        /* test replace the given ingredient with a new ingredient*/
        IngredientInStorage newIngredient = differentMockIngredient();
        ingredientController.replaceIngredient(0, newIngredient);
        assertTrue(ingredientController.getIngredients().contains(newIngredient));
        assertFalse(ingredientController.getIngredients().contains(Ingredient));

    }

}
