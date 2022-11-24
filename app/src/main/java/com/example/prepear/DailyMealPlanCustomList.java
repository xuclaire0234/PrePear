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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DailyMealPlanCustomList extends ArrayAdapter<Meal> {
    private ArrayList<Meal> mealsInOneDailyPlan; // contains
    private Context context;
    private FirebaseFirestore db;


    // class constructor
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
        TextView mealScale = view.findViewById(R.id.customized_number);

        ImageView mealPicture = view.findViewById(R.id.meal_imageView);
        String mealDocumentID = meal.getDocumentID();
        DocumentReference mealDocRef;
        if (Objects.equals(meal.getMealType(), "IngredientInStorage")) { // if meal type is an in-storage ingredient
            mealDocRef = db.collection("Ingredient Storage").document(mealDocumentID);
            // On below part: retrieve the data (detailed information) of a Meal object
            mealDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            String mealName = (String) documentSnapshot.getData().get("description");
                            mealTitle.setText(mealName);
                            int mealIconCode = ((Long)documentSnapshot.getData().get("icon code")).intValue();
                            mealPicture.setImageResource(mealIconCode);
                            mealScale.setText("Amount: " + (meal.getCustomizedAmount()));
                        }
                    }
                }
            });
        } else if (Objects.equals(meal.getMealType(), "Recipe")) { // if
            mealDocRef = db.collection("Recipes").document(mealDocumentID);
            mealDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    // On below part: set the current meal item's name TextView
                    String mealName = (String) documentSnapshot.getData().get("Title");
                    mealTitle.setText(mealName);
                    mealScale.setText("# of Servings: " + (meal.getCustomizedNumberOfServings()));
                    // On below part: display the current meal item's image/icon based on its meal type
                    Glide.with(getContext())
                            .load((String) documentSnapshot.getData().get("Image URI")).into(mealPicture);
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

    /**
     * @param updatedMeal The Meal object that will be updated
     */
    public void updateMealInfo(Meal updatedMeal) {
        for (Meal eachMeal: mealsInOneDailyPlan) {
            if (Objects.equals(eachMeal.getDocumentID(), updatedMeal.getDocumentID())){
                if (Objects.equals(eachMeal.getMealType(), "IngredientInStorage")) {
                    eachMeal.setCustomizedAmount(updatedMeal.getCustomizedAmount());
                } else if (Objects.equals(eachMeal.getMealType(), "Recipe")) {
                    eachMeal.setCustomizedNumberOfServings(updatedMeal.getCustomizedNumberOfServings());
                }
            }
        }
    }
}