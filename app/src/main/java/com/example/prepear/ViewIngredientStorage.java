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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**/
public class ViewIngredientStorage extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private ListView ingredientStorageList;
    private ArrayAdapter<IngredientInStorage> ingredientStorageListAdapter;
    private ArrayList<IngredientInStorage> ingredientStorageDataList = new ArrayList<>();
    private String[] userSortChoices = {"description(ascending)","description(descending)",
                                        "best before (oldest to newest)", "best before (newest to oldest)",
                                        "location(ascending by default)", "category"}; // used for Spinner
    String userSelectedSortChoice;


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
//                // use it as newInstance argument to create its associated AddEditIngredientFragment object
//                // on below necessarily required to swap into a correct Fragment
//                AddEditFoodFragment foodFragment = AddEditFoodFragment.newInstance(clickedFood);
//                // use Fragment Transaction
//                getSupportFragmentManager().beginTransaction()
//                        // on below line fill with the correct Fragment object
//                        // should be showing a Fragment for view/edit an existing in-storage ingredient
//                        .add(foodFragment, null)
//                        .commit();
            }
        });

        final Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_spinner);
        sortBySpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        ArrayAdapter adapterForSpinner = new ArrayAdapter(this,
                                                            android.R.layout.simple_spinner_item,
                                                            userSortChoices);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sortBySpinner.setAdapter(adapterForSpinner);


    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userSelectedSortChoice = userSortChoices[position];
        SortInStorageIngredients(userSelectedSortChoice);
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void SortInStorageIngredients(String userSelectedSortChoice){

    }
}



