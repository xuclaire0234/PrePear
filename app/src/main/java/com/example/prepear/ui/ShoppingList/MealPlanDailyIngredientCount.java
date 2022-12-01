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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
     * ingredient in storage. They were of type {@link ArrayList}
     */
    private ArrayList<String> recipeIdsCollection, ingredientIdsCollection;

    /**
     * These variables are private and stores the scale of the ingredient in recipe and ingredient in
     * storage. They were of type {@link ArrayList}
     */
    private ArrayList<Double> recipeScaleCollection, ingredientScaleCollection;

    /**
     * This variable is private and stores the database information, which is of type {@link FirebaseFirestore}
     */
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String userUID;

    /**
     * This variable is private and stores the scale calculation result, which is of type {@link Double}
     */
    private Double scale;

    /**
     * This constructor creates an {@link MealPlanDailyIngredientCount} object with the given attributes
     * @param date a string for the date when meal plan is picked
     */
    public MealPlanDailyIngredientCount(String date){
        this.date = date; // assign value to the attributes date
        this.connectToDB(); // connect to the database
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.user = firebaseAuth.getCurrentUser();
        this.userUID = user.getUid();
        /*
         initialize all the arraylist attributes
         */
        ingredients = new ArrayList<>();
        ingredientIdsCollection = new ArrayList<>();
        ingredientScaleCollection = new ArrayList<>();
        recipeIdsCollection = new ArrayList<>();
        recipeScaleCollection = new ArrayList<>();

        this.gainAllIngredientAtDate(date); // gain data from the database
    }

    /**
     * This function connect to the database
     */
    private void connectToDB() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * This function gain data of all ingredients needed in a particular day from database
     * @param date is the date of meal plan we want to get all ingredients from, which is of type
     *            {@link String}
     */
    public void gainAllIngredientAtDate(String date) {
        /*
        initialize all the arraylist attributes
         */
        ingredients = new ArrayList<>();
        ingredientIdsCollection = new ArrayList<>();
        ingredientScaleCollection = new ArrayList<>();
        recipeIdsCollection = new ArrayList<>();
        recipeScaleCollection = new ArrayList<>();

        /*
         get data from the database
         */
        db
                .collection("Users")
                .document(userUID)
                .collection("Daily Meal Plans")
                .document(date)
                .collection("Meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                        // get all meals from the current date meal plan
                        for (QueryDocumentSnapshot document : task1.getResult()) {
                            if (task1.isSuccessful()) {
                                String id = (String) document.getData().get("Document ID"); // get the meal id
                                String type = (String) document.getData().get("Meal Type"); // get the meal type
                                Number scaleOfItem = (Number) document.getData().get("Customized Scaling Number"); // get the scaling
                                if (type.equals("Recipe")) {
                                    // if the meal is a recipe, add its id and scale to the recipe collection
                                    recipeIdsCollection.add(id);
                                    recipeScaleCollection.add(scaleOfItem.doubleValue());
                                } else if (type.equals("IngredientInStorage")) {
                                    // if the meal is a ingredientInStorage, add its id and scale to the ingredient collection
                                    ingredientIdsCollection.add(id);
                                    ingredientScaleCollection.add(scaleOfItem.doubleValue());
                                }
                            }
                        }

                        // get all the ingredient type meal
                        for (int i = 0; i < ingredientIdsCollection.size(); i++) {
                            scale = ingredientScaleCollection.get(i);
                            db
                                    .collection("Users")
                                    .document(userUID)
                                    .collection("Ingredient Storage")
                                    .document(ingredientIdsCollection.get(i)).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                            DocumentSnapshot doc = task2.getResult();
                                            if (task2.isSuccessful()) {
                                                if (doc.getData() != null) {
                                                    // get the description, unit and category
                                                    String briefDescription = (String) doc.getData().get("description");
                                                    String unit = (String) doc.getData().get("unit");
                                                    String ingredientCategory = (String) doc.getData().get("category");
                                                    // store the ingredient got from database, changing the scale
                                                    ingredients.add(new IngredientInRecipe(briefDescription,String.valueOf(scale),unit,ingredientCategory));
                                                }
                                            }
                                        }
                                    });
                        }

                        // get all the recipe type meal
                        for (int i = 0; i < recipeIdsCollection.size(); i++) {
                            final Double scaleOfThisRecipe = recipeScaleCollection.get(i);
                            final String idOfThisRecipe = recipeIdsCollection.get(i);
                            db
                                    .collection("Users")
                                    .document(userUID)
                                    .collection("Recipes")
                                    .document(recipeIdsCollection.get(i))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
                                            DocumentSnapshot doc2 = task3.getResult();
                                            if (doc2.getData() != null) {
                                                // get the original number of scaling first
                                                Number numberOfServings = (Number) doc2.get("Number of Servings");
                                                scale = numberOfServings.doubleValue();

                                                // divide to get the scaling number needed to apply to all the ingredients in recipe
                                                scale = scaleOfThisRecipe/scale;

                                                db
                                                        .collection("Users")
                                                        .document(userUID)
                                                        .collection("Recipes")
                                                        .document(idOfThisRecipe)
                                                        .collection("Ingredient")
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task4) {
                                                                // gain all the ingredient in recipe from the current recipe
                                                                for (QueryDocumentSnapshot ingredientDoc : task4.getResult()) {
                                                                    if (task4.isSuccessful()) {
                                                                        // get brief description, amount, unit and ingredient category information
                                                                        String briefDescription = (String) ingredientDoc.getData().get("Brief Description");
                                                                        Number amount = (Number) ingredientDoc.getData().get("Amount");
                                                                        String unit = (String) ingredientDoc.getData().get("Unit");
                                                                        String ingredientCategory = (String) ingredientDoc.getData().get("Ingredient Category");
                                                                        Double amountValue = amount.doubleValue();

                                                                        amountValue = amountValue * scale; // scale the amount value
                                                                        amountValue = Math.round(amountValue * 1000d) / 1000d; // round the amount value

                                                                        // store the ingredient got from database, with the scaled amount
                                                                        ingredients.add(new IngredientInRecipe(briefDescription,String.valueOf(amountValue),unit,ingredientCategory));
                                                                    }
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * This function gain all the ingredients got from the database
     * @return the return is of type {@link ArrayList}
     */
    public ArrayList<IngredientInRecipe> getIngredients() {
        return ingredients;
    }

    /**
     * This function gain the date from
     * @return
     */
    public String getDate() {
        return date;
    }
}
