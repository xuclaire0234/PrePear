package com.example.prepear;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.prepear.databinding.ActivityAddEditIngredientBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.protobuf.Field;
import com.google.protobuf.StringValue;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {
    private Button confirm;
    private Button delete;
    private Button cancel;
    private LinearLayout instructions;
    private TextInputLayout descriptionView;
    private Spinner categoryView;
    private TextInputLayout otherCategory;
    private EditText dateView;
    private Spinner locationView;
    private TextInputLayout otherLocation;
    private EditText amountView;
    private Spinner unitView; // Spinner for picking ingredient unit
    private TextInputLayout otherUnit;
    private String bestBeforeDateString;  // best before date string
    private int iconCode;
    private ImageView ingredientIcon;
    private Button addIcon;
    private DatePickerDialog dialog; // create datePicker for best before date
    private CollectionReference collectionReferenceForInStorageIngredients;
    private final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        /* Set back button */
        ActionBar actionBar = getSupportActionBar();  // calling the action bar
        actionBar.setDisplayHomeAsUpEnabled(true);  // showing the back button in action bar

        /* connects the layout with the views and buttons */
        descriptionView = findViewById(R.id.brief_description);
        categoryView = (Spinner) findViewById(R.id.ingredient_category);
        otherCategory = findViewById(R.id.other_category);
        dateView = findViewById(R.id.bestBeforeDate);
        locationView = (Spinner) findViewById(R.id.ingredient_location);
        otherLocation = findViewById(R.id.other_location);
        amountView = findViewById(R.id.ingredient_amount);
        unitView = (Spinner) findViewById(R.id.ingredient_unit);
        otherUnit = findViewById(R.id.other_unit);
        instructions = findViewById(R.id.instruction);
        delete = findViewById(R.id.delete_button);
        confirm = findViewById(R.id.add_button);
        ingredientIcon = findViewById(R.id.ingredient_icon);
        addIcon = findViewById(R.id.add_icon_button);
        cancel = findViewById(R.id.cancel_button_ingredient);

        /* Prompt user to choose an icon from provided icon Activity */
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditIngredientActivity.this, ChooseIngredientIconActivity.class);

                //intent.putExtra("Choose icon", "1");
                startActivity(intent);
            }
        });



        /* set uer selected icon to the imageView ingredientIcon */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        try {
            if (extras.getString("Receive code").equals("1")) {
                String tag = getIntent().getExtras().getString("image tag");
                int res = getResources().getIdentifier(tag, "drawable", this.getPackageName());
                iconCode = res;
                ingredientIcon.setImageResource(res);
            }
        } catch (NullPointerException e) {

        }

        /* the activity will back to the viewIngredientInStorageActivity class if click CANCEL button */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(AddEditIngredientActivity.this, ViewIngredientStorageActivity.class);
                startActivity(intentBack);
            }
        });


        /* set date picker dialog */
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* create a date picker for the best before date of the ingredient */
                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH);
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(AddEditIngredientActivity.this, R.style.activity_date_picker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // On below parts:
                                // set the day of month , the month of year and the year value in the edit text
                                // if-conditional statement helps to regulate the format of yyyy-mm-dd.
                                if (monthOfYear < 9 && dayOfMonth < 10) {
                                    bestBeforeDateString = year + "-" + "0" + (monthOfYear + 1) +
                                            "-" + "0" + dayOfMonth;
                                } else if (dayOfMonth < 10) {
                                    bestBeforeDateString = year + "-" + (monthOfYear + 1) +
                                            "-" + "0" + dayOfMonth;
                                } else if (monthOfYear < 9) {
                                    bestBeforeDateString = year + "-" + "0" + (monthOfYear + 1) +
                                            "-" + dayOfMonth;
                                } else {
                                    bestBeforeDateString = year + "-" + (monthOfYear + 1) +
                                            "-" + dayOfMonth;
                                }
                                dateView.setText(bestBeforeDateString);
                            }
                        }, currentYear, currentMonth, currentDay);
                dialog.show();
                /* Temporarily remove keyboards before displaying the dialog */
                removeKeyboard();
            }
        });
        setTextWatchers();
        // On below: create array adapter for the spinner from array in strings.xml file
        ArrayAdapter adapterForCategories = ArrayAdapter.createFromResource(this,
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        // On below:  specify the layout the adapter should use to display the spinner list
        adapterForCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryView.setAdapter(adapterForCategories); // set spinner adapter to be the array list
        categoryView.setOnTouchListener(new View.OnTouchListener() {
            /*Temporarily remove keyboards before displaying spinner */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeKeyboard();  // remove all keyboards when spinner is on display
                return false;
            }
        });
        categoryView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryView.getSelectedItem().toString().trim();
                String location = locationView.getSelectedItem().toString().trim();
                String unit = unitView.getSelectedItem().toString().trim();
                if (category.equals("Other")) {
                    otherCategory.setVisibility(View.VISIBLE);
                    if (location.equals("Other") && unit.equals("Other")) {
                        instructions.setVisibility(View.GONE);
                    }
                } else {
                    otherCategory.setVisibility(View.GONE);
                    instructions.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // On below: create array adapter for the spinner from array in strings.xml file
        ArrayAdapter adapterForLocation = ArrayAdapter.createFromResource(this, R.array.locations,
                android.R.layout.simple_spinner_item);
        // On below: specify the layout the adapter should use to display the spinner list
        adapterForLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationView.setAdapter(adapterForLocation);  // set spinner adapter to be the array list
        locationView.setOnTouchListener(new View.OnTouchListener() {
            /* Temporarily remove keyboards before displaying spinner */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeKeyboard();  // remove all keyboards when spinner is on display
                return false;
            }
        });
        locationView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryView.getSelectedItem().toString().trim();
                String location = locationView.getSelectedItem().toString().trim();
                String unit = unitView.getSelectedItem().toString().trim();
                if (location.equals("Other")) {
                    otherLocation.setVisibility(View.VISIBLE);
                    if (category.equals("Other") && unit.equals("Other")) {
                        instructions.setVisibility(View.GONE);
                    }
                } else {
                    otherLocation.setVisibility(View.GONE);
                    instructions.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // create array adapter for the spinner from array in strings.xml file
        ArrayAdapter adapterForUnits = ArrayAdapter.createFromResource(this, R.array.units,
                android.R.layout.simple_spinner_item);
        adapterForUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitView.setAdapter(adapterForUnits);
        unitView.setOnTouchListener(new View.OnTouchListener() {
            /*Temporarily remove keyboards before displaying spinner*/
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeKeyboard();  // remove all keyboards when spinner is on display
                return false;
            }
        });
        unitView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryView.getSelectedItem().toString().trim();
                String location = locationView.getSelectedItem().toString().trim();
                String unit = unitView.getSelectedItem().toString().trim();
                if (unit.equals("Other")) {
                    otherUnit.setVisibility(View.VISIBLE);
                    if (location.equals("Other") && category.equals("Other")) {
                        instructions.setVisibility(View.GONE);
                    }
                } else {
                    otherUnit.setVisibility(View.GONE);
                    instructions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        try {
            if (getIntent().getStringExtra("Add or Edit").equals("1")) {
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* If the user is adding a new ingredient. */
                        String description = descriptionView.getEditText().getText().toString().trim();
                        String category = categoryView.getSelectedItem().toString().trim();
                        if (category.equals("Other")) {
                            category = otherCategory.getEditText().getText().toString().trim();
                        }
                        String date = dateView.getText().toString().trim();
                        String location = locationView.getSelectedItem().toString().trim();
                        if (location.equals("Other")) {
                            location = otherLocation.getEditText().getText().toString().trim();
                        }
                        String unit = unitView.getSelectedItem().toString().trim();
                        if (unit.equals("Other")) {
                            unit = otherUnit.getEditText().getText().toString().trim();
                        }
                        String amount = amountView.getText().toString().trim();

                        if (validateInput()) {
                            // key: value pair as a element in HashMap/Map
                            Date dateTimeNow = new Date();
                            String documentId = DATEFORMAT.format(dateTimeNow);
                            IngredientInStorage ingredientToAdd = new IngredientInStorage(description,
                                    category, date, location, amount, unit, documentId,iconCode);
                            DatabaseController database = new DatabaseController();
                            database.addIngredientToIngredientStorage(AddEditIngredientActivity.this, ingredientToAdd);
                            /* return the new ingredient to be added to the list adapter */
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IngredientToAdd", ingredientToAdd);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                });
            } else {
                /* user wants to edit or delete an ingredient */
                IngredientInStorage ingredientToEdit = (IngredientInStorage) getIntent().getSerializableExtra("ingredientInStorage");
                /* set the previous values of the text fields and spinners */
                descriptionView.getEditText().setText(ingredientToEdit.getBriefDescription()); //  set ingredient description
                String category = ingredientToEdit.getIngredientCategory();
                List<String> categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.ingredient_categories)));
                if (categories.contains(category)) {
                    categoryView.setSelection(adapterForCategories
                            .getPosition(ingredientToEdit.getIngredientCategory())); // set the category spinner to the right item
                } else {
                    categoryView.setSelection(adapterForCategories.getPosition("Other"));
                    otherCategory.setVisibility(View.VISIBLE);
                    otherCategory.getEditText().setText(category);
                }
                dateView.setText(ingredientToEdit.getBestBeforeDate()); // set the best before date
                locationView.setSelection(adapterForLocation
                        .getPosition(ingredientToEdit.getLocation())); // set the location spinner to the right item
                String location = ingredientToEdit.getLocation();
                List<String> locations = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.locations)));
                if (locations.contains(location)) {
                    locationView.setSelection(adapterForLocation
                            .getPosition(ingredientToEdit.getLocation())); // set the category spinner to the right item
                } else {
                    locationView.setSelection(adapterForLocation.getPosition("Other"));
                    otherLocation.setVisibility(View.VISIBLE);
                    otherLocation.getEditText().setText(location);
                }
                amountView.setText(String.valueOf(ingredientToEdit.getAmountValue())); // set the amount of the ingredient
                unitView.setSelection(adapterForUnits
                        .getPosition(ingredientToEdit.getUnit())); // set the unit spinner to the right item
                String unit = ingredientToEdit.getUnit();
                List<String> units = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.units)));
                if (units.contains(unit)) {
                    unitView.setSelection(adapterForUnits
                            .getPosition(ingredientToEdit.getUnit())); // set the category spinner to the right item
                } else {
                    unitView.setSelection(adapterForUnits.getPosition("Other"));
                    otherUnit.setVisibility(View.VISIBLE);
                    otherUnit.getEditText().setText(unit);
                }
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseController databaseController = new DatabaseController();
                        databaseController.deleteIngredientFromIngredientStorage(AddEditIngredientActivity.this, ingredientToEdit);
                        /* return the ingredient to be deleted from the list adapter */
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("IngredientToDelete", ingredientToEdit);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String description = descriptionView.getEditText().getText().toString().trim();
                        String category = categoryView.getSelectedItem().toString().trim();
                        if (category.equals("Other")) {
                            category = otherCategory.getEditText().getText().toString().trim();
                        }
                        String date = dateView.getText().toString().trim();
                        String location = locationView.getSelectedItem().toString().trim();
                        if (location.equals("Other")) {
                            location = otherLocation.getEditText().getText().toString().trim();
                        }
                        String unit = unitView.getSelectedItem().toString().trim();
                        if (unit.equals("Other")) {
                            unit = otherUnit.getEditText().getText().toString().trim();
                        }
                        String amount = amountView.getText().toString().trim();
                        double amountValue = Double.parseDouble(amount);
                        if (validateInput()) {
                            /* set the same ingredient object with the new user input */
                            ingredientToEdit.setBriefDescription(description);
                            ingredientToEdit.setIngredientCategory(category);
                            ingredientToEdit.setBestBeforeDate(date);
                            ingredientToEdit.setAmountValue(amountValue);
                            ingredientToEdit.setAmountString(amount);
                            ingredientToEdit.setUnit(unit);
                            ingredientToEdit.setLocation(location);
                            // on below:
                            DatabaseController database = new DatabaseController();
                            database.editIngredientInIngredientStorage(AddEditIngredientActivity.this, ingredientToEdit);
                            Log.d("amount in activity", String.valueOf(ingredientToEdit.getAmountValue()));
                            ViewIngredientStorageActivity activity = new ViewIngredientStorageActivity();

                            /* return ingredient to be edited on the list adapter */
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IngredientToEdit", ingredientToEdit);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                });
            }
        } catch (NullPointerException e) {
        }
    }
    private void setTextWatchers() {
        descriptionView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (descriptionView.getEditText().getText().length() > 100)  // reached input length limit
                {
                    descriptionView.getEditText().setTextColor(Color.RED);  // change color to red
                    descriptionView.setError("Name is too long!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (descriptionView.getEditText().getText().length() <= 100)  // if the number of characters is reduced
                {
                    descriptionView.getEditText().setTextColor(Color.BLACK);  // change back the color to black
                    descriptionView.setError(null);
                }
            }
        });
        otherUnit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otherUnit.getEditText().getText().length() > 100)  // reached input length limit
                {
                    otherUnit.getEditText().setTextColor(Color.RED);  // change color to red
                    otherUnit.setError("Unit is too long!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otherUnit.getEditText().getText().length() <= 100)  // if the number of characters is reduced
                {
                    otherUnit.getEditText().setTextColor(Color.BLACK);  // change back the color to black
                    otherUnit.setError(null);
                }
            }
        });
        otherCategory.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otherCategory.getEditText().getText().length() > 100)  // reached input length limit
                {
                    otherCategory.getEditText().setTextColor(Color.RED);  // change color to red
                    otherCategory.setError("Category is too long!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otherCategory.getEditText().getText().length() <= 100)  // if the number of characters is reduced
                {
                    otherUnit.getEditText().setTextColor(Color.BLACK);  // change back the color to black
                    otherCategory.setError(null);
                }
            }
        });
        otherLocation.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otherLocation.getEditText().getText().length() > 100)  // reached input length limit
                {
                    otherLocation.getEditText().setTextColor(Color.RED);  // change color to red
                    otherLocation.setError("Location is too long!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otherLocation.getEditText().getText().length() <= 100)  // if the number of characters is reduced
                {
                    otherLocation.getEditText().setTextColor(Color.BLACK);  // change back the color to black
                    otherLocation.setError(null);
                }
            }
        });
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method removes all present soft keyboards and is used when user clicks on one of the spinners
     */
    private void removeKeyboard() {
        /* get the input method manager which manages interaction within the system.
           get System service is used to access Android system-level services like the keyboard */
        InputMethodManager inputMethodManager =
                (InputMethodManager) AddEditIngredientActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Hide the soft keyboards associated with description and amount edit text fields
        inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(amountView.getWindowToken(), 0);
    }

    public boolean validateInput() {
        Boolean valid = true;
        String description = descriptionView.getEditText().getText().toString().trim();
        String category = categoryView.getSelectedItem().toString().trim();
        String location = locationView.getSelectedItem().toString().trim();
        String unit = unitView.getSelectedItem().toString().trim();
        String date = dateView.getText().toString().trim();
        String amount = amountView.getText().toString().trim();
        if (category.isEmpty() || date.isEmpty() || location.isEmpty() || amount.isEmpty() || unit.isEmpty()){
            CharSequence text = "Error, Some Fields Are Empty!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            valid = false;
        } else if (amount.equals("0")) {
            CharSequence text = "Error, Amount can't be zero!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (description.isEmpty()) {
            descriptionView.setError("Field can't be empty!");
            valid = false;
        } else if (description.length() > 100) {
            valid = false;
        }
        if (category.equals("Other")){
            String otherCategoryChoice = otherCategory.getEditText().getText().toString().trim();
            if (otherCategoryChoice.isEmpty()){
                otherCategory.setError("Field can't be empty!");
                valid = false;
            } else if(otherCategoryChoice.length() > 100){
                valid = false;
            }
        }
        if (location.equals("Other")) {
            String otherLocationChoice = otherLocation.getEditText().getText().toString().trim();
            if (otherLocationChoice.isEmpty()) {
                otherLocation.setError("Field can't be empty!");
                valid = false;
            } else if(otherLocationChoice.length() > 100){
                valid = false;
            }
        }
        if (unit.equals("Other")) {
            String otherUnitChoice = otherUnit.getEditText().getText().toString().trim();
            if (otherUnitChoice.isEmpty()) {
                otherUnit.setError("Field can't be empty!");
                valid = false;
            } else if(otherUnitChoice.length() > 100){
                valid = false;
            }
        }
        return valid;
    }


}