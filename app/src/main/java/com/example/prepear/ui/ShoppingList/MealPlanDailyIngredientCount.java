package com.example.prepear.ui.ShoppingList;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.prepear.Ingredient;
import com.example.prepear.IngredientInRecipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MealPlanDailyIngredientCount {
    private ArrayList<IngredientInRecipe> ingredients;
    private String date;
    private String TAG;
    private CollectionReference collectionReference;
    private ArrayList<String> unitSet;
    private ArrayList<String> recipeIdsCollection, ingredientIdsCollection;
    private ArrayList<Double> recipeScaleCollection, ingredientScaleCollection;
    private ArrayList<String> checkList = new ArrayList<>();
    private FirebaseFirestore db;
    private Double scale;
    private IngredientInRecipe recipe;

    public interface listener {
        void onCatch(IngredientInRecipe ingredientInRecipe);
    }

    public MealPlanDailyIngredientCount(String date){
        this.initializeUnit();
        this.date = date;
        this.connectToDB();
        ingredients = new ArrayList<>();
        ingredientIdsCollection = new ArrayList<>();
        ingredientScaleCollection = new ArrayList<>();
        recipeIdsCollection = new ArrayList<>();
        recipeScaleCollection = new ArrayList<>();
        this.gainAllIngredientAtDate(date);
    }

    public void initializeUnit() {
        this.unitSet = new ArrayList<>();
        this.unitSet.add("g");
        this.unitSet.add("kg");
        this.unitSet.add("oz");
        this.unitSet.add("lb");
        this.unitSet.add("ml");
        this.unitSet.add("l");
    }

    private void connectToDB() {
        this.TAG = "MealPlan";
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection("Daily Meal Plans");
    }

    private void test() {
        final IngredientInRecipe recipes = new IngredientInRecipe("1","1","1","1");
        db
                .collection("Ingredient Storage")
                .document("2022-11-12 14:25:25").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String briefDescription = (String) documentSnapshot.get("description");
                        String unit = (String) documentSnapshot.get("unit");
                        String ingredientCategory = (String) documentSnapshot.get("category");
                        recipes.setBriefDescription(briefDescription);
                        recipes.setAmountString("11");
                        recipes.setUnit(unit);
                        recipes.setIngredientCategory(ingredientCategory);
                    }
                });
        ingredients.add(recipes);
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

    private void addIngredients(String briefDescription, Double amount, String unit, String ingredientCategory) {
        Double transferredAmount = unitConvert(unit, amount);
        String transferredUnit;
        if (unit == "l" || unit == "ml"){
            transferredUnit = "ml";
        }else {
            transferredUnit = "g";
        }
        if (checkList.contains(briefDescription)) {
            int index = checkList.indexOf(briefDescription);
            String category = ingredients.get(index).getIngredientCategory();
            if (!category.contains(ingredientCategory)) {
                category = category + ingredientCategory;
                ingredients.get(index).setIngredientCategory(category);
                addAmount(index,transferredAmount);
            }
        } else {
            checkList.add(briefDescription);
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

    private Double unitConvert(String unit, Double amount) {
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
        return amount * scale;
    }

    public ArrayList<IngredientInRecipe> getIngredients() {
        return ingredients;
    }

    public String getDate() {
        return date;
    }
}
