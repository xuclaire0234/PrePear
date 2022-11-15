/**
 * Classname: DatabaseControllerTest
 * Version Information: 1.0.0
 * Date: 11/3/2022
 * Author: Yingyue Cao
 * Copyright Notice:
 */
package com.example.prepear;

import android.content.Context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for Database controller
 */
public class DatabaseControllerTest {
    private DatabaseController databaseController;
    private Context context;

    /**
     * before each run, initialize an database controller
     */
    @BeforeEach
    private void mockRecipeController() {
        databaseController = new DatabaseController();
    }

    /**
     * This function return a mock recipe object
     * @return the return is of type {@link Recipe}
     */
    private Recipe mockRecipe() {
        return new Recipe(null, "Hot Pot", 20, 1, "Hot Pot", "None");
    }

    /**
     * This function return a mock ingredient in recipe object
     * @return the return is of type {@link IngredientInRecipe}
     */
    private IngredientInRecipe mockIngredientInRecipe() {
        return new IngredientInRecipe("Beef","350.2","g","Meat");
    }

    /**
     * This function return a mock ingredient in storage object
     * @return the return is of type {@link IngredientInStorage}
     */
    private IngredientInStorage mockIngredientInStorage() {
        return new IngredientInStorage("Egg","egg","2022-11-24","box","3","quantities","2022-11-04 02:26:44");
    }

    /**
     * Testing add ingredient in storage to database
     */
    @Test
    @DisplayName("This function is for addIngredientToIngredientStorage")
    void addIngredientToIngredientStorageTest() {
        /* add a new ingredient to the database */
        IngredientInStorage newIngredient = mockIngredientInStorage();
        databaseController.addIngredientToIngredientStorage(this.context,newIngredient);
    }

    /**
     * Testing editIngredientInIngredientStorage
     */
    @Test
    @DisplayName("This function is for editIngredientInIngredientStorage")
    void editIngredientInIngredientStorageTest() {
        /* add a new ingredient to the database */
        IngredientInStorage newIngredient = mockIngredientInStorage();
        databaseController.addIngredientToIngredientStorage(this.context,newIngredient);

        /* edit this ingredient */
        newIngredient.setIngredientCategory("other");
        databaseController.editIngredientInIngredientStorage(this.context,newIngredient);
    }


}
