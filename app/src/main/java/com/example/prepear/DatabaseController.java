/**
 * Classname: DatabaseController
 * Version Information: 3.0.0
 * Date: 11/17/2022
 * Author: Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the controller of database which is responsible of adding, editing, and deleting
 * elements from database.
 */
public class DatabaseController {
    // On below: initialize class attributes
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userUID;

    public DatabaseController(){
        this.db = FirebaseFirestore.getInstance(); // connect to the Firebase Firestore Cloud Database
        this.firebaseAuth = FirebaseAuth.getInstance(); // instantiate FirebaseAuthentication
        this.firebaseUser = firebaseAuth.getCurrentUser(); // get the current user
        this.userUID = firebaseUser.getUid(); // get current user's User UID
    }

    /**
     * This function adds a ingredient to the database to ingredient storage.
     * @param context information about the current state of the app received from calling activity
     * @param ingredientToAdd ingredient that is going to be added to the database
     */
    public void addIngredientToIngredientStorage(Context context, IngredientInStorage ingredientToAdd) {
        // On below part: sets detailed information of ingredient to the ingredient document
        HashMap<String, Object> data = new HashMap<>();
        String documentId = ingredientToAdd.getDocumentId();
        data.put("document id", documentId);
        data.put("description", ingredientToAdd.getBriefDescription());
        data.put("bestBeforeDate", ingredientToAdd.getBestBeforeDate());
        data.put("location", ingredientToAdd.getLocation());
        data.put("category", ingredientToAdd.getIngredientCategory());
        data.put("amount", ingredientToAdd.getAmountValue());
        data.put("unit", ingredientToAdd.getUnit());
        data.put("icon code",ingredientToAdd.getIconCode());

        // On below part: adds the ingredient's document to the Ingredient Storage Collection
        db
                .collection("Users")
                .document(userUID)
                .collection("Ingredient Storage")
                .document(documentId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // On below part: will get executed when the task is succeeded
                        Toast.makeText(context, "Ingredient has been successfully uploaded",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // On below line: gets executed if there’s any problem
                        Toast.makeText(context, "Error uploading ingredient", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This function edits an ingredient that is inside ingredient storage database.
     * @param context information about the current state of the app received from calling activity
     * @param ingredientToEdit  ingredient that is going to be edited
     */
    public void editIngredientInIngredientStorage (Context context, IngredientInStorage ingredientToEdit) {
        // On below part: update the ingredient's document inside Ingredient Storage Collection
        db
                .collection("Users")
                .document(userUID)
                .collection("Ingredient Storage")
                .document(ingredientToEdit.getDocumentId())
                .update("description", ingredientToEdit.getBriefDescription(),
                        "category", ingredientToEdit.getIngredientCategory(),
                        "bestBeforeDate", ingredientToEdit.getBestBeforeDate(),
                        "amount", ingredientToEdit.getAmountValue(),
                        "unit", ingredientToEdit.getUnit(),
                        "icon code",ingredientToEdit.getIconCode(),
                        "location", ingredientToEdit.getLocation())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Ingredient has been successfully edited",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error editing ingredient", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This function deletes an ingredient from the ingredient storage database.
     * @param context information about the current state of the app received from calling activity
     * @param ingredientToDelete ingredient that is going to be deleted
     */
    public void deleteIngredientFromIngredientStorage (Context context, IngredientInStorage ingredientToDelete) {
        // On below part: delete the ingredient's document from Ingredient Storage Collection
        db
                .collection("Users")
                .document(userUID)
                .collection("Ingredient Storage")
                .document(ingredientToDelete.getDocumentId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Ingredient has been successfully deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error deleting ingredient", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This function either adds or edits a ingredient to/from the recipe database.
     * @param context information about the current state of the app received from calling activity
     * @param recipeToAdd recipe that is going to be added
     * @param ingredientInRecipeDataList the list of ingredients that is included in that specific recipe
     * @param editDeleteListSaved the list that is either going to be edited or deleted ?
     * @param idOfRecipe the id of the specific recipe
     */
    public void addEditRecipeToRecipeList(Context context, Recipe recipeToAdd,
                                          ArrayList<IngredientInRecipe> ingredientInRecipeDataList,
                                          ArrayList<String> editDeleteListSaved, String idOfRecipe) {
        /* sets detailed information of recipe to the recipe document */
        HashMap<String, Object> data = new HashMap<>();
        String recipeId = idOfRecipe;
        data.put("Id", recipeId);
        data.put("Image URI", recipeToAdd.getImageURI());
        data.put("Title", recipeToAdd.getTitle());
        data.put("Preparation Time", recipeToAdd.getPreparationTime());
        data.put("Number of Servings", recipeToAdd.getNumberOfServings());
        data.put("Recipe Category", recipeToAdd.getRecipeCategory());
        data.put("Comments", recipeToAdd.getComments());

        /* add recipe document to the recipe list collection */
        db
                .collection("Users")
                .document(userUID)
                .collection("Recipes")
                .document(recipeId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Recipe list has been successfully updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error uploading recipe", Toast.LENGTH_SHORT).show();
                    }
                });

        /* delete the old version of ingredient list inside the recipe ? */
        for (String id : editDeleteListSaved){
            db
                    .collection("Users")
                    .document(userUID)
                    .collection("Recipes")
                    .document(recipeId)
                    .collection("Ingredient")
                    .document(id)
                    .delete();
        }

        /* add the ingredient list into the specific recipe document one by one in nested format */
        for (IngredientInRecipe ingredient: ingredientInRecipeDataList) {
            data = new HashMap<>();
            String ingredientId = ingredient.getId();
            data.put("Id", recipeId);
            data.put("Brief Description", ingredient.getBriefDescription());
            data.put("Amount", ingredient.getAmountValue());
            data.put("Unit", ingredient.getUnit());
            data.put("Ingredient Category", ingredient.getIngredientCategory());

            db
                    .collection("Users")
                    .document(userUID)
                    .collection("Recipes")
                    .document(recipeId)
                    .collection("Ingredient")
                    .document(ingredientId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            /* Toast.makeText(context, "Ingredients has been successfully uploaded",
                                    Toast.LENGTH_SHORT).show(); */
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error uploading ingredients", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * This function deletes a recipe from recipe list database.
     * @param context information about the current state of the app received from calling activity
     * @param recipeToDelete recipe that is going to be deleted
     */
    public void deleteRecipeFromRecipeList(Context context, Recipe recipeToDelete) {
        /* delete the list of ingredient one by one from recipe document */
        ArrayList<IngredientInRecipe> IngredientListToBeDeleted = recipeToDelete.getListOfIngredients();
        for (IngredientInRecipe ingredient: IngredientListToBeDeleted) {
            db
                    .collection("Users")
                    .document(userUID)
                    .collection("Recipes")
                    .document(recipeToDelete.getId())
                    .collection("Ingredient")
                    .document(ingredient.getId())
                    .delete();
        }

        // On below part: delete the recipe document from the recipe list collection
        db
                .collection("Users")
                .document(userUID)
                .collection("Recipes")
                .document(recipeToDelete.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Recipe has been successfully deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error deleting recipe", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This function either adds or edits a meal to/from a daily meal plan of daily meal plans database.
     * @param context information about the current state of the app received from calling activity
     * @param dailyMealPlan the daily meal plan where to add/edit the meal to/from
     * @param mealToUpdate the meal to be added/edited
     */
    public void addEditMealToDailyMealPlan(Context context, DailyMealPlan dailyMealPlan, Meal mealToUpdate) {
        HashMap<String, Object> data = new HashMap<>();
        String mealID = mealToUpdate.getMealID();
        data.put("Meal ID", mealID);
        String documentID = mealToUpdate.getDocumentID();
        data.put("Document ID", documentID);
        String mealType = mealToUpdate.getMealType();
        data.put("Meal Type", mealType);
        Integer eatHour = mealToUpdate.getEatHour();
        data.put("Eat Hour", eatHour);
        Integer eatMinute = mealToUpdate.getEatMinute();
        data.put("Eat Minute", eatMinute);
        if (mealType.equals("Recipe")) {
            data.put("Customized Scaling Number", mealToUpdate.getCustomizedNumberOfServings());
        } else {
            data.put("Customized Scaling Number", mealToUpdate.getCustomizedAmount());
        }
        db
                .collection("Users")
                .document(userUID)
                .collection("Daily Meal Plans")
                .document(dailyMealPlan.getCurrentDailyMealPlanDate())
                .collection("Meals")
                .document(mealID)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Meal has been successfully updated in daily meal plan",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error updating meal.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This function deletes a meal from a daily meal plan of daily meal plans database.
     * @param context information about the current state of the app received from calling activity
     * @param dailyMealPlan the daily meal plan where to delete the meal
     * @param mealToDelete the meal to be deleted
     */
    public void deleteMealFromDailyMealPlan(Context context, DailyMealPlan dailyMealPlan, Meal mealToDelete) {
        db
                .collection("Users")
                .document(userUID)
                .collection("Daily Meal Plans")
                .document(dailyMealPlan.getCurrentDailyMealPlanDate())
                .collection("Meals")
                .document(mealToDelete.getMealID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Meal has been successfully deleted from daily meal plan",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error deleting meal.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This function adds a daily meal plan to the daily meal plans database.
     * @param context information about the current state of the app received from calling activity
     * @param dailyMealPlanToAdd the daily meal plan to be added
     */
    public void addDailyMealPlanToMealPlan(Context context, DailyMealPlan dailyMealPlanToAdd) {
        // adds the daily meal plan to the daily meal plans database
        Map<String, Object> data = new HashMap<>();
        String date = dailyMealPlanToAdd.getCurrentDailyMealPlanDate();
        data.put("Date", date);
        db
                .collection("Users")
                .document(userUID)
                .collection("Daily Meal Plans")
                .document(date)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Daily meal plan has been successfully added to meal plan",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error adding daily meal plan.", Toast.LENGTH_SHORT).show();
                    }
                });

        // adds the list of meals one by one to the daily meal plan document
        for (Meal meal: dailyMealPlanToAdd.getDailyMealDataList()) {
            addEditMealToDailyMealPlan(context, dailyMealPlanToAdd, meal);
        }
    }

    /**
     * This function deletes a daily meal plan from daily meal plans database.
     * @param context information about the current state of the app received from calling activity
     * @param dailyMealPlanToDelete the daily meal plan to be deleted
     */
    public void deleteDailyMealPlanFromMealPlan(Context context, DailyMealPlan dailyMealPlanToDelete) {
        // deletes the list of meals one by one from daily meal plan document
        for (Meal meal: dailyMealPlanToDelete.getDailyMealDataList()) {
            deleteMealFromDailyMealPlan(context, dailyMealPlanToDelete, meal);
        }

        // deletes daily meal plan from daily meal plans database
        db
                .collection("Users")
                .document(userUID)
                .collection("Daily Meal Plans")
                .document(dailyMealPlanToDelete.getCurrentDailyMealPlanDate())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Daily meal plan has been successfully deleted from meal plan",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error deleting daily meal plan.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}