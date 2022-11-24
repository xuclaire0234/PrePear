/**
 * Classname: ComputeShoppingList
 * Version Information: 1.0.0
 * Date: 11/19/2022
 * Author: Jingyi Xu
 * Copyright Notice:
 */
package com.example.prepear;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.prepear.ui.ShoppingList.MealPlanDailyIngredientCount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ComputeShoppingList {
    private ArrayList<IngredientInStorage> allIngredientsInStorage;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Ingredient Storage");

    /**
     * This constructor of ComputeShoppingList Class
     */
    public ComputeShoppingList() {
        this.allIngredientsInStorage = new ArrayList<>();
        this.loadIngredientsInStorage();

    }

    /**
     * This method helps shopping list to load all the ingredients in ingredientInStorage
     * in order to compute shopping list
     * @return ArrayList<IngredientInStorage> contains all the ingredients in ingredientInStorage
     */
    public ArrayList<IngredientInStorage> getAllIngredientsInStorage() {
        return allIngredientsInStorage;
    }

    /**
     * This method will be called when needs to calculate the shopping list and
     * the first thing is to get all the ingredients in the ingredient storage database
     * and, store those data in the arraylist -> allIngredientsInStorage
     */
    public void loadIngredientsInStorage( ){
        allIngredientsInStorage.clear();
        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // On below part: continually updating and retrieve data from Ingredient Storage Collection to local after a update in database
                            // On below line: use for-loop traverse through every document(stores every in-storage ingredient detailed information)
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    // On below part: grab the stored values inside each document with its corresponding key
                                    String documentID = document.getId();
                                    String description = (String) document.getData().get("description");
                                    String bestBeforeDate = (String) document.getData().get("bestBeforeDate");
                                    String location = (String) document.getData().get("location");
                                    String unit = (String) document.getData().get("unit");
                                    String amount = String.valueOf(document.getData().get("amount"));
                                    String category = (String) document.getData().get("category");
                                    int iconCode = ((Long) document.getData().get("icon code")).intValue();

                                    // On below line: use retrieved data from document and build a new in-storage ingredient entry,
                                    // then add locally into the Ingredient Storage Data List
                                    allIngredientsInStorage.add(new IngredientInStorage(description, category,
                                            bestBeforeDate, location, amount, unit, documentID, iconCode));
                                } else {
                                    Log.d("This document", "onComplete: DNE! ");
                                }
                            }

                        }
                    }
                });
    }
}

