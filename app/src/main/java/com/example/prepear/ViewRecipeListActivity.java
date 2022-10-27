package com.example.prepear;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewRecipeListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView recipeList;
    CustomRecipeList recipeAdapter;
    ArrayList<Recipe> recipeDataList;
    Integer sortItemRecipe = 0;
    Integer recipePosition = -1;
    int LAUNCH_ADD_RECIPE_ACTIVITY = 1;
    final String[] sortItemSpinnerContent = {"Title", "Preparation Time", "Number Of Serving", "Recipe Category"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_activity);

        final Spinner sortItemSpinner;
        final ImageButton mainPage;
        final ImageButton viewIngredients;
        final ImageButton viewMealPlan;
        final ImageButton viewShoppingList;
        final FloatingActionButton addRecipe;


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
                Intent intent = new Intent(ViewRecipeListActivity.this, AddEditRecipeActivity.class);
                startActivityForResult(intent, LAUNCH_ADD_RECIPE_ACTIVITY);
            }
        });

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

        sortItemSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortItemSpinnerContent);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortItemSpinner.setAdapter(ad);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                recipeDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Preparation Time")));
                    String title = doc.getId();
                    Number preparationTime = (Number) doc.getData().get("Preparation Time");
                    Number numberOfServings = (Number) doc.getData().get("Number of Servings");
                    String recipeCategory = (String) doc.getData().get("Recipe Category");
                    String comments = (String) doc.getData().get("Comments");
                    Uri imageURI = (Uri) doc.getData().get("ImageURI");
                    //ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) doc.getData().get("Ingredients");
                    recipeDataList.add(new Recipe(imageURI, title, preparationTime.intValue(), numberOfServings.intValue(), recipeCategory, comments)
                    );
                }
                recipeAdapter.sortRecipe(sortItemRecipe);
                recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sortItemRecipe = i;
        recipeAdapter.sortRecipe(sortItemRecipe);
        recipeAdapter.notifyDataSetChanged();
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