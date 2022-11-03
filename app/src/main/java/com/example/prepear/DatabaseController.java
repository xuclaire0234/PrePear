/*
 * Classname: DatabaseController
 *
 * Version information: 1.0.0
 *
 * Date: 11/2/2022
 *
 * Copyright notice: Jiayin He
 */

package com.example.prepear;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the controller of database which is responsible of adding, editing, and deleting
 * elements from database.
 */
public class DatabaseController {
    // connects to the database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private IngredientInStorage newIngredientInStorage;

    public void viewIngredientsInIngredientStorage(Context context, ArrayList<IngredientInStorage> ingredientStorageDataList) {
        db
                .collection("Ingredient Storage")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        ingredientStorageDataList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String documentID = doc.getId();
                            String description =  (String) doc.getData().get("description");
                            String bestBeforeDate = (String) doc.getData().get("bestBeforeDate");
                            String location = (String) doc.getData().get("location");
                            String unit = (String) doc.getData().get("unit");
                            String amount = String.valueOf(doc.getData().get("amount"));
                            String category = (String) doc.getData().get("category");

                            ingredientStorageDataList.add(new IngredientInStorage(description, category,
                                    bestBeforeDate, location, amount, unit, documentID));
                        }
                    }
                });
    }

    public void addIngredientToIngredientStorage(Context context, IngredientInStorage ingredientToAdd) {
        HashMap<String, Object> data = new HashMap<>();
        String documentId = ingredientToAdd.getDocumentId();
        data.put("document id", documentId);
        data.put("description", ingredientToAdd.getBriefDescription());
        data.put("bestBeforeDate", ingredientToAdd.getBestBeforeDate());
        data.put("location", ingredientToAdd.getLocation());
        data.put("category", ingredientToAdd.getIngredientCategory());
        data.put("amount", ingredientToAdd.getAmountValue());
        data.put("unit", ingredientToAdd.getUnit());

        db
                .collection("Ingredient Storage")
                .document(documentId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        // Log.d(description, "This Ingredient's Data has been added successfully into the Storage!");
                        Toast.makeText(context, "Ingredient has been successfully uploaded",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        // Log.d(description, "Data cannot be added!" + e.toString());
                        Toast.makeText(context, "Error uploading ingredient", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editIngredientInIngredientStorage (Context context, IngredientInStorage ingredientToEdit) {
        db
                .collection("Ingredient Storage")
                .document(ingredientToEdit.getDocumentId())
                .update("description", ingredientToEdit.getBriefDescription(),
                        "category", ingredientToEdit.getIngredientCategory(),
                        "bestBeforeDate", ingredientToEdit.getBestBeforeDate(),
                        "amount", ingredientToEdit.getAmountValue(),
                        "unit", ingredientToEdit.getUnit(),
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

    public void deleteIngredientFromIngredientStorage (Context context, IngredientInStorage ingredientToDelete) {
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

    public ArrayList<Recipe> viewRecipesInRecipeList (Context context, ArrayList<Recipe> recipeDataList) {
        ArrayList<Recipe> newRecipeDataList = recipeDataList;
        db
                .collection("Recipes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        newRecipeDataList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String documentID = doc.getId();
                            String title = (String) doc.getData().get("Title");
                            Number preparationTime = (Number) doc.getData().get("Preparation Time");
                            Number numberOfServings = (Number) doc.getData().get("Number of Servings");
                            String recipeCategory = (String) doc.getData().get("Recipe Category");
                            String comments = (String) doc.getData().get("Comments");
                            String imageURI = (String) doc.getData().get("Image URI");

                            Recipe newRecipe = new Recipe(imageURI, title, preparationTime.intValue(),
                                    numberOfServings.intValue(), recipeCategory, comments);
                            newRecipe.setId(documentID);
                            newRecipeDataList.add(newRecipe);
                        }

                        for (int i = 0; i < newRecipeDataList.size(); i++) {
                            int indexOfRecipe = i;

                            db.collection("Recipes")
                                    .document(newRecipeDataList.get(indexOfRecipe).getId())
                                    .collection("Ingredient")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            newRecipeDataList.get(indexOfRecipe).deleteAllIngredients();

                                            // loop through all the documents in Ingredient collection
                                            for (QueryDocumentSnapshot doc : value) {
                                                String id = doc.getId();

                                                String briefDescription = (String) doc.getData().get("Brief Description");
                                                Number amount = (Number) doc.getData().get("Amount");
                                                String unit = (String) doc.getData().get("Unit");
                                                String ingredientCategory = (String) doc.getData().get("Ingredient Category");

                                                IngredientInRecipe newIngredient = new IngredientInRecipe(briefDescription, amount.toString(), unit, ingredientCategory);
                                                newIngredient.setId(id);
                                                newRecipeDataList.get(indexOfRecipe).addIngredientToRecipe(newIngredient);
                                            }
                                        }
                                    });
                        }
                    }
                });
        return newRecipeDataList;
    }

    public void addEditRecipeToRecipeList(Context context, Recipe recipeToAdd, ArrayList<IngredientInRecipe> ingredientInRecipeDataList, ArrayList<String> editDeleteListSaved, String idOfRecipe) {
        HashMap<String, Object> data = new HashMap<>();
        String recipeId = idOfRecipe;
        data.put("Id", recipeId);
        data.put("Image URI", recipeToAdd.getImageURI());
        data.put("Title", recipeToAdd.getTitle());
        data.put("Preparation Time", recipeToAdd.getPreparationTime());
        data.put("Number of Servings", recipeToAdd.getNumberOfServings());
        data.put("Recipe Category", recipeToAdd.getRecipeCategory());
        data.put("Comments", recipeToAdd.getComments());

        db
                .collection("Recipes")
                .document(recipeId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Recipe has been successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error uploading recipe", Toast.LENGTH_SHORT).show();
                    }
                });

        for (String id : editDeleteListSaved){
            db
                    .collection("Recipes")
                    .document(recipeId).collection("Ingredient")
                    .document(id)
                    .delete();
        }

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
                            Toast.makeText(context, "Ingredients has been successfully uploaded", Toast.LENGTH_SHORT).show();
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

    public void deleteRecipeFromRecipeList(Context context, Recipe recipeToDelete) {
        ArrayList<IngredientInRecipe> IngredientListToBeDeleted = recipeToDelete.getListOfIngredients();
        for (IngredientInRecipe ingredient: IngredientListToBeDeleted) {
            db
                    .collection("Recipes")
                    .document(recipeToDelete.getId())
                    .collection("Ingredient")
                    .document(ingredient.getId())
                    .delete();
        }

        final String TAG = "Recipes";
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