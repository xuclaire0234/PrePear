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

/**
 * Initialize the opening page of the app
 * Prompts the user to select a section of app by clicking the corresponding button for each section,
 * and direct the user to the user-selected section
*/
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On below: set the customized MainActivity's content up front after launching the app
        setContentView(R.layout.activity_login);
        // On below: grab the Button for (after clicking)launching Ingredient Storage Section of the app
        final Button ingredientStorageButton = findViewById(R.id.log_in);
        directToViewIngredientStorage(ingredientStorageButton);
        // On below: grab the Button for (after clicking)launching Recipe Folder Section of the app
        final Button recipeListButton = findViewById(R.id.create_account);
        directToViewRecipeFolder(recipeListButton);
    }

    /**
     * Direct to ViewIngredientStorage Activity after clicking the "INGREDIENT STORAGE" button on MainActivity
     * @param clickedButton clickedButton an button for the user to click,
     * and be directed to the corresponding activity
     */
    public void directToViewIngredientStorage(Button clickedButton) {
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On below: initialize a intent for launching the Ingredient Storage Activity
                Intent intentForViewIngredientStorageActivity = new Intent(LoginActivity.this,
                        HomeActivity.class);
                // On below: start launching
                startActivity(intentForViewIngredientStorageActivity);
            }
        });
    }

    /**
     * Direct to ViewRecipeList Activity after clicking the "RECIPE FOLDER" button on MainActivity
     * @param clickedButton clickedButton an button for the user to click,
     * and be directed to the corresponding activity
     */
    public void directToViewRecipeFolder(Button clickedButton){
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On below: initialize a intent for launching the Recipe Folder Activity
                Intent intentForViewRecipeListActivity = new Intent(LoginActivity.this,
                        HomeActivity.class);
                // On below: After clicking the button, start launching
                startActivity(intentForViewRecipeListActivity);
            }
        });
    }

}