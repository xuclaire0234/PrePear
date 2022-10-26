/*
 * Class Name: IngredientDB
 * Version: 1.0
 * Date: Oct 25th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 * */
package com.example.prepear;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

/**/
public class IngredientDB {
    private Collection<IngredientInStorage> ingredientInStorageDB;
    private CollectionReference ingredientsInStorageCollectionReference;

    /**/
    public IngredientDB(FirebaseFirestore db){
        ingredientsInStorageCollectionReference = db.collection("Ingredients In Storage");
    }


}
