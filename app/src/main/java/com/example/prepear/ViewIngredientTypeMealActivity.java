/**
 * Classname: ViewIngredientTypeMealActivity
 * Version Information: 1.0.0
 * Date: 11/17/2022
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class defines the view ingredient type meal activity that allows user to view details of
 * a specific ingredient type meal that is inside the specific daily meal plan.
 */
public class ViewIngredientTypeMealActivity extends AppCompatActivity {
    private ImageView ingredientIconImageView;
    private TextView briefDescriptionTextView;
    private TextView bestBeforeDateTextView;
    private TextView locationTextView;
    private EditText amountEditText;
    private TextView unitTextView;
    private TextView ingredientCategoryTextView;
    private Button deleteButton;
    private Button commitButton;
    private Meal viewedMeal;
    private Double originalAmount;
    private Double originalCustomizedAmount;

    /**
     * This method creates the ViewIngredientTypeMealActivity.
     * @param savedInstanceState a {@link Bundle} object that can be used to recreate the activity and load all data from it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ingredient_type_meal);

        // connects the layout with the views and buttons
        ingredientIconImageView = findViewById(R.id.ingredient_icon);
        briefDescriptionTextView = findViewById(R.id.brief_description_text_view);
        bestBeforeDateTextView = findViewById(R.id.best_before_date_text_view);
        locationTextView = findViewById(R.id.location_text_view);
        amountEditText = findViewById(R.id.amount_edit_text);
        unitTextView = findViewById(R.id.unit_text_view);
        ingredientCategoryTextView = findViewById(R.id.ingredient_category_text_view);
        deleteButton = findViewById(R.id.delete_button);
        commitButton = findViewById(R.id.commit_button);

        /* gets the information of the specific ingredient being clicked inside ViewDailyMealPlanActivity
        and display them on the screen */
        viewedMeal = (Meal) getIntent().getSerializableExtra("viewed meal");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Ingredient Storage").document(viewedMeal.getDocumentID());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ingredientIconImageView.setImageResource(((Long) document.getData().get("icon code")).intValue());
                        briefDescriptionTextView.setText((String) document.getData().get("description"));
                        bestBeforeDateTextView.setText((String) document.getData().get("bestBeforeDate"));
                        locationTextView.setText((String) document.getData().get("location"));
                        originalAmount = (Double) document.getData().get("amount");  // stores the original amount
                        unitTextView.setText((String) document.getData().get("unit"));
                        ingredientCategoryTextView.setText((String) document.getData().get("category"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // sets up the amount edit text based on customized scaling number
        originalCustomizedAmount = viewedMeal.getCustomizedAmount();
        amountEditText.setText(originalCustomizedAmount.toString());

        /* sets delete button to delete the viewing ingredient type meal after clicking it, and then return
        to the ViewDailyMealPlanActivity */
        deleteButton.setOnClickListener((View v) -> {
            // return to the calling ViewDailyMealPlanActivity
            Intent returnIntent = new Intent();
            returnIntent.putExtra("action", "delete meal");
            returnIntent.putExtra("mealToDelete", viewedMeal);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        /* sets commit button to save the customized amount of the viewing ingredient type meal after clicking it,
        and then return to the ViewDailyMealPlanActivity */
        commitButton.setOnClickListener((View v) -> {
            Intent returnIntent = new Intent();
            String currentCustomizedAmount = amountEditText.getText().toString();

            // checks if the customized amount changed or not, and set result based on it, then return
            if (currentCustomizedAmount.equals(originalCustomizedAmount.toString())) {
                setResult(Activity.RESULT_CANCELED, returnIntent);
            } else {
                viewedMeal.setCustomizedAmount(Double.parseDouble(currentCustomizedAmount));
                returnIntent.putExtra("action", "update meal");
                returnIntent.putExtra("mealToUpdate", viewedMeal);
                setResult(Activity.RESULT_OK, returnIntent);
            }
            finish();
        });
    }
}