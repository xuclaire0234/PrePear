package com.example.prepear;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewRecipeActivity extends AppCompatActivity {

    ListView recipeList;
    CustomRecipeList recipeAdapter;
    ArrayList<Recipe> recipeDataList;
    Integer sortItemRecipe = 0;

    String[] sortItemsRecipe = {"title", "preparation time", "number of servings", "recipe category"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        recipeDataList = new ArrayList<>();
        recipeAdapter = new CustomRecipeList(this, recipeDataList);


        final String TAG = "Recipe";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Recipes");

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
                    ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) doc.getData().get("Ingredients");
                    recipeDataList.add(new Recipe(title, preparationTime, numberOfServings, recipeCategory, comments, ingredients));
                }
                recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                recipeAdapter.sortRecipe(sortItemRecipe);
            }
        });
    }

}