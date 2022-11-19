package com.example.prepear;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class DailyMealPlanCustomList extends ArrayAdapter<Meal> {
    private ArrayList<Meal> mealsInOneDailyPlan; // contains
    private Context context;
    private FirebaseFirestore db;


    /* constructor comment */
    public DailyMealPlanCustomList(Context contextParameter,
                                   ArrayList<Meal> mealsParameter) {
        super(contextParameter, 0, mealsParameter);
        this.context = contextParameter;
        this.mealsInOneDailyPlan = mealsParameter;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView; // get the reference to the current convertView object

        if (view == null) {
            // if the convertView holds nothing, then inflate the ingredient_info.xml
            view = LayoutInflater.from(context).inflate(R.layout.daily_meal_plan_content, parent, false);
        }

        Meal meal = mealsInOneDailyPlan.get(position); // use position to locate the current meal item on Daily Meal Plan ListView
        TextView mealTitle = view.findViewById(R.id.meal_name_textView);

        ImageView mealPicture = view.findViewById(R.id.meal_imageView);
        String mealDocumentID = meal.getDocumentID();
        DocumentReference mealDocRef;
        if (Objects.equals(meal.getMealType(), "IngredientInStorage")) { // if meal type is an in-storage ingredient
            mealDocRef = db.collection("Ingredient Storage").document(mealDocumentID);
            mealDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    // On below part: set the current meal item's name TextView
                    String mealName = (String) documentSnapshot.get("description");
                    mealTitle.setText(mealName);
                    // On below part: display the current meal item's image/icon based on its meal type
                    int mealIconCode = (Integer) documentSnapshot.get("icon code");
                    mealPicture.setImageResource(mealIconCode);
                    mealPicture.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        } else if (Objects.equals(meal.getMealType(), "Recipe")) { // if
            mealDocRef = db.collection("Recipes").document(mealDocumentID);
            mealDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    // On below part: set the current meal item's name TextView
                    String mealName = (String) documentSnapshot.get("Title");
                    mealTitle.setText(mealName);
                    // On below part: display the current meal item's image/icon based on its meal type
                    String mealImageURI = (String) documentSnapshot.get("icon code");
                    mealPicture.setImageURI(Uri.parse(mealImageURI));
                    mealPicture.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        }
        return view; // return this view
    }


}