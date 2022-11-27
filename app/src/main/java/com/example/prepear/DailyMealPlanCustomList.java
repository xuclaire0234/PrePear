/**
 * Classname: DailyMealPlanCustomList
 * Version Information: 2.0.0
 * Date: 11/17/2022
 * Author: Shihao Liu, Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * This class defines the custom daily meal plan list that shows the listview of all meals being planned
 * for a specific date.
 */
public class DailyMealPlanCustomList extends ArrayAdapter<Meal> {
    // initializes class attributes
    private ArrayList<Meal> mealsInOneDailyPlan;
    private Context context;
    private FirebaseFirestore db;
    private DailyMealPlan clickedDailyMealPlan;

    /**
     * This method constructs the new daily meal plan custom list.
     * @param contextParameter the context of the custom list
     * @param mealsParameter the list of meals in the clicked daily meal plan
     * @param clickedDailyMealPlan the clicked daily meal plan
     */
    public DailyMealPlanCustomList(Context contextParameter,
                                   ArrayList<Meal> mealsParameter, DailyMealPlan clickedDailyMealPlan) {
        super(contextParameter, 0, mealsParameter);
        this.context = contextParameter;
        this.mealsInOneDailyPlan = mealsParameter;
        db = FirebaseFirestore.getInstance();
        this.clickedDailyMealPlan = clickedDailyMealPlan;
    }

    /**
     * This method creates the list view.
     * @param position the position of meals
     * @param convertView the convert view
     * @param parent the parent of the view
     * @return the created view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView; // get the reference to the current convertView object

        if (view == null) {
            // if the convertView holds nothing, then inflate the ingredient_info.xml
            view = LayoutInflater.from(context).inflate(R.layout.daily_meal_plan_content, parent, false);
        }

        // connects the layouts to the activity
        TextView mealTitle = view.findViewById(R.id.meal_name_textView);
        TextView mealScale = view.findViewById(R.id.customized_number);
        Button selectTimeButton = view.findViewById(R.id.edit_time_button);
        ImageView mealPicture = view.findViewById(R.id.meal_imageView);
        DatabaseController databaseController = new DatabaseController();

        // sets up the view for each meal inside the selected daily meal plan
        Meal meal = mealsInOneDailyPlan.get(position); // use position to locate the current meal item on Daily Meal Plan ListView
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
                            // sets the current meal item's name TextView and customized amount TextView
                            String mealName = (String) documentSnapshot.getData().get("description");
                            mealTitle.setText(mealName);
                            mealScale.setText("Amount: " + (meal.getCustomizedAmount()));

                            // displays the current meal item's image/icon based on its meal type
                            int mealIconCode = ((Long)documentSnapshot.getData().get("icon code")).intValue();
                            mealPicture.setImageResource(mealIconCode);

                            // sets the user defined time to eat current meal to the select time button
                            if (meal.getEatHour() == 24) {
                                selectTimeButton.setText("Select Time");
                            } else {
                                selectTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", meal.getEatHour(), meal.getEatMinute()));
                            }
                        } else {
                            for (Meal eachMeal: mealsInOneDailyPlan) {
                                if (Objects.equals(eachMeal.getMealID(), meal.getMealID())){
                                    databaseController.deleteMealFromDailyMealPlan(context, clickedDailyMealPlan, meal);
                                    mealsInOneDailyPlan.remove(eachMeal);
                                    notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        } else if (Objects.equals(meal.getMealType(), "Recipe")) { // if meal type is a recipe
            mealDocRef = db.collection("Recipes").document(mealDocumentID);
            mealDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            String mealName = (String) documentSnapshot.getData().get("Title");
                            mealTitle.setText(mealName);
                            mealScale.setText("# of Servings: " + (meal.getCustomizedNumberOfServings()));

                            // displays the current meal item's image/icon based on its meal type
                            Glide.with(getContext())
                                    .load((String) documentSnapshot.getData().get("Image URI")).into(mealPicture);
                            mealPicture.setVisibility(View.VISIBLE);

                            // sets the user defined time to eat current meal to the select time button
                            if (meal.getEatHour() == 24) {
                                selectTimeButton.setText("Select Time");
                            } else {
                                selectTimeButton.setText(String.format(Locale.getDefault(),
                                        "%02d:%02d", meal.getEatHour(), meal.getEatMinute()));
                            }
                        } else {
                            for (Meal eachMeal: mealsInOneDailyPlan) {
                                if (Objects.equals(eachMeal.getMealID(), meal.getMealID())){
                                    mealsInOneDailyPlan.remove(eachMeal);
                                    databaseController.deleteMealFromDailyMealPlan(context, clickedDailyMealPlan, meal);
                                    notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }

        // sets up the select time button for displaying time picker after clicking it
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initializes time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        v.getRootView().getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                // initialize hour and minute
                                meal.setEatHour(selectedHour);
                                meal.setEatMinute(selectedMinute);
                                String time = selectedHour + ":" + selectedMinute;  // stores hour and minute into string

                                // initializes 24 hours time format
                                SimpleDateFormat format24 = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = format24.parse(time);
                                    selectTimeButton.setText(format24.format(date));    // sets selected time on button text view
                                    databaseController.addEditMealToDailyMealPlan(context, clickedDailyMealPlan, meal); // update database of the meal

                                    // resorts the daily meal plan list view by time picked by user through comparing hours and minutes
                                    Collections.sort(mealsInOneDailyPlan, new Comparator<Meal>() {
                                        @Override
                                        public int compare(Meal meal1, Meal meal2) {
                                            String time1 = String.format(Locale.getDefault(), "%02d:%02d", meal1.getEatHour(), meal1.getEatMinute());
                                            String time2 = String.format(Locale.getDefault(), "%02d:%02d", meal2.getEatHour(), meal2.getEatMinute());
                                            return time1.compareTo(time2);
                                            /*
                                            if (meal1.getEatHour().equals(meal2.getEatHour())) {
                                                return meal1.getEatMinute().compareTo(meal2.getEatMinute());
                                            } else {
                                                return meal1.getEatHour().compareTo(meal2.getEatHour());
                                            }

                                             */
                                        }
                                    });
                                    notifyDataSetChanged();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 24, 0, true
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));   // sets transparent background
                timePickerDialog.updateTime(meal.getEatHour(), meal.getEatMinute());    // displays previous selected time
                timePickerDialog.show();    // shows dialog
            }
        });

        return view; // return this view
    }

    /**
     * This method updates the information of a meal.
     * @param updatedMeal the meal to be updated
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