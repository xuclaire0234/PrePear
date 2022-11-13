/**
 * Classname: DatabaseController
 * Version Information: 1.0.0
 * Date: 11/3/2022
 * Author: Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the controller of database which is responsible of adding, editing, and deleting
 * elements from database.
 */
public class DatabaseController {
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // connects to the database

    /**
     * This function adds a ingredient to the database to ingredient storage.
     * @param context information about the current state of the app received from calling activity
     * @param ingredientToAdd ingredient that is going to be added to the database
     */
    public void addIngredientToIngredientStorage(Context context, IngredientInStorage ingredientToAdd) {
        /* sets detailed information of ingredient to the ingredient document */
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

        /* adds the ingredient document to the ingredient storage collection */
        db
                .collection("Ingredient Storage")
                .document(documentId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Toast.makeText(context, "Ingredient has been successfully uploaded",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
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
        /* edit the ingredient document inside ingredient storage collection */
        db
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
        /* delete the ingredient document from ingredient storage collection */
        db
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
                    .collection("Recipes")
                    .document(recipeId).collection("Ingredient")
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
                    .collection("Recipes")
                    .document(recipeToDelete.getId())
                    .collection("Ingredient")
                    .document(ingredient.getId())
                    .delete();
        }

        /* delete the recipe document from the recipe list collection*/
        db
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
}