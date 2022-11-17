package com.example.prepear;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.example.prepear.ui.Recipe.RecipeFragment;

import org.checkerframework.checker.units.qual.A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class AddMealPlanActivity extends AppCompatActivity implements View.OnClickListener, IngredientFragment.OnCallbackReceived{
    private Integer LAUNCH_INGREDIENT_FRAGMENT = 1;
    private Integer LAUNCH_RECIPE_FRAGMENT = 2;
    private String startDate;
    private String endDate;
    private EditText startDateView;
    private EditText endDateView;
    private LinearLayout amountLayout;
    private EditText amountEditText;
    private LinearLayout numberOfServingsLayout;
    private EditText numberOfServingsEditText;
    private Button confirm;
    private Button cancel;
    private DatePickerDialog dialog; // create datePicker for best dates
    private RadioButton ingredientButton;
    private RadioButton recipeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_plan);

        startDateView = findViewById(R.id.start_date);
        endDateView = findViewById(R.id.end_date);
        amountLayout = findViewById(R.id.amount_layout);
        amountEditText = findViewById(R.id.amount);
        numberOfServingsLayout = findViewById(R.id.number_of_servings_layout);
        numberOfServingsEditText = findViewById(R.id.number_of_servings);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        ingredientButton = findViewById(R.id.ingredient_radioButton);
        recipeButton = findViewById(R.id.recipe_radioButton);


        // set date picker dialog
        startDateView.setOnClickListener(this);
        endDateView.setOnClickListener(this);
        // set click listener for radio buttons
        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientButton.setChecked(true);
                recipeButton.setChecked(false);
                FragmentTransaction transaction;
                transaction = getSupportFragmentManager().beginTransaction();
                FrameLayout fl = findViewById(android.R.id.content);
                fl.removeAllViews();
                transaction.replace(android.R.id.content, new IngredientFragment(), "selectIngredient");
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        });
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeButton.setChecked(true);
                ingredientButton.setChecked(false);
                // on below call activity for new in-storage ingredient
                FragmentTransaction transaction;
                transaction = getSupportFragmentManager().beginTransaction();
                FrameLayout fl = findViewById(android.R.id.content);
                fl.removeAllViews();
                transaction.replace(android.R.id.content, new RecipeFragment(), "selectRecipe");
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportFragmentManager().executePendingTransactions();

            }
        });

    }


    /**
     * This method removes all present soft keyboards and is used when user clicks on one of the spinners
     */
    private void removeKeyboard() {
        /* get the input method manager which manages interaction within the system.
         * get System service is used to access Android system-level services like the keyboard
         */
        InputMethodManager inputMethodManager =
                (InputMethodManager) AddMealPlanActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
        // On below part: Hide the soft keyboards associated with description and amount edit text fields
        inputMethodManager.hideSoftInputFromWindow(amountLayout.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(numberOfServingsLayout.getWindowToken(), 0);
    }

    @Override
    /**
     * This method updates the ingredient list view after the AddEditIngredientActivity concludes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_INGREDIENT_FRAGMENT) { // if user adds a new ingredient
            if (resultCode == Activity.RESULT_OK) {
                //get ingredient
                IngredientInStorage ingredientToAdd = (IngredientInStorage) data.getSerializableExtra("IngredientToAdd");
                /*numberOfServingsLayout.setVisibility(View.GONE);
                amountLayout.setVisibility(View.VISIBLE);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = amountEditText.getText().toString().trim();
                        if (amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()){
                            CharSequence text = "Error, Please Enter an Amount!";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Intent intent = new Intent(AddMealPlanActivity.this, MealPlanFragment.class);
                            intent.putExtra("New Meal Plan", "1");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                // convert date strings to local dates
                                Date start = sdf.parse(startDate);
                                LocalDate localStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                Date end = sdf.parse(endDate);
                                LocalDate localEnd = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                Integer counter = 1;  // counter for the number of Meal objects
                                for (LocalDate date = localStart; date.isEqual(localEnd); date = date.plusDays(1)){
                                    // create a Meal object for each DailyMealPlan object
                                    Meal firstMeal = new Meal(ingredientToAdd.getDocumentId(), 0, "IngredientInStorage");
                                    // create the DailyMealPlan object
                                    DailyMealPlan newMeal = new DailyMealPlan(date.toString(), firstMeal);
                                    // send DailyMealPlan object to MealFragment
                                    intent.putExtra("meal"+counter.toString(), newMeal);
                                    counter += 1;
                                }
                                intent.putExtra("counter", counter); // send the number of DailyMealPlan objects created
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            setResult(Activity.RESULT_OK, intent);
                            finish(); // exit activity
                        }
                    }
                });*/


            } else {
                setResult(Activity.RESULT_CANCELED);
                finish();

            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                amountLayout.setVisibility(View.GONE);
                numberOfServingsLayout.setVisibility(View.VISIBLE);

            }else{

            }
        }
    }

    @Override
    public void onClick(View v) {
        // On below part: create a date picker for the best before date of the ingredient
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        dialog = new DatePickerDialog(AddMealPlanActivity.this, R.style.activity_date_picker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        /* On below parts:
                         * set the day of month , the month of year and the year value in the edit text
                         * if-conditional statement helps to regulate the format of yyyy-mm-dd.
                         * */
                        if (monthOfYear < 9 && dayOfMonth < 10) {
                            if (v.getId() == R.id.start_date) {
                                startDate = year + "-" + "0" + (monthOfYear + 1) +
                                        "-" + "0" + dayOfMonth;
                            }else{
                                endDate = year + "-" + "0" + (monthOfYear + 1) +
                                        "-" + "0" + dayOfMonth;
                            }
                        } else if (dayOfMonth < 10) {
                            if (v.getId() == R.id.start_date) {
                                startDate = year + "-" + (monthOfYear + 1) +
                                        "-" + "0" + dayOfMonth;
                            }else{
                                endDate = year + "-" + (monthOfYear + 1) +
                                        "-" + "0" + dayOfMonth;
                            }
                        } else if (monthOfYear < 9) {
                            if (v.getId() == R.id.start_date) {
                                startDate = year + "-" + "0" + (monthOfYear + 1) +
                                        "-" + dayOfMonth;
                            }else{
                                endDate = year + "-" + "0" + (monthOfYear + 1) +
                                        "-" + dayOfMonth;
                            }
                        } else {
                            if (v.getId() == R.id.start_date) {
                                startDate = year + "-" + (monthOfYear + 1) +
                                        "-" + dayOfMonth;
                            }else{
                                endDate = year + "-" + (monthOfYear + 1) +
                                        "-" + dayOfMonth;
                            }
                        }
                        if (v.getId() == R.id.start_date) {
                            startDateView.setText(startDate);
                        }else{
                            endDateView.setText(endDate);
                        }
                    }
                }, currentYear, currentMonth, currentDay);
        dialog.show();
        // On below line: temporarily remove keyboards before displaying the dialog
        removeKeyboard();

    }

    @Override
    public void addIngredientTypeMeal(IngredientInStorage selectedIngredient) {

        numberOfServingsLayout.setVisibility(View.GONE);
        amountLayout.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountEditText.getText().toString().trim();
                if (amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()){
                    CharSequence text = "Error, Please Enter an Amount!";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
                else{
                    /*Intent intent = new Intent(AddMealPlanActivity.this, MealPlanFragment.class);
                    intent.putExtra("New Meal Plan", "1");*/
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Bundle bundle = new Bundle();

                        // convert date strings to local dates
                        Date start = sdf.parse(startDate);
                        LocalDate localStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        Date end = sdf.parse(endDate);
                        LocalDate localEnd = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        Integer counter = 1;  // counter for the number of Meal objects
                        for (LocalDate date = localStart; date.isEqual(localEnd); date = date.plusDays(1)){
                            // create a Meal object for each DailyMealPlan object
                            Meal firstMeal = new Meal(selectedIngredient.getDocumentId(), 0, "IngredientInStorage");
                            // create the DailyMealPlan object
                            DailyMealPlan newMeal = new DailyMealPlan(date.toString(), firstMeal);
                            // send DailyMealPlan object to MealFragment
                            bundle.putSerializable("meal"+counter, newMeal);
                            counter += 1;
                            }
                        bundle.putSerializable("counter",counter);
                        MealPlanFragment fragment = new MealPlanFragment();
                        fragment.setArguments(bundle);
                        } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    finish(); // exit activity
                }
            }
        });

    }
}
