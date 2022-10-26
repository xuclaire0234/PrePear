/*
* Class Name: ViewIngredientStorage
* Version Information: Version 1.0
* Date: Oct 25th, 2022
* Author: Shihao Liu
* Copyright Notice:
* */

package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**/
public class ViewIngredientStorage extends AppCompatActivity {

    private ListView ingredientStorageList;
    private ArrayAdapter<IngredientInStorage> ingredientStorageListAdapter;
    private ArrayList<IngredientInStorage> ingredientStorageDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ingredient_storage);

        // on below line grab the ListView object for use
        ingredientStorageList = findViewById(R.id.ingredients_in_storage_listview);
        // on below line initialize the used-defined ArrayAdapter for use
        ingredientStorageListAdapter = new IngredientStorageCustomList(this,
                                                                        ingredientStorageDataList);
        // on below line build a connection between the in-storage ingredients data list and the ArrayAdapter
        ingredientStorageList.setAdapter(ingredientStorageListAdapter);

        // on below line grab the ingredient addition button for use
        final FloatingActionButton addInStorageIngredientButton = findViewById(R.id.add_ingredient_button);
        addInStorageIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below the addition Fragment for new in-storage ingredient
            }
        });

        ingredientStorageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // grab the clicked item out of the ListView
                Object clickedItem = ingredientStorageList.getItemAtPosition(position);
                // casting this clicked item to FoodEntry type from Object type
                IngredientInStorage clickedFood = (IngredientInStorage) clickedItem;
                // use it as newInstance argument to create its associated AddEditIngredientFragment object
                // on below necessarily required to swap into a correct Fragment
                AddEditFoodFragment foodFragment = AddEditFoodFragment.newInstance(clickedFood);
                // use Fragment Transaction
                getSupportFragmentManager().beginTransaction()
                        // on below line fill with the correct Fragment object
                        // should be showing a Fragment for view/edit an existing in-storage ingredient
                        .add(foodFragment, null)
                        .commit();
            }
        });

    }



}