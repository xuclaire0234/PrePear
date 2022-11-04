/**
 * Class Name: IngredientStorageDatabaseTest
 * Version Information: Version 1.0
 * Create Date: Nov 3rd, 2022
 * Authors: Shihao Liu
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
* BackEnd Testing for "Ingredient Storage Collection"
* */
public class IngredientStorageDatabaseTest {
    private FirebaseFirestore mockDB; // for connecting
    private CollectionReference mockIngredientStorageCollection;
    private IngredientInStorage mockIngredient;
    private final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    public void setMockDB(){
        mockDB = FirebaseFirestore.getInstance();
        setMockIngredientStorageCollection();
    }

    public void setMockIngredientStorageCollection(){
        mockIngredientStorageCollection = mockDB.collection("Ingredient Storage");
    }

    @Test
    @DisplayName("On below method is for testing adding a new ingredient into the storage")
    /* Create a Map object for the mock ingredient's data stored together as key-value pairs ,
     * and use this Map object to test for adding a new ingredient in Ingredient Storage Collection
     * */
    void testAddIngredientInStorage(){
        String mockIngredientDocumentId = DATEFORMAT.format(new Date()); // get the current date&time as the mock ingredient's document id
        mockIngredient = new IngredientInStorage("Orange", "Fruits", "2022-11-10",
                "Fridge","1.234556789","quantities",mockIngredientDocumentId);
        Map<String, Object> mockIngredientData = new HashMap<>(); // initialize the Map object for mock ingredient's data-storage
        // On below: store every key-value pairs into the initialized Map object
        mockIngredientData.put("document id", mockIngredientDocumentId);
        mockIngredientData.put("description", "Orange");
        mockIngredientData.put("bestBeforeDate", "2022-11-10");
        mockIngredientData.put("location", "Fridge");
        mockIngredientData.put("category", "Fruits");
        mockIngredientData.put("amount", "1.234556789");
        mockIngredientData.put("unit", "quantities");

        // On below: attempt to add the mock ingredient's Map object with its key-value paired data
        mockIngredientStorageCollection
                .document(mockIngredientDocumentId)
                .set(mockIngredientData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(mockIngredient.getBriefDescription(),
                                "The mock ingredient object has been successfully added into" +
                                        "Ingredient Storage Collection.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(mockIngredient.getBriefDescription(),
                                "The mock ingredient's data can't be added!" + e);
                    }
                });

        DocumentReference mockDocRef = mockIngredientStorageCollection
                .document(mockIngredient.getDocumentId());
        mockDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(mockIngredient.getBriefDescription(),
                                "DocumentSnapshot data: " + document.getData());
                        assertSame(mockIngredientData, document.getData());
                    } else {
                        Log.d(mockIngredient.getBriefDescription(), "No such document");
                    }
                } else {
                    Log.d(mockIngredient.getBriefDescription(), "get failed with ", task.getException());
                }
            }
        });

    }

    /**/



}
