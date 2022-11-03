package com.example.prepear;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* BackEnd Testing for "Ingredient Storage Collection"
* */
public class IngredientStorageDatabaseTest {
    private FirebaseFirestore mockDB;
    private CollectionReference mockIngredientStorageCollection;
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
    void testAddIngredient(){
        String mockIngredientDocumentId = DATEFORMAT.format(new Date());
        IngredientInStorage mockIngredient = new IngredientInStorage("Orange", "Fruits", "2022-11-10",
                "Firdge","1.234556789","quantities",mockIngredientDocumentId);

    }


}
