/*
 * Class Name: MainActivity
 * Version Information: Version 1.0
 * Date: Oct 25th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 * */

package com.example.prepear;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

/**/
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
     * @para clickedButton an button for the user to click and be directed to the corresponding activity
     */
    public void directToViewIngredientStorage(Button clickedButton) {
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForViewIngredientStorageActivity = new Intent(MainActivity.this, ViewIngredientStorage.class);
                startActivity(intentForViewIngredientStorageActivity);
            }
        });
    }

    /**
     * @param clickedButton clickedButton an button for the user to click and be directed to the corresponding activity
     */
    public void directToViewRecipeFolder(Button clickedButton){
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForViewRecipeListActivity = new Intent(MainActivity.this, ViewRecipeListActivity.class);
                startActivity(intentForViewRecipeListActivity);
            }
        });
    }

}