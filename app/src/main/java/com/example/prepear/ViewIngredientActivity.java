package com.example.prepear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewIngredientActivity extends AppCompatActivity {

    private EditText editDescription;
    private EditText editAmount;
    private EditText editDate;
    private Spinner editLocation;
    private  Spinner editCategory;
    private  Spinner editUnit;
    private Button editButton;
    private Button deleteButton;
    private  Button cancelButton;
    private IngredientInStorage viewedIngredient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ingredient);

        final String TAG = "Ingredients";
        FirebaseFirestore dbForInStorageIngredients = FirebaseFirestore.getInstance();;
        final CollectionReference collectionReference = dbForInStorageIngredients.collection("Ingredient Storage");

        editDescription = findViewById(R.id.brief_description_edit);
        editAmount = findViewById(R.id.ingredient_amount_edit);
        editDate = findViewById(R.id.date_edit);
        editLocation = findViewById(R.id.location_edit);
        editCategory = findViewById(R.id.category_edit);
        editUnit = findViewById(R.id.ingredient_unit_edit);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        cancelButton = findViewById(R.id.cancel_button);

        editCategory.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        @SuppressLint("ResourceType") ArrayAdapter<String> adapterForCategories = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,R.array.categories );
        adapterForCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCategory.setAdapter(adapterForCategories);


        editLocation.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        @SuppressLint("ResourceType") ArrayAdapter<String> adapterForLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,R.array.locations);
        adapterForLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editLocation.setAdapter(adapterForLocation);

        editUnit.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        @SuppressLint("ResourceType") ArrayAdapter<String> adapterForUnit = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,R.array.units );
        adapterForUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUnit.setAdapter(adapterForUnit);


        viewedIngredient = (IngredientInStorage) getIntent().getSerializableExtra("viewed Ingredient");
//            Glide.with(AddEditIngredientActivity.this)
//                    .load(linkOfImage).into(imageImageView);
        editDescription.setText(viewedIngredient.getBriefDescription());
        editDate.setText(viewedIngredient.getBestBeforeDate());
        editLocation.setSelection(adapterForLocation.getPosition(viewedIngredient.getLocation()));
        editCategory.setSelection(adapterForCategories.getPosition(viewedIngredient.getIngredientCategory()));
        editAmount.setText((int) viewedIngredient.getAmount());
        editUnit.setSelection(adapterForUnit.getPosition(viewedIngredient.getUnit()));


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(ViewIngredientActivity.this, AddEditIngredientActivity.class);
                switchActivityIntent.putExtra("viewed ingredient", viewedIngredient);
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
                collectionReference.document(viewedIngredient.getId())
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
                                Log.w(TAG, "Fails to delete from database", e);
                            }
                        });

                Intent returnIntent = new Intent();
                returnIntent.putExtra("delete ingredient", viewedIngredient);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }
}