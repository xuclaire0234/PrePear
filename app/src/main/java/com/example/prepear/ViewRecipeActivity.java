package com.example.prepear;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView recipeList;
    CustomRecipeList recipeAdapter;
    ArrayList<Recipe> recipeDataList;
    Integer sortItemRecipe = 0;
    Integer recipePosition = -1;
    final String[] sortItemSpinnerContent = {"Title", "Preparation Time", "Number Of Serving", "Recipe Category"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        final Spinner sortItemSpinner;
        final ImageButton mainPage;
        final ImageButton viewIngredients;
        final ImageButton viewMealPlan;
        final ImageButton viewShoppingList;
        final FloatingActionButton addRecipe;

        final TextView testForSpinner;
        testForSpinner = findViewById(R.id.testSpinner);


        final String TAG = "Recipes";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Recipes");

        sortItemSpinner = findViewById(R.id.sort_spinner);
        viewIngredients = findViewById(R.id.ingredient_storage_button);
        mainPage = findViewById(R.id.home_button);
        viewMealPlan = findViewById(R.id.meal_planner_button);
        viewShoppingList = findViewById(R.id.shopping_list_button);
        addRecipe = findViewById(R.id.add_recipe_button);
        recipeList = findViewById(R.id.recipe_listview);

        recipeDataList = new ArrayList<>();
        recipeAdapter = new CustomRecipeList(this, recipeDataList);
        recipeList.setAdapter(recipeAdapter);

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipePosition = -1;
                // Intent intent = new Intent(ViewRecipeActivity.this,AddRecipeActivity.class);
                // startActivity(intent);
            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                recipePosition = i;
                Recipe recipe;
                recipe = recipeAdapter.getItem(i);
                // Intent intent = new Intent(ViewRecipeActivity.this,EditRecipeActivity.class);
                // intent.putExtra("recipeList", recipe);
                // startActivity(intent);
            }
        });

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
                // sortItemRecipe = recipeAdapter.getSortItemRecipe();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Preparation Time")));
                    String title = doc.getId();
                    Number preparationTime = (Number) doc.getData().get("Preparation Time");
                    Number numberOfServings = (Number) doc.getData().get("Number of Servings");
                    String recipeCategory = (String) doc.getData().get("Recipe Category");
                    String comments = (String) doc.getData().get("Comments");
                    //ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) doc.getData().get("Ingredients");
                    recipeDataList.add(new Recipe(title, preparationTime.intValue(), numberOfServings.intValue(), recipeCategory, comments));
                }
                recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                recipeAdapter.sortRecipe(sortItemRecipe);
                testForSpinner.setText(String.valueOf(sortItemRecipe));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.sortItemRecipe = i;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        this.sortItemRecipe = 3;
    }
}