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

    public FirebaseFirestore prePearDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button ingredientStorageButton = findViewById(R.id.ingredient_storage_button);
        directToViewIngredientStorage(ingredientStorageButton);
    }

    /**
     * @para clickedButton an button the user clicks and direct to the corresponding activity
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



}