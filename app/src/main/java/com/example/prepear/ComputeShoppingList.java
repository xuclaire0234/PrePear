package com.example.prepear;
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
    private String startDate;
    private String endDate;
    //    private ArrayList<IngredientInStorage> allIngredientsInStorage = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Ingredient Storage");

    public ComputeShoppingList(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * This method will be called when needs to calculate the shopping list and
     * the first thing is to get all the ingredients in the ingredient storage database
     * and, store those data in the arraylist -> allIngredientsInStorage
     */
    public ArrayList<IngredientInStorage> loadIngredientsInStorage(){
        ArrayList<IngredientInStorage> allIngredientsInStorage = new ArrayList<>();
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
        return allIngredientsInStorage;
    }
    // String startDate, String endDate not sure needed to add as parameters
    /**
     * This method should compute the shopping list which is all the ingredients should be shown on the screen
     * and, user has to buy after comparing with ingredients in the storage.
     * @return ArrayList<IngredientInRecipe> an arraylist contains all the ingredients needed to buy
     */
    public ArrayList<IngredientInRecipe> calculateShoppingList() throws ParseException {
        /* initializing the arraylist for ingredients in shopping list */
        ArrayList<IngredientInRecipe> ingredientInShoppingList = new ArrayList<>();
        ArrayList<IngredientInStorage> allIngredientsInStorage = loadIngredientsInStorage(); // get all ingredients in storage

        /* convert String date to Date type for us to iterate and compare with ingredients in storage */
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);

        Calendar startDay = Calendar.getInstance();
        startDay.setTime(start);
        Calendar endDay = Calendar.getInstance();
        endDay.setTime(end);

        // !startDay.after(endDay) -> represents iterate inclusive the end date
        while( !startDay.after(endDay)){
            Date targetDay = startDay.getTime(); // current date
            String currentDate = formatter.format(targetDay); // convert date type to String
            /* since get the current date, we should get all needed ingredients for that date */
            MealPlanDailyIngredientCount dailyIngredientCount = new MealPlanDailyIngredientCount(currentDate);
            ArrayList<IngredientInRecipe> dailyIngredients = dailyIngredientCount.getIngredients();

            for (int i = 0; i < dailyIngredients.size(); i++) { // iterate ingredients for one date
                IngredientInRecipe targetIngredient = dailyIngredients.get(i);
                String targetDescription = targetIngredient.getBriefDescription();

                /* using targetIngredient's briefDescription to find if ingredientInStorage has a same ingredient*/
                for (int j = 0; j < allIngredientsInStorage.size(); j++) {
                    IngredientInStorage targetIngredientStorage = allIngredientsInStorage.get(j);
                    if(targetDescription.equals(targetIngredientStorage.getBriefDescription())){

                        /* since we can find the same ingredient in storage, now we need to compare date*/
                        Date bestBeforeDate = formatter.parse(targetIngredientStorage.getBestBeforeDate());
                        if(bestBeforeDate.compareTo(targetDay) > 0 || bestBeforeDate.compareTo(targetDay) == 0){
                            // bestBeforeDate occurs after targetDate
                            // now, need to compare the amount to check if we need to buy extra
                            double targetAmount = targetIngredient.getAmountValue();
                            double storageAmount = targetIngredientStorage.getAmountValue();
                            if(storageAmount > targetAmount){
                                // update storage amount since it is enough to use ingredient in storage
                                // do not need to prepare this ingredient
                                double difference = storageAmount - targetAmount;
                                targetIngredientStorage.setAmountValue(difference);
                            }else if(storageAmount == targetAmount){
                                // remove this ingredient in storage
                                // ingredient in storage can totally cover the ingredient user needs
                                allIngredientsInStorage.remove(targetIngredientStorage);
                            }else if(storageAmount < targetAmount) {
                                // remove this ingredient in storage
                                // update rest needed amount of ingredient to ingredientInShoppingList
                                // check first if there is an existing one in ingredientInShoppingList
                                // otherwise, just update amount for that ingredient
                                allIngredientsInStorage.remove(targetIngredientStorage);
                                double differenceCart = targetAmount - storageAmount;
                                String targetCategory = targetIngredient.getIngredientCategory();
                                String targetUnit = targetIngredient.getUnit();
                                boolean key = true;

                                while (key) {
                                    for (int k = 0; k < ingredientInShoppingList.size(); k++) {
                                        IngredientInRecipe ingredientForShopping = ingredientInShoppingList.get(k);
                                        if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
                                            // there is an existing ingredient in ingredientInShoppingList
                                            double exitingAmount = ingredientForShopping.getAmountValue();
                                            double updateAmount = exitingAmount + differenceCart;
                                            ingredientForShopping.setAmountValue(updateAmount);  // only update amount
                                            key = false;
                                        }
                                    }
                                    /* after iterating ingredientInShoppingList, if there is no existing one */
                                    ingredientInShoppingList.add(new IngredientInRecipe(targetDescription,String.valueOf(differenceCart),targetUnit,targetCategory));
                                    key = false;
                                }
                            }

                        }else if(bestBeforeDate.compareTo(targetDay) < 0){
                            // bestBeforeDate occurs before targetDate
                            // now, need to buy exact amount of ingredient since suer cannot use ingredients in storage
                            // add ingredient directly into ingredient ingredientInShoppingList after check if there is existing one
                            // otherwise, update amount for that ingredient
                            boolean anotherKey = true;
                            while(anotherKey){
                                for (int p = 0; p < ingredientInShoppingList.size(); p++) {
                                    IngredientInRecipe ingredientForShopping = ingredientInShoppingList.get(p);
                                    if(targetDescription.equals(ingredientForShopping.getBriefDescription())){
                                        double exitingAmount = ingredientForShopping.getAmountValue();
                                        double updateAmount = exitingAmount + targetIngredient.getAmountValue(); // existing amount add needed amount
                                        ingredientForShopping.setAmountValue(updateAmount);
                                        anotherKey = false;
                                    }
                                }
                                /* after iterating ingredientInShoppingList, if there is no existing one */
                                ingredientInShoppingList.add(targetIngredient);
                            }

                        }
                    }
                }

            }
            startDay.add(Calendar.DATE, 1);
        }
        return ingredientInShoppingList;
    }
}

