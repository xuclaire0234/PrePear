/*
* Class Name: ViewIngredientStorage
* Version Information: Version 1.0
* Date: Oct 25th, 2022
* Author: Shihao Liu
* Copyright Notice:
* */

package com.example.prepear;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**/
public class ViewIngredientStorage extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private ListView ingredientStorageList;
    private ArrayAdapter<IngredientInStorage> ingredientStorageListAdapter;
    private ArrayList<IngredientInStorage> ingredientStorageDataList = new ArrayList<>();
    private String[] userSortChoices = {" ","description(ascending)","description(descending)",
                                        "best before (oldest to newest)",
                                        "best before (newest to oldest)",
                                        "location(ascending by default)", "category"}; // used for Spinner
    private String userSelectedSortChoice;

    final String IN_STORAGE_INGREDIENTS_COLLECTION_NAME = "IngredientsInStorage";
    FirebaseFirestore dbForInStorageIngredients = FirebaseFirestore.getInstance();
    final CollectionReference collectionReferenceForInStorageIngredients =
            dbForInStorageIngredients.collection(IN_STORAGE_INGREDIENTS_COLLECTION_NAME);

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

        final Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_spinner);
        sortBySpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        ArrayAdapter adapterForSpinner = new ArrayAdapter(this,
                                                            android.R.layout.simple_spinner_item,
                                                            userSortChoices);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sortBySpinner.setAdapter(adapterForSpinner);

        collectionReferenceForInStorageIngredients.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                ingredientStorageDataList.clear(); // clear the previous data for storing new data

                for (QueryDocumentSnapshot doc: value){
                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(doc.getData().get("Description")));
                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(doc.getData().get("Best Before Date")));
                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(doc.getData().get("Category")));

                    String description = doc.getId();
                    String bestBeforeDate = (String) doc.getData().get("Best Before Date");
                    String location = (String) doc.getData().get("Location");
                    String unit = (String) doc.getData().get("Unit");
                    int amount = (int) doc.getData().get("Amount");
                    String category = (String) doc.getData().get("Category");
                    ingredientStorageDataList.add(new IngredientInStorage(description, bestBeforeDate, location, unit, amount, category));

                    SortInStorageIngredients(userSelectedSortChoice);
                    ingredientStorageList.notifyAll();
                }
            }
        });
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
        ingredientStorageListAdapter.notifyDataSetChanged();
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
        // the in-storage ingredient in default order iff userSelectedSortChoice == " "
        if (userSelectedSortChoice == " "){

        } else if  (userSelectedSortChoice == "description(ascending)") {
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBriefDescription().compareTo(ingredient2.getBriefDescription());
                }
            });
        } else if (userSelectedSortChoice == "description(descending)") {
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBriefDescription().compareTo(ingredient1.getBriefDescription());
                }
            });
        } else if (userSelectedSortChoice ==  "best before (oldest to newest)") {
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBestBeforeDate().compareTo(ingredient2.getBestBeforeDate());
                }
            });
        } else if (userSelectedSortChoice ==  "best before (newest to oldest)") {
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBestBeforeDate().compareTo(ingredient1.getBestBeforeDate());
                }
            });
        } else if (userSelectedSortChoice == "location(ascending by default)") {
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getIngredientLocation().compareTo(ingredient1.getIngredientLocation());
                }
            });
        } else if (userSelectedSortChoice == "category") {
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getIngredientCategory().compareTo(ingredient1.getIngredientCategory());
                }
            });
        }
    }
}



