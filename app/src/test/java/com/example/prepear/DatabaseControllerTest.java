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
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseControllerTest {
    private DatabaseController databaseController;
    private Context context;

    @BeforeEach
    private void mockRecipeController() {
        databaseController = new DatabaseController();
    }

    private Recipe mockRecipe() {
        return new Recipe(null, "Hot Pot", 20, 1, "Hot Pot", "None");
    }

    private IngredientInRecipe mockIngredientInRecipe() {
        return new IngredientInRecipe("Beef","350.2","g","Meat");
    }

    private IngredientInStorage mockIngredientInStorage() {
        return new IngredientInStorage("Egg","egg","2022-11-24","box","3","quantities","2022-11-04 02:26:44");
    }

    @Test
    @DisplayName("This function is for addIngredientToIngredientStorage")
    void addIngredientToIngredientStorageTest() {
        databaseController.addIngredientToIngredientStorage(this.context,mockIngredientInStorage());

    }



}
