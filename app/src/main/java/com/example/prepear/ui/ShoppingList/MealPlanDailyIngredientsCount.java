package com.example.prepear.ui.ShoppingList;

import androidx.annotation.NonNull;

import com.example.prepear.Ingredient;
import com.example.prepear.IngredientInRecipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MealPlanDailyIngredientsCount {
    private ArrayList<IngredientInRecipe> ingredients;
    private String date;
    private String TAG;
    private CollectionReference collectionReference;
    private ArrayList<String> unitSet1, unitSet2, unitSet3;
    private ArrayList<String> descriptionUnitCombine;

    public MealPlanDailyIngredientsCount(String date){
        initializeUnit();
        this.date = date;
        connectToDB();
        gainData();
    }

    public void initializeUnit() {
        this.unitSet1 = new ArrayList<>();
        this.unitSet1.add("g");
        this.unitSet1.add("kg");
        this.unitSet1.add("oz");
        this.unitSet1.add("lb");
        this.unitSet2 = new ArrayList<>();
        this.unitSet2.add("ml");
        this.unitSet2.add("l");
    }

    private void connectToDB() {
        this.TAG = "MealPlan";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection("Daily Meal Plans");
    }

    private void gainData() {
        this.collectionReference
                .document(date)
                .collection("Ingredient Type Meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String briefDescription = (String) document.getData().get("Brief Description");
                    Number amount = (Number) document.getData().get("Amount");
                    String unit = (String) document.getData().get("Unit");
                    String ingredientCategory = (String) document.getData().get("Ingredient Category");

                    Double transferredAmount = unitConvert(unit,amount.toString());
                    String transferredUnit;
                    if (unitSet1.contains(unit)){
                        transferredUnit = "g";
                    }else if (unitSet2.contains(unit)){
                        transferredUnit = "ml";
                    }else {
                        transferredUnit = unit;
                    }
                    String checkList;
                    checkList = briefDescription + transferredUnit + ingredientCategory;
                    if (descriptionUnitCombine.contains(checkList)){
                        int index = descriptionUnitCombine.indexOf(checkList);
                        addAmount(index,transferredAmount);
                    } else {
                        descriptionUnitCombine.add(checkList);
                        ingredients.add(new IngredientInRecipe(briefDescription,
                                String.valueOf(transferredAmount),transferredUnit,ingredientCategory));
                    }
                }
            }
        });

        this.collectionReference
                .document(date)
                .collection("Recipe Type Meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            
                        }
                    }
                });


    }

    private void addIngredients(IngredientInRecipe ingredient) {
        ingredients.add(ingredient);
    }

    private void addAmount(Integer index, Double amount) {
        Double sumAmount = ingredients.get(index).getAmountValue();
        sumAmount = sumAmount + amount;
        ingredients.get(index).setAmountValue(sumAmount);
        ingredients.get(index).setAmountString(String.valueOf(sumAmount));
    }

    private Double unitConvert(String unit, String amount) {
        Double amountValue = Double.parseDouble(amount);
        Double scale = 1.0;
        if (unit == "kg") {
            scale = 1000.0;
        }else if (unit == "oz") {
            scale = 28.3495;
        }else if (unit == "lb") {
            scale = 453.592;
        }else if (unit == "l") {
            scale = 1000.0;
        }
        return amountValue * scale;
    }
}
