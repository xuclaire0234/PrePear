package com.example.prepear.ui.ShoppingList;

import androidx.annotation.NonNull;

import com.example.prepear.Ingredient;
import com.example.prepear.IngredientInRecipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MealPlanDailyIngredientCount {
    private ArrayList<IngredientInRecipe> ingredients;
    private String date;
    private String TAG;
    private CollectionReference collectionReference;
    private ArrayList<String> unitSet1, unitSet2, unitSet3;
    private ArrayList<String> idsCollection;
    private ArrayList<Double> scaleCollection;
    private ArrayList<String> checkList;
    private FirebaseFirestore db;
    private Double scale;

    public MealPlanDailyIngredientCount(String date){
        initializeUnit();
        this.date = date;
        connectToDB();
        gainDataFromIngredient();
        gainDateFromRecipe();
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
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection("Daily Meal Plans");
    }

    private void gainDataFromIngredient() {
        this.collectionReference
                .document(date)
                .collection("Ingredient Type Meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                idsCollection.clear();
                scaleCollection.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = (String) document.getData().get("Id");
                    Number scaleNumber = (Number) document.getData().get("Amount");
                    scale = scaleNumber.doubleValue();
                    db
                            .collection("Ingredient Storage")
                            .document(id).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            String briefDescription = (String) doc.getData().get("Brief Description");
                            String unit = (String) doc.getData().get("Unit");
                            String ingredientCategory = (String) doc.getData().get("Ingredient Category");
                            addIngredients(briefDescription,scale,unit,ingredientCategory);
                        }
                    });
                }
            }
        });
    }
    private void gainDateFromRecipe() {
        this.collectionReference
                .document(date)
                .collection("Recipe Type Meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        idsCollection.clear();
                        scaleCollection.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = (String) document.getData().get("Id");
                            Number scale = (Number) document.getData().get("Amount");
                            idsCollection.add(id);
                            scaleCollection.add(scale.doubleValue());
                        }
                        for (int i = 0; i < idsCollection.size(); i++) {
                            db
                                    .collection("Recipes")
                                    .document(idsCollection.get(i)).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot doc = task.getResult();
                                    Number numberOfServings = (Number) doc.getData().get("Number of Servings");
                                    scale = numberOfServings.doubleValue();
                                }
                            });
                            scale = scaleCollection.get(i)/scale;

                            db
                                    .collection("Recipes")
                                    .document(idsCollection.get(i))
                                    .collection("Ingredient")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot ingredientDoc : task.getResult()) {
                                                String briefDescription = (String) ingredientDoc.getData().get("Brief Description");
                                                Number amount = (Number) ingredientDoc.getData().get("Amount");
                                                String unit = (String) ingredientDoc.getData().get("Unit");
                                                String ingredientCategory = (String) ingredientDoc.getData().get("Ingredient Category");

                                                Double amountValue = amount.doubleValue();
                                                amountValue = amountValue * scale;
                                                addIngredients(briefDescription,amountValue,unit,ingredientCategory);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void addIngredients(String briefDescription, Double amount, String unit, String ingredientCategory) {
        Double transferredAmount = unitConvert(unit,amount.toString());
        String transferredUnit;
        if (unitSet1.contains(unit)){
            transferredUnit = "g";
        }else if (unitSet2.contains(unit)){
            transferredUnit = "ml";
        }else {
            transferredUnit = unit;
        }
        String check;
        check = briefDescription + transferredUnit + ingredientCategory;
        if (checkList.contains(check)) {
            int index = checkList.indexOf(checkList);
            addAmount(index,transferredAmount);
        } else {
            checkList.add(check);
            ingredients.add(new IngredientInRecipe(briefDescription,
                    String.valueOf(transferredAmount),transferredUnit,ingredientCategory));
        }

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

    public ArrayList<IngredientInRecipe> getIngredients() {
        return ingredients;
    }

    public String getDate() {
        return date;
    }
}
