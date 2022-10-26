package com.example.prepear;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewRecipeListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView recipeList;
    CustomRecipeList recipeAdapter;
    ArrayList<Recipe> recipeDataList;
    Integer sortItemRecipe = 0;
    Integer recipePosition = -1;
    int LAUNCH_ADD_RECIPE_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        final Spinner sortItemSpinner;
        final Button mainPage;
        final Button viewIngredients;
        final Button viewMealPlan;
        final Button viewShoppingList;
        final Button addRecipe;
        final String[] sortItemSpinnerContent = {"Title", "Preparation Time", "Number Of Serving", "Recipe Category"};


        final String TAG = "Recipe";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Recipes");


        recipeDataList = new ArrayList<>();
        recipeAdapter = new CustomRecipeList(this, recipeDataList);
        recipeList.setAdapter(recipeAdapter);

        addRecipe = null; // set button, change later
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipePosition = -1;
                Intent intent = new Intent(ViewRecipeListActivity.this, AddEditRecipeActivity.class);
                startActivityForResult(intent, LAUNCH_ADD_RECIPE_ACTIVITY);
            }
        });

        recipeList = null; // set ListView, change later
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                recipePosition = i;
                Recipe viewedRecipe = recipeAdapter.getItem(i);
                Intent intent = new Intent(ViewRecipeListActivity.this, ViewRecipeActivity.class);
                intent.putExtra("viewed recipe", viewedRecipe);
                startActivity(intent);
            }
        });

        sortItemSpinner = null; // set to null first, change next.
        sortItemSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sortItemSpinnerContent);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortItemSpinner.setAdapter(ad);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                recipeDataList.clear();
                sortItemRecipe = recipeAdapter.getSortItemRecipe();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Preparation Time")));
                    String title = doc.getId();
                    Integer preparationTime = (Integer) doc.getData().get("Preparation Time");
                    Integer numberOfServings = (Integer) doc.getData().get("Number of Servings");
                    String recipeCategory = (String) doc.getData().get("Recipe Category");
                    String comments = (String) doc.getData().get("Comments");
                    ArrayList<IngredientInRecipe> ingredients = (ArrayList<IngredientInRecipe>) doc.getData().get("Ingredients");
                    Uri imageURI = (Uri) doc.getData().get("ImageURI");
                    recipeDataList.add(new Recipe(imageURI, title, preparationTime, numberOfServings, recipeCategory, comments, ingredients));
                }
                recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                recipeAdapter.sortRecipe(sortItemRecipe);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sortItemRecipe = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_ADD_RECIPE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Recipe recipeToAdd = (Recipe) data.getSerializableExtra("new recipe");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // No action
            }
        }
    }
}