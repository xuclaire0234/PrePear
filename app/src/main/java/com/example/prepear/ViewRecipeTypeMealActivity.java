/**
 * Classname: ViewRecipeTypeMealActivity
 * Version Information: 1.0.0
 * Date: 11/16/2022
 * Author: Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This class defines the view recipe type meal activity that allows user to view details of
 * a specific recipe type meal that is inside the specific daily meal plan.
 */
public class ViewRecipeTypeMealActivity extends AppCompatActivity implements CustomizeNumberOfServingsFragment.OnFragmentInteractionListener {
    // initializes class attributes
    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView preparationTimeTextView;
    private TextView numberOfServingsEditText;
    private TextView originalNumberOfServingsTextView;
    private TextView recipeCategoryTextView;
    private TextView commentsTextView;
    private ListView ingredientInRecipeListView;
    private ArrayAdapter<IngredientInRecipe> ingredientInRecipeArrayAdapter;
    private ArrayList<IngredientInRecipe> ingredientInRecipeDataList;
    private Button commitButton;
    private Button deleteButton;
    private Button returnButton;
    private Meal viewedMeal;
    private Integer originalNumberOfServings;
    private Integer originalCustomizedNumberOfServings;
    private ArrayList<Double> originalIngredientInRecipeAmountList;

    /**
     * This method creates the ViewRecipeTypeMealActivity.
     * @param savedInstanceState a {@link Bundle} object that can be used to recreate the activity and load all data from it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_type_meal);

        // connects the layout with the views and buttons
        imageImageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.title_TextView);
        preparationTimeTextView = findViewById(R.id.preparation_time_TextView);
        numberOfServingsEditText = findViewById(R.id.number_of_servings_EditText);
        originalNumberOfServingsTextView = findViewById(R.id.original_number_of_servings_hint_TextView);
        recipeCategoryTextView = findViewById(R.id.recipe_category_TextView);
        commentsTextView = findViewById(R.id.comments_TextView);
        ingredientInRecipeListView = findViewById(R.id.ingredient_in_recipe_ListView);
        commitButton = findViewById(R.id.commit_button);
        deleteButton = findViewById(R.id.delete_button);
        returnButton = findViewById(R.id.return_button);

        // sets up the ingredient in recipe list view
        ingredientInRecipeDataList = new ArrayList<IngredientInRecipe>();
        ingredientInRecipeArrayAdapter = new CustomIngredientInRecipeList(this, ingredientInRecipeDataList);
        ingredientInRecipeListView.setAdapter(ingredientInRecipeArrayAdapter);

        // sets up a new array list for storing original amount needed for each ingredient in recipe list
        originalIngredientInRecipeAmountList = new ArrayList<Double>();

        /* gets the information of the specific recipe being clicked inside ViewDailyMealPlanActivity
        and display them on the screen */
        viewedMeal = (Meal) getIntent().getSerializableExtra("viewed meal");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Recipes").document(viewedMeal.getDocumentID());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // gets and displays all the detailed information of the recipe type meal besides the list of ingredient and number of servings
                        Glide.with(ViewRecipeTypeMealActivity.this)
                                .load((String) document.getData().get("Image URI")).into(imageImageView);
                        titleTextView.setText((String) document.getData().get("Title"));
                        preparationTimeTextView.setText(String.format(Locale.getDefault(),
                                "%d", ((Long) document.getData().get("Preparation Time")).intValue()));
                        originalNumberOfServings = ((Long) document.getData().get("Number of Servings")).intValue();  // stores the original number of servings
                        originalNumberOfServingsTextView.setText(originalNumberOfServings.toString());
                        recipeCategoryTextView.setText((String) document.getData().get("Recipe Category"));
                        commentsTextView.setText((String) document.getData().get("Comments"));

                        // gets and displays the customized number of servings
                        originalCustomizedNumberOfServings = viewedMeal.getCustomizedNumberOfServings();
                        numberOfServingsEditText.setText(String.format(Locale.getDefault(),
                                "%d", originalCustomizedNumberOfServings));

                        // gets and displays all the detailed information of the list of ingredients in recipe type meal
                        CollectionReference colRef = docRef.collection("Ingredient");
                        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    /* Loop through all the documents in Ingredient collection to get the
                                    ingredient in recipe list and their original amount being needed */
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String briefDescription = (String) doc.getData().get("Brief Description");
                                        Double amount = (Double) doc.getData().get("Amount");
                                        String unit = (String) doc.getData().get("Unit");
                                        String ingredientCategory = (String) doc.getData().get("Ingredient Category");

