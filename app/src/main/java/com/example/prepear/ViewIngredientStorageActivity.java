/**
* Class Name: ViewIngredientStorage
 * Version Information: Version 1.0
 * Date: Oct 25th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 */

package com.example.prepear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * This class is an Activity Class for displaying the all in-storage ingredients with its detailed info on a ListView
 */
public class ViewIngredientStorageActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener,
        AddEditIngredientFragment.OnFragmentInteractionListener {

    private ListView ingredientStorageList; // for displaying all added in-storage ingredients
    private ArrayAdapter<IngredientInStorage> ingredientStorageListAdapter;
    private ArrayList<IngredientInStorage> ingredientStorageDataList = new ArrayList<>(); // store in-storage ingredient entries
    // On below: a String array containing all sort by choices used on sort-by Spinner for user selection
    private String[] userSortChoices = {"                 ---- Select  ---- ",
            "description(ascending)","description(descending)",
            "best before (oldest to newest)", "best before (newest to oldest)",
            "location(ascending)","category(ascending)",
            "location(descending)", "category(descending)"};
    private String userSelectedSortChoice; // Store the current sort-by selection made by user
    private final String IN_STORAGE_INGREDIENTS_COLLECTION_NAME = "Ingredient Storage";
    private FirebaseFirestore dbForInStorageIngredients = FirebaseFirestore.getInstance();
    private CollectionReference inStorageIngredientCollection = dbForInStorageIngredients.collection("Ingredient Storage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ingredient_storage);

        // On below: Grab the ListView object for use
        ingredientStorageList = findViewById(R.id.ingredients_in_storage_listview);
        // On below: initialize the used-defined ArrayAdapter for use
        ingredientStorageListAdapter = new IngredientStorageCustomList(this,
                ingredientStorageDataList);
        // On below: build a connection between the in-storage ingredients data list and the ArrayAdapter
        ingredientStorageList.setAdapter(ingredientStorageListAdapter);

        // On below: grab the ingredient addition button for use
        final FloatingActionButton addInStorageIngredientButton = findViewById(R.id.add_ingredient_button);
        addInStorageIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below the addition Fragment for new in-storage ingredient
                new AddEditIngredientFragment().show(getSupportFragmentManager(), "Add Ingredient");
            }
        });

        ingredientStorageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Grab the clicked item out of the ListView
                Object clickedItem = ingredientStorageList.getItemAtPosition(position);
                // Casting this clicked item to FoodEntry type from Object type
                IngredientInStorage clickedFood = (IngredientInStorage) clickedItem;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Use it as newInstance argument to create its associated AddEditIngredientFragment object
                // On below: necessarily required to swap into a correct Fragment
                AddEditIngredientFragment ingredientFragment = AddEditIngredientFragment.newInstance(clickedFood);
                ingredientFragment.show(transaction, "Edit Ingredient");
            }
        });

        // On below: Sort-by Spinner Initialization
        final Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_spinner); // grab the Sort-by Spinner
        sortBySpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        // on below: initialize the ArrayAdapter
        ArrayAdapter adapterForSpinner = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                userSortChoices);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // On below: set the ArrayAdapter up with the Spinner
        sortBySpinner.setAdapter(adapterForSpinner);
        // On below: Continually updating
        inStorageIngredientCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ingredientStorageDataList.clear();
                        if (task.isSuccessful()) {
                            // On below part: continually updating and retrieve data from Ingredient Storage Collection to local after a update in database
                            // On below line: use for-loop traverse through every document(stores every in-storage ingredient detailed information)
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("description")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("bestBeforeDate")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("location")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("category")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("amount")));

                                    // On below part: grab the stored values inside each document with its corresponding key
                                    String documentID = document.getId();
                                    String description =  (String) document.getData().get("description");
                                    String bestBeforeDate = (String) document.getData().get("bestBeforeDate");
                                    String location = (String) document.getData().get("location");
                                    String unit = (String) document.getData().get("unit");
                                    String amount = String.valueOf(document.getData().get("amount"));
                                    String category = (String) document.getData().get("category");

                                    // On below line: use retrieved data from document and build a new in-storage ingredient entry,
                                    // then add locally into the Ingredient Storage Data List
                                    ingredientStorageDataList.add(new IngredientInStorage(description, category,
                                            bestBeforeDate, location, amount, unit, documentID));
                                    // Notifying the adapter to render any new data fetched from the cloud
                                    ingredientStorageListAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("This document", "onComplete: DNE! ");
                                }

                            }
                        }
                    }
                });

        // On below part: After retrieving all existing in-storage ingredients' data from DB to in-storage ingredient list,
        // sort all retrieved ingredients based on user's picked sort-by choice
        SortInStorageIngredients(userSelectedSortChoice);
        ingredientStorageListAdapter.notifyDataSetChanged(); // for updating data in the ArrayAdapter
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
        userSelectedSortChoice = userSortChoices[position]; // use position index to locate the sort-by selection the user made
        SortInStorageIngredients(userSelectedSortChoice); // based on the selection user just made, sort throughout
        ingredientStorageListAdapter.notifyDataSetChanged(); // notify for updating data after sorting completely
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
        // Auto
    }


    /**
     * This method sorts all in-storage ingredients according to the user's selection made for sorting
     * @param userSelectedSortChoice a String as the user's selection for sorting all in-storage ingredients
     */
    public void SortInStorageIngredients(String userSelectedSortChoice){
        if (Objects.equals(userSelectedSortChoice, "description(ascending)")) { // sort by ingredient's description A->Z alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBriefDescription().compareTo(ingredient2.getBriefDescription());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "description(descending)")) { // sort by ingredient's description Z->A alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBriefDescription().compareTo(ingredient1.getBriefDescription());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "best before (oldest to newest)")) { // sort by ingredient's best before date from the most recently expired to the most newest expired
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBestBeforeDate().compareTo(ingredient2.getBestBeforeDate());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "best before (newest to oldest)")) { // sort by ingredient's best before date from the most newest expired to the most recently expired
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBestBeforeDate().compareTo(ingredient1.getBestBeforeDate());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "location(ascending)")) { // sort by ingredient's location A->Z alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getLocation().compareTo(ingredient2.getLocation());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "location(descending)")) { // sort by ingredient's location Z->A alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getLocation().compareTo(ingredient1.getLocation());
                }
            });
        }
        else if (Objects.equals(userSelectedSortChoice, "category(ascending)")) { // sort by ingredient's category A->Z alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getIngredientCategory().compareTo(ingredient2.getIngredientCategory());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "category(descending)")) { // sort by ingredient's category Z->A alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getIngredientCategory().compareTo(ingredient1.getIngredientCategory());
                }
            });
        }
    }

    /**
     * This method will be called during the procedure of adding a new in-storage ingredient
     * in order to add the new in-storage ingredient entry into ArrayAdapter
     * @param newIngredientInStorage An IngredientInStorage object will be added in the storage
     */
    @Override
    public void onOkPressed (IngredientInStorage newIngredientInStorage) {
        ingredientStorageListAdapter.add(newIngredientInStorage);
        onEditPressed(newIngredientInStorage);
    }


    /**
     * This method will be called during the process of deleting an existing in-storage ingredient out of ArrayAdapter
     * @param ingredientInStorage An existing IngredientInStorage object in the storage
     */
    @Override
    public void onDeletePressed (IngredientInStorage ingredientInStorage) {
        ingredientStorageListAdapter.remove(ingredientInStorage);
        onEditPressed(ingredientInStorage);
    }

    /**
     * This method will be called after editing an existing in-storage ingredient's detailed information
     * @param ingredientInStorage An existing IngredientInStorage object in the storage
     */
    @Override
    public void onEditPressed (IngredientInStorage ingredientInStorage) {
        ingredientStorageListAdapter.notifyDataSetChanged(); // notify for updating data in the ingredient list
    }
}
