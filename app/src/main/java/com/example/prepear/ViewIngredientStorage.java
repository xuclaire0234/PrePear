/*
* Class Name: ViewIngredientStorage
* Version Information: Version 1.0
* Date: Oct 25th, 2022
* Author: Shihao Liu
* Copyright Notice:
* */

package com.example.prepear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Objects;

/**/
public class ViewIngredientStorage extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private ListView ingredientStorageList;
    IngredientStorageCustomList ingredientStorageListAdapter;
    //private ArrayAdapter<IngredientInStorage> ingredientStorageListAdapter;
    private ArrayList<IngredientInStorage> ingredientStorageDataList = new ArrayList<>();
    private String[] userSortChoices = {" --Select--","description(ascending)","description(descending)",
            "best before (oldest to newest)", "best before (newest to oldest)",
            "location(ascending by default)", "category"}; // used for Spinner
    ////
    int LAUNCH_ADD_ingredient_ACTIVITY = 1;
    int LAUNCH_VIEW_ingredient_ACTIVITY = 2;
    //////
    Integer IngredientPosition = -1;
    Integer sortItemIngredient = 0;
    final String TAG = "Ingredient";
    private String userSelectedSortChoice;
    private final FirebaseFirestore dbForInStorageIngredients = FirebaseFirestore.getInstance();
    final CollectionReference inStorageIngredientsCollection = dbForInStorageIngredients.collection("Ingredient Storage");

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
                //new AddEditIngredientFragment(inStorageIngredientsCollection).show(getSupportFragmentManager(), "Add Ingredient");
                IngredientPosition = -1;
                Intent intent = new Intent(ViewIngredientStorage.this, AddEditIngredientActivity.class);
                intent.putExtra("calling activity", "1");
                startActivityForResult(intent, LAUNCH_ADD_ingredient_ACTIVITY);
            }
        });


        ingredientStorageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IngredientPosition = position;
                IngredientInStorage viewedIngredient = ingredientStorageListAdapter.getItem(position);
                Intent intent = new Intent(ViewIngredientStorage.this, ViewIngredientActivity.class);
                intent.putExtra("viewed ingredient", viewedIngredient);
                startActivityForResult(intent, LAUNCH_VIEW_ingredient_ACTIVITY);
            }
        });


        // on below code part: Sort-by Spinner Initialization
        final Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_spinner);
        sortBySpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterForSpinner = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                userSortChoices);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sortBySpinner.setAdapter(adapterForSpinner);

//        inStorageIngredientsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                            FirebaseFirestoreException error) {
//                        // Clear the old list
//                        ingredientStorageDataList.clear();
//
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            Log.d(TAG, String.valueOf(doc.getData().get("bestBeforeDate")));
//                            String id = doc.getId();
//                            String description = (String) doc.getData().get("description"); //
//                            String bestBeforeDate = (String) doc.getData().get("bestBeforeDate"); //
//                            String location = (String) doc.getData().get("location"); //
//                            String unit = (String) doc.getData().get("unit"); //
//                                    /* amount might be null! */
//                            String amount = (String) doc.getData().get("amount"); //
//                            String category = (String) doc.getData().get("category"); //;
//                            IngredientInStorage newIngredient = new IngredientInStorage(description, bestBeforeDate, location, unit, amount, category);
//                            ingredientStorageDataList.add(newIngredient);
//                            newIngredient.setId(id);
//
//                            }
//                        ingredientStorageListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
//                    }
//                });

        inStorageIngredientsCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ingredientStorageDataList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Log.d(TAG, String.valueOf(doc.getData().get("bestBeforeDate")));
                            String id = doc.getId();
                            String description = (String) doc.getData().get("description"); //
                            String bestBeforeDate = (String) doc.getData().get("bestBeforeDate"); //
                            String location = (String) doc.getData().get("location"); //
                            String unit = (String) doc.getData().get("unit"); //
                                    /* amount might be null! */
                            String amount = String.valueOf(doc.getData().get("amount")); //
                            String category = (String) doc.getData().get("category"); //;
                            IngredientInStorage newIngredient = new IngredientInStorage(description, bestBeforeDate, location, unit, amount, category);
                            ingredientStorageDataList.add(newIngredient);
                            newIngredient.setId(id);
                        }
                        ingredientStorageListAdapter.notifyDataSetChanged();
                    }
                });
    }




//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        userSelectedSortChoice = userSortChoices[position];
//        SortInStorageIngredients(userSelectedSortChoice);
//        ingredientStorageListAdapter.notifyDataSetChanged();
//    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Auto
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0){
            sortItemIngredient = i - 1;
            ingredientStorageListAdapter.sortIngredient(sortItemIngredient);
            ingredientStorageListAdapter.notifyDataSetChanged();
            Toast.makeText(ViewIngredientStorage.this, userSortChoices[i],Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ingredientStorageListAdapter.notifyDataSetChanged();

        if (requestCode == LAUNCH_ADD_ingredient_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                IngredientInStorage ingredientForAdd = (IngredientInStorage) data.getSerializableExtra("new ingredient");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // No action
            }
        }

        if (requestCode == LAUNCH_VIEW_ingredient_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                IngredientInStorage IngredientForDelete = (IngredientInStorage) data.getSerializableExtra("delete Ingredient");
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}




