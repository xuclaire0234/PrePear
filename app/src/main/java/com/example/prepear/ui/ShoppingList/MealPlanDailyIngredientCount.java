/**
 * Classname: MealPlanDailyIngredientCount
 * Version Information: 1.0.0
 * Date: 11/19/2022
 * Author: Yingyue Cao
 * Copyright Notice:
 */
package com.example.prepear.ui.ShoppingList;

import androidx.annotation.NonNull;

import com.example.prepear.IngredientInRecipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

/**
 * This Class creates set up the listeners to catch and calculate ingredients that needed by the
 * meal plan for a certain date. The ingredient is of type {@link IngredientInRecipe}, with details
 * brief description {@link String}, amount {@link Integer}, unit {@link String}, category of
 * ingredient {@link String} and ingredient's id in database {@link String}
 */
public class MealPlanDailyIngredientCount {

    /**
     * This variable is private and stores the list of ingredients which is of type {@link IngredientInRecipe}
     */
    private ArrayList<IngredientInRecipe> ingredients;

    /**
     * This variable is private and stores the date text which is of type {@link String}
     */
    private String date;

    /**
     * These variables are private and stores the ids collection for ingredient in recipe and
     * ingredient in storage
     */
    private ArrayList<String> recipeIdsCollection, ingredientIdsCollection;
    private ArrayList<Double> recipeScaleCollection, ingredientScaleCollection;
    private FirebaseFirestore db;
    private Double scale;

    public MealPlanDailyIngredientCount(String date){
        this.date = date;
        this.connectToDB();
        ingredients = new ArrayList<>();
        ingredientIdsCollection = new ArrayList<>();
        ingredientScaleCollection = new ArrayList<>();
        recipeIdsCollection = new ArrayList<>();
        recipeScaleCollection = new ArrayList<>();
        this.gainAllIngredientAtDate(date);
    }

    private void connectToDB() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void gainAllIngredientAtDate(String date) {
        ingredients = new ArrayList<>();
        ingredientIdsCollection = new ArrayList<>();
        ingredientScaleCollection = new ArrayList<>();
        recipeIdsCollection = new ArrayList<>();
        recipeScaleCollection = new ArrayList<>();
        db
                .collection("Daily Meal Plans")
                .document(date)
                .collection("Meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                        for (QueryDocumentSnapshot document : task1.getResult()) {
                            if (task1.isSuccessful()) {
                                String id = (String) document.getData().get("Document ID");
                                String type = (String) document.getData().get("Meal Type");
                                Number scaleOfItem = (Number) document.getData().get("Customized Scaling Number");
                                if (type.equals("Recipe")) {
                                    recipeIdsCollection.add(id);
                                    recipeScaleCollection.add(scaleOfItem.doubleValue());
                                } else if (type.equals("Ingredient")) {
                                    ingredientIdsCollection.add(id);
                                    ingredientScaleCollection.add(scaleOfItem.doubleValue());
                                }
                            }
                        }

                        for (int i = 0; i < ingredientIdsCollection.size(); i++) {
                            scale = ingredientScaleCollection.get(i);
                            db
                                    .collection("Ingredient Storage")
                                    .document(ingredientIdsCollection.get(i)).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                            DocumentSnapshot doc = task2.getResult();
                                            if (task2.isSuccessful()) {
                                                String briefDescription = (String) doc.getData().get("description");
                                                String unit = (String) doc.getData().get("unit");
                                                String ingredientCategory = (String) doc.getData().get("category");

                                                // might change it later
                                                ingredients.add(new IngredientInRecipe(briefDescription,String.valueOf(scale),unit,ingredientCategory));
                                            }
                                        }
                                    });
                        }

                        for (int i = 0; i < recipeIdsCollection.size(); i++) {
                            final Double scaleOfThisRecipe = recipeScaleCollection.get(i);
                            final String idOfThisRecipe = recipeIdsCollection.get(i);
                            db
                                    .collection("Recipes")
                                    .document(recipeIdsCollection.get(i))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
                                            DocumentSnapshot doc2 = task3.getResult();
                                            Number numberOfServings = (Number) doc2.get("Number of Servings");
                                            scale = numberOfServings.doubleValue();

                                            scale = scaleOfThisRecipe/scale;

                                            db
                                                    .collection("Recipes")
                                                    .document(idOfThisRecipe)
                                                    .collection("Ingredient")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task4) {
                                                            for (QueryDocumentSnapshot ingredientDoc : task4.getResult()) {
                                                                if (task4.isSuccessful()) {
                                                                    String briefDescription = (String) ingredientDoc.getData().get("Brief Description");
                                                                    Number amount = (Number) ingredientDoc.getData().get("Amount");
                                                                    String unit = (String) ingredientDoc.getData().get("Unit");
                                                                    String ingredientCategory = (String) ingredientDoc.getData().get("Ingredient Category");

                                                                    Double amountValue = amount.doubleValue();
                                                                    amountValue = amountValue * scale;
                                                                    amountValue = Math.round(amountValue * 1000d) / 1000d;
                                                                    ingredients.add(new IngredientInRecipe(briefDescription,String.valueOf(amountValue),unit,ingredientCategory));
                                                                }
                                                            }
                                                        }
                                                    });




                                        }
                                    });

                        }


                    }
                });
    }

    public ArrayList<IngredientInRecipe> getIngredients() {
        return ingredients;
    }

    public String getDate() {
        return date;
    }
}
