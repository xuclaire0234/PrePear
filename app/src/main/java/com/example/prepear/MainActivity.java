/**
 * Class Name: MainActivity
 * Version Information: Version 1.0
 * Create Date: Oct 25th, 2022
 * Last Edit Date: Nov 3rd, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 */

package com.example.prepear;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Initialize the opening page of the app
 * Prompts the user to select a section of app by clicking the corresponding button for each section,
 * and direct the user to the user-selected section
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button ingredientStorageButton = findViewById(R.id.ingredient_storage_button);
        directToViewIngredientStorage(ingredientStorageButton);

        final Button recipeListButton = findViewById(R.id.recipe_folder_button);
        directToViewRecipeFolder(recipeListButton);
    }


    /**
     * Direct to ViewIngredientStorage Activity after clicking the "INGREDIENT STORAGE" button
     * @param clickedButton clickedButton an button for the user to click,
     * and be directed to the corresponding activity
     */
    public void directToViewIngredientStorage(Button clickedButton) {
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForViewIngredientStorageActivity = new Intent(MainActivity.this,
                        ViewIngredientStorageActivity.class);
                startActivity(intentForViewIngredientStorageActivity);
            }
        });
    }

    /**
     * Direct to ViewRecipeList Activity after clicking the "RECIPE FOLDER" button
     * @param clickedButton clickedButton an button for the user to click,
     * and be directed to the corresponding activity
     */
    public void directToViewRecipeFolder(Button clickedButton){
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForViewRecipeListActivity = new Intent(MainActivity.this,
                        ViewRecipeListActivity.class);
                startActivity(intentForViewRecipeListActivity);
            }
        });
    }

}