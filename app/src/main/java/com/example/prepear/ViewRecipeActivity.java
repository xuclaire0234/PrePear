package com.example.prepear;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewRecipeActivity extends AppCompatActivity {
    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView preparationTimeTextView;
    private TextView numberOfServingsTextView;
    private TextView recipeCategoryTextView;
    private TextView commentsTextView;
    private ListView ingredientInRecipeListView;
    private Button editButton;
    private Button deleteButton;
    private Button returnButton;
    private ArrayAdapter<IngredientInRecipe> ingredientInRecipeArrayAdapter;
    private ArrayList<IngredientInRecipe> ingredientInRecipeDataList;
    private Recipe viewedRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        final String TAG = "Recipes";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Recipes");

        imageImageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.title_TextView);
        preparationTimeTextView = findViewById(R.id.preparation_time_TextView);
        numberOfServingsTextView = findViewById(R.id.number_of_servings_TextView);
        recipeCategoryTextView = findViewById(R.id.recipe_category_TextView);
        commentsTextView = findViewById(R.id.comments_TextView);
        ingredientInRecipeListView = findViewById(R.id.ingredient_in_recipe_ListView);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        returnButton = findViewById(R.id.return_button);

        viewedRecipe = (Recipe) getIntent().getSerializableExtra("viewed recipe");
        Glide.with(ViewRecipeActivity.this)
                .load(viewedRecipe.getImageURI()).into(imageImageView);
        titleTextView.setText(viewedRecipe.getTitle());
        preparationTimeTextView.setText(viewedRecipe.getPreparationTime().toString());
        numberOfServingsTextView.setText(viewedRecipe.getNumberOfServings().toString());
        recipeCategoryTextView.setText(viewedRecipe.getRecipeCategory());
        commentsTextView.setText(viewedRecipe.getComments());


        ingredientInRecipeDataList = viewedRecipe.getListOfIngredients();
        ingredientInRecipeArrayAdapter = new CustomIngredientInRecipeList(this, ingredientInRecipeDataList);
        ingredientInRecipeListView.setAdapter(ingredientInRecipeArrayAdapter);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(ViewRecipeActivity.this, AddEditRecipeActivity.class);
                switchActivityIntent.putExtra("viewed recipe", viewedRecipe);
                switchActivityIntent.putExtra("calling activity", "2");
                startActivity(switchActivityIntent);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.document(viewedRecipe.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                Intent returnIntent = new Intent();
                returnIntent.putExtra("delete recipe", viewedRecipe);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }
}