                                        IngredientInRecipe ingredient = new IngredientInRecipe(briefDescription, amount.toString(), unit, ingredientCategory); // initialize a ingredient object
                                        ingredient.setId(doc.getId()); // set id of the ingredient in database
                                        ingredientInRecipeDataList.add(ingredient);
                                        originalIngredientInRecipeAmountList.add(amount);
                                    }
                                    ingredientInRecipeArrayAdapter.notifyDataSetChanged();

                                    // scales the ingredient amounts to be displayed
                                    scaleIngredientAmounts(originalNumberOfServings, originalCustomizedNumberOfServings);
                                    ingredientInRecipeArrayAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // sets up the number of servings text view to be able to direct to customize number of servings fragment once been clicked
        numberOfServingsEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomizeNumberOfServingsFragment.newInstance(numberOfServingsEditText.getText().toString()).show(getSupportFragmentManager(), "CUSTOMIZE_NUMBER_OF_SERVINGS");
            }
        });

        /* sets delete button to delete the viewing recipe type meal after clicking it, and then return
        to the ViewDailyMealPlanActivity */
        deleteButton.setOnClickListener((View v) -> {
            // return to the calling ViewDailyMealPlanActivity
            Intent returnIntent = new Intent();
            returnIntent.putExtra("action", "delete meal");
            returnIntent.putExtra("mealToDelete", viewedMeal);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        // sets return button to directly return to the ViewDailyMealPlanActivity after clicking it
        returnButton.setOnClickListener((View v) -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });

        // sets commit button to update the customized number of servings of the meal
        commitButton.setOnClickListener((View v) -> {
            Intent returnIntent = new Intent();
            String currentCustomizedNumberOfServings = numberOfServingsEditText.getText().toString();

            // checks if the customized number of servings changed or not, and set result based on it, then return
            if (currentCustomizedNumberOfServings.equals(originalCustomizedNumberOfServings.toString())) {
                setResult(Activity.RESULT_CANCELED, returnIntent);
            } else {
                viewedMeal.setCustomizedNumberOfServings(Integer.parseInt(currentCustomizedNumberOfServings));
                returnIntent.putExtra("action", "update meal");
                returnIntent.putExtra("mealToUpdate", viewedMeal);
                setResult(Activity.RESULT_OK, returnIntent);
            }
            finish();
        });
    }

    /**
     * This method gets the new customized number of servings set by user inside CustomizeNumberOfServingsFragment
     * and scales the ingredients in recipe based on it.
     * @param newCustomizedNumberOfServings the newly customized number of servings of type {@link String}
     */
    public void onConfirmPressed(String newCustomizedNumberOfServings) {
        scaleIngredientAmounts(originalNumberOfServings, Integer.parseInt(newCustomizedNumberOfServings));
        numberOfServingsEditText.setText(newCustomizedNumberOfServings);
    }

    /**
     * This method scales the ingredients needed in recipe and updates it to data list for displaying purpose.
     * @param originalNumberOfServings the original number of servings that belongs to the recipe that is of type {@link Integer}
     * @param customizedNumberOfServings the customized number of servings entered by user that is of type {@link Integer}
     */
    public void scaleIngredientAmounts(Integer originalNumberOfServings, Integer customizedNumberOfServings) {
        for (IngredientInRecipe ingredient: ingredientInRecipeDataList) {
            // on below loops through all the ingredients inside ingredient list to change its amount to display
            int index = ingredientInRecipeDataList.indexOf(ingredient); // gets the current index of the ingredient
            double originalIngredientAmountNeeded = originalIngredientInRecipeAmountList.get(index); // gets the original amount value of the ingredient from original ingredient in recipe list using index
            double scalingNumber = (double) customizedNumberOfServings / originalNumberOfServings;  // calculates the scaling number
            scalingNumber = Math.round(scalingNumber * 100.0) / 100.0;  // rounds the scaling number to two decimal places
            double scaledIngredientAmount = originalIngredientAmountNeeded * (scalingNumber);   // calculates the scaled ingredient amount number based on scaling number
            scaledIngredientAmount = Math.round(scaledIngredientAmount * 100.0) / 100.0;    // rounds the scaled ingredient amount number to two decimal places
            ingredientInRecipeDataList.get(index).setAmountValue(scaledIngredientAmount);  // sets the amount value to the calculated value
            ingredientInRecipeDataList.get(index).setAmountString(String.valueOf(scaledIngredientAmount)); // sets the amount string to the calculated string
        }
        ingredientInRecipeArrayAdapter.notifyDataSetChanged();
    }
}