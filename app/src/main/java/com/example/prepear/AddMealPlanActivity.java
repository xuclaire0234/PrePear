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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.example.prepear.ui.Recipe.RecipeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class AddMealPlanActivity extends AppCompatActivity implements View.OnClickListener, IngredientFragment.IngredientOnCallbackReceived, RecipeFragment.RecipeOnCallbackReceived{
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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = "Error, Please Finish Adding This Ingredient Or Click Cancel!";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return to view fragment
                // set cancel result
                finish(); // exit activity
                Intent intentBack = new Intent();
                setResult(Activity.RESULT_CANCELED, intentBack);
            }
        });

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
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
        // On below line: temporarily remove keyboards before displaying the dialog
        removeKeyboard();
    }

    @Override
    public void addIngredientTypeMeal(IngredientInStorage selectedIngredient) {
        setContentView(R.layout.activity_add_meal_plan);
        amountEditText = findViewById(R.id.amount);
        amountLayout = findViewById(R.id.amount_layout);
        numberOfServingsLayout = findViewById(R.id.number_of_servings_layout);
        startDateView = findViewById(R.id.start_date);
        endDateView = findViewById(R.id.end_date);
        ingredientButton = findViewById(R.id.ingredient_radioButton);
        recipeButton = findViewById(R.id.recipe_radioButton);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        ingredientButton.setChecked(true);
        if (startDate != null){
            startDateView.setText(startDate);
            startDateView.setOnClickListener(this);

        }else{
            startDateView.setOnClickListener(this);
        }
        if (endDate != null){
            endDateView.setText(endDate);
            endDateView.setOnClickListener(this);

        }else{
            endDateView.setOnClickListener(this);
        }
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeButton.setChecked(false);
                CharSequence text = "Error, Please Finish Adding This Ingredient Or Click Cancel!";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
        if (selectedIngredient != null) {
            amountLayout.setVisibility(View.VISIBLE);
            numberOfServingsLayout.setVisibility(View.GONE);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String amount = amountEditText.getText().toString().trim();
                    if (amount.isEmpty()) {
                        CharSequence text = "Error, Please Enter an Amount!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }else if(startDate.isEmpty() || endDate.isEmpty()){
                        CharSequence text = "Error, Please Finish Selecting The Dates!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }else if (amount.equals("0")){
                        CharSequence text = "Error, Please Enter An Amount Greater Than Zero!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    } else {
                        Intent intentBack = new Intent();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            // convert date strings to local dates
                            Date start = sdf.parse(startDate);
                            LocalDate localStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            Date end = sdf.parse(endDate);
                            LocalDate localEnd = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            Integer counter = 0;  // counter for the number of Meal objects
                            if(localEnd.isBefore(localStart)){
                                CharSequence text = "Error, End Date Must Come After Start Date!";
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                            }else {
                                for (LocalDate date = localStart; date.isBefore(localEnd) || date.isEqual(localEnd); date = date.plusDays(1)) {
                                    // create a Meal object for each DailyMealPlan object
                                    Meal firstMeal = new Meal(selectedIngredient.getDocumentId(), Integer.parseInt(amount), "IngredientInStorage");
                                    counter += 1;
                                    // create the DailyMealPlan object
                                    DailyMealPlan newMeal = new DailyMealPlan(date.toString(), firstMeal);
                                    // send DailyMealPlan object to MealFragment
                                    intentBack.putExtra("meal" + counter, newMeal);
                                }
                                intentBack.putExtra("counter", counter);
                                setResult(Activity.RESULT_OK, intentBack);
                                finish(); // exit activity
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // return to view fragment
                    // set cancel result
                    finish(); // exit activity
                    Intent intentBack = new Intent();
                    setResult(Activity.RESULT_CANCELED, intentBack);
                }
            });
        }else{
            // return to view fragment
            // set cancel result
            finish(); // exit activity
            Intent intentBack = new Intent();
            setResult(Activity.RESULT_CANCELED, intentBack);
        }
    }

    @Override
    public void addRecipeTypeMeal(Recipe selectedRecipe) {
        setContentView(R.layout.activity_add_meal_plan);
        numberOfServingsEditText = findViewById(R.id.number_of_servings);
        amountLayout = findViewById(R.id.amount_layout);
        numberOfServingsLayout = findViewById(R.id.number_of_servings_layout);
        startDateView = findViewById(R.id.start_date);
        endDateView = findViewById(R.id.end_date);
        ingredientButton = findViewById(R.id.ingredient_radioButton);
        numberOfServingsLayout =  findViewById(R.id.number_of_servings_layout);
        recipeButton = findViewById(R.id.recipe_radioButton);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        recipeButton.setChecked(true);
        if (startDate != null){
            startDateView.setText(startDate);
            startDateView.setOnClickListener(this);

        }else{
            startDateView.setOnClickListener(this);
        }
        if (endDate != null){
            endDateView.setText(endDate);
            endDateView.setOnClickListener(this);

        }else{
            endDateView.setOnClickListener(this);
        }
        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientButton.setChecked(false);
                CharSequence text = "Error, Please Finish Adding This Recipe Or Click Cancel!";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
        if (selectedRecipe != null) {
            amountLayout.setVisibility(View.GONE);
            numberOfServingsLayout.setVisibility(View.VISIBLE);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String numberOfServings = numberOfServingsEditText.getText().toString().trim();
                    if (numberOfServings.isEmpty()) {
                        CharSequence text = "Error, Please Enter The Number Of Servings!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    } else if(startDate.isEmpty() || endDate.isEmpty()){
                        CharSequence text = "Error, Please Finish Selecting The Dates!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }else if (numberOfServings.equals("0")){
                        CharSequence text = "Error, Please Enter a Number Of Servings Greater Than Zero!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }else {
                        Intent intentBack = new Intent();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            // convert date strings to local dates
                            Date start = sdf.parse(startDate);
                            LocalDate localStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            Date end = sdf.parse(endDate);
                            LocalDate localEnd = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            Integer counter = 0;  // counter for the number of Meal objects
                            if(localEnd.isBefore(localStart)){
                                CharSequence text = "Error, End Date Must Come After Start Date!";
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                            } else {
                                for (LocalDate date = localStart; date.isBefore(localEnd) || date.isEqual(localEnd); date = date.plusDays(1)) {
                                    // create a Meal object for each DailyMealPlan object
                                    Meal firstMeal = new Meal(selectedRecipe.getId(), Integer.parseInt(numberOfServings), "Recipe");
                                    // create the DailyMealPlan object
                                    counter += 1;
                                    DailyMealPlan newMeal = new DailyMealPlan(date.toString(), firstMeal);
                                    // send DailyMealPlan object to MealFragment
                                    intentBack.putExtra("meal" + counter, newMeal);
                                }
                                intentBack.putExtra("counter", counter);
                                setResult(Activity.RESULT_OK, intentBack);
                                finish(); // exit activity
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // exit activity
                    Intent intentBack = new Intent();
                    setResult(Activity.RESULT_CANCELED, intentBack);
                }
            });
        } else {
            // return to view fragment
            // set cancel result
            finish(); // exit activity
            Intent intentBack = new Intent();
            setResult(Activity.RESULT_CANCELED, intentBack);;
        }

    }
}
