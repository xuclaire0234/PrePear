/**/

package com.example.prepear;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class defines the add/edit ingredientInStorage activity that allows user to either add a new ingredient or
 * edit a existing recipe.
 */
public class AddEditIngredientActivity extends AppCompatActivity {
    private Button confirm; // confirm button after addition/edition of a in-storage ingredient
    private Button delete; // delete button for deleting the in-storage ingredient when editing it
    private TextInputLayout descriptionView; // for user to input ingredient's description
    private Spinner categoryView; // for user to input ingredient's other categories
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
    private final int ICON_REQUEST = 1;
    private DatePickerDialog dialog; // create datePicker for best before date
    private final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        // On below part: set back button
        ActionBar actionBar = getSupportActionBar();  // calling the action bar
        actionBar.setDisplayHomeAsUpEnabled(true);  // showing the back button in action bar

        // On below part: connect the layout with the views and buttons
        descriptionView = findViewById(R.id.brief_description);
        categoryView = (Spinner) findViewById(R.id.ingredient_category);
        otherCategory = findViewById(R.id.other_category);
        dateView = findViewById(R.id.bestBeforeDate);
        locationView = (Spinner) findViewById(R.id.ingredient_location);
        otherLocation = findViewById(R.id.other_location);
        amountView = findViewById(R.id.ingredient_amount);
        unitView = (Spinner) findViewById(R.id.ingredient_unit);
        otherUnit = findViewById(R.id.other_unit);
        delete = findViewById(R.id.delete_button);
        confirm = findViewById(R.id.add_button);
        ingredientIcon = findViewById(R.id.ingredient_icon);
        addIcon = findViewById(R.id.add_icon_button);

        // On below part: prompt user to choose an icon from provided icon Activity
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditIngredientActivity.this, ChooseIngredientIconActivity.class);
                startActivityForResult(intent,ICON_REQUEST);
            }
        });

        // On below part: set date picker dialog
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On below part: create a date picker for the best before date of the ingredient
                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH);
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(AddEditIngredientActivity.this, R.style.activity_date_picker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                /* On below parts:
                                 * set the day of month , the month of year and the year value in the edit text
                                 * if-conditional statement helps to regulate the format of yyyy-mm-dd.
                                 * */
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
                // On below line: temporarily remove keyboards before displaying the dialog
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
            // On below part: temporarily remove keyboards before displaying spinner
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
                // On below part: show edit text if option 'Other' is selected, otherwise hide edit text
                if (category.equals("Other")) {
                    otherCategory.setVisibility(View.VISIBLE);
                } else {
                    otherCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        // On below line: create array adapter for the spinner from array in strings.xml file
        ArrayAdapter adapterForLocation = ArrayAdapter.createFromResource(this, R.array.locations,
                android.R.layout.simple_spinner_item);
        // On below part: specify the layout the adapter should use to display the spinner list
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
                String location = locationView.getSelectedItem().toString().trim();
                // On below part: show edit text if option 'Other' is selected, otherwise hide edit text
                if (location.equals("Other")) {
                    otherLocation.setVisibility(View.VISIBLE);
                } else {
                    otherLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });


        // On below part: create array adapter for the spinner from array in strings.xml file
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
                String unit = unitView.getSelectedItem().toString().trim();
                // On below part: show edit text if option 'Other' is selected, otherwise hide edit text
                if (unit.equals("Other")) {
                    otherUnit.setVisibility(View.VISIBLE);
                } else {
                    otherUnit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        try {
            if (getIntent().getStringExtra("Add or Edit").equals("1")) {
                // If the user is adding a new ingredient
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // On below part: get user input
                        String description = descriptionView.getEditText().getText().toString().trim();
                        String category = categoryView.getSelectedItem().toString().trim();
                        if (category.equals("Other")) {
                            // On below line: get input from edit text when option 'Other' is selected
                            category = otherCategory.getEditText().getText().toString().trim();
                        }
                        String date = dateView.getText().toString().trim();
                        String location = locationView.getSelectedItem().toString().trim();
                        if (location.equals("Other")) {
                            // On below line: get input from edit text when option 'Other' is selected
                            location = otherLocation.getEditText().getText().toString().trim();
                        }
                        String unit = unitView.getSelectedItem().toString().trim();
                        if (unit.equals("Other")) {
                            // On below line: get input from edit text when option 'Other' is selected
                            unit = otherUnit.getEditText().getText().toString().trim();
                        }
                        String amount = amountView.getText().toString().trim();

                        if (validateInput()) {  // if user input is valid
                            // key: value pair as a element in HashMap/Map
                            Date dateTimeNow = new Date();
                            String documentId = DATEFORMAT.format(dateTimeNow);
                            IngredientInStorage ingredientToAdd = new IngredientInStorage(description,
                                    category, date, location, amount, unit, documentId, iconCode);
                            DatabaseController database = new DatabaseController();
                            database.addIngredientToIngredientStorage(AddEditIngredientActivity.this, ingredientToAdd);
                            // return the new ingredient to be added to the list adapter
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IngredientToAdd", ingredientToAdd);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish(); // exit activity
                        }
                    }
                });
            } else {
                // if user wants to edit or delete an ingredient
                int index = (int) getIntent().getSerializableExtra("index");
                IngredientInStorage ingredientToEdit = (IngredientInStorage) getIntent()
                        .getSerializableExtra("ingredientInStorage");  // get ingredient clicked on
                // set the previous values of the text fields and spinners
                int iconCodeEdit = ingredientToEdit.getIconCode();
                ingredientIcon.setImageResource(iconCodeEdit);
                descriptionView.getEditText().setText(ingredientToEdit.getBriefDescription());
                String category = ingredientToEdit.getIngredientCategory();  // get ingredient category
                /**
                 *  get the list of ingredient categories to check if the ingredient's category is among that list.
                 *  If it's in the list then show the option on the spinner,
                 *  otherwise set the spinner to display 'Other', and show the category on the edit text instead
                 */
                List<String> categories = new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.ingredient_categories)));
                if (categories.contains(category)) {
                    categoryView.setSelection(adapterForCategories
                            .getPosition(ingredientToEdit.getIngredientCategory()));
                } else {
                    categoryView.setSelection(adapterForCategories.getPosition("Other"));
                    otherCategory.setVisibility(View.VISIBLE);  // display the edit text
                    otherCategory.getEditText().setText(category);
                }
                dateView.setText(ingredientToEdit.getBestBeforeDate()); // set the best before date
                String location = ingredientToEdit.getLocation();
                /**
                 *  get the list of ingredient locations to check if the ingredient's location is among that list.
                 *  If it's in the list then show the option on the spinner,
                 *  otherwise set the spinner to display 'Other', and show the location on the edit text instead
                 */
                List<String> locations = new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.locations)));
                if (locations.contains(location)) {
                    locationView.setSelection(adapterForLocation
                            .getPosition(ingredientToEdit.getLocation()));
                } else {
                    locationView.setSelection(adapterForLocation.getPosition("Other"));
                    otherLocation.setVisibility(View.VISIBLE);  // display the edit text
                    otherLocation.getEditText().setText(location);
                }
                amountView.setText(String.valueOf(ingredientToEdit.getAmountValue())); // set the amount
                String unit = ingredientToEdit.getUnit();
                /**
                 *  get the list of ingredient unit to check if the ingredient's unit is among that list.
                 *  If it's in the list then show the option on the spinner,
                 *  otherwise set the spinner to display 'Other', and show the unit on the edit text instead
                 */
                List<String> units = new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.units)));
                if (units.contains(unit)) {
                    unitView.setSelection(adapterForUnits
                            .getPosition(ingredientToEdit.getUnit()));
                } else {
                    unitView.setSelection(adapterForUnits.getPosition("Other"));
                    otherUnit.setVisibility(View.VISIBLE);  // display the edit text
                    otherUnit.getEditText().setText(unit);
                }
                if(iconCode == 0 ){
                    iconCode = iconCodeEdit;
                }
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // On below part: delete ingredient from the data base
                        DatabaseController databaseController = new DatabaseController();
                        databaseController.deleteIngredientFromIngredientStorage(AddEditIngredientActivity.this, ingredientToEdit);
                        // On below part: return the ingredient to be deleted from the list adapter
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("IngredientToDelete", ingredientToEdit);
                        returnIntent.putExtra("index", index);  // index of ingredient to delete
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // On below: get the values of the text fields and spinners

                        String description = descriptionView.getEditText().getText().toString().trim();
                        String category = categoryView.getSelectedItem().toString().trim();
                        if (category.equals("Other")) {
                            // On below: get category from the edit text if the option 'Other' is selected
                            category = otherCategory.getEditText().getText().toString().trim();
                        }
                        String date = dateView.getText().toString().trim();
                        String location = locationView.getSelectedItem().toString().trim();
                        if (location.equals("Other")) {
                            // On below: get category from the edit text if the option 'Other' is selected
                            location = otherLocation.getEditText().getText().toString().trim();
                        }
                        String unit = unitView.getSelectedItem().toString().trim();
                        if (unit.equals("Other")) {
                            // On below: get category from the edit text if the option 'Other' is selected
                            unit = otherUnit.getEditText().getText().toString().trim();
                        }
                        if (validateInput()) {
                            String amount = amountView.getText().toString().trim();
                            double amountValue = Double.parseDouble(amount); // float value of amount
                            // On below part: set the same ingredient object with the new user input
                            ingredientToEdit.setBriefDescription(description);
                            ingredientToEdit.setIngredientCategory(category);
                            ingredientToEdit.setBestBeforeDate(date);
                            ingredientToEdit.setAmountValue(amountValue);
                            ingredientToEdit.setAmountString(amount);
                            ingredientToEdit.setUnit(unit);
                            ingredientToEdit.setLocation(location);
                            ingredientToEdit.setIconCode(iconCode);
                            // on below: edit the ingredient in the data base
                            DatabaseController database = new DatabaseController();
                            database.editIngredientInIngredientStorage(AddEditIngredientActivity.this, ingredientToEdit);
                            // On below part: return ingredient to be edited on the list adapter
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IngredientToEdit", ingredientToEdit);
                            returnIntent.putExtra("index", index);  // index of ingredient
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();  // exit activity
                        }
                    }
                });
            }
        } catch (NullPointerException e) {
        }
    }

    /**
     * This method sets text watchers for all edit texts in the activity checking if input exceeds
     * 100 characters. If it does, the text turns red and an error message is displayed under the
     * edit text
     */
    private void setTextWatchers() {
        descriptionView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (descriptionView.getEditText().getText().length() > 100)
                { // reached input length limit
                    descriptionView.getEditText().setTextColor(Color.RED);  // change color to red
                    descriptionView.setError("Name is too long!");  // display error message
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (descriptionView.getEditText().getText().length() <= 100)
                { // if the number of characters is reduced
                    // On below: change back the color to black and remove error message
                    descriptionView.getEditText().setTextColor(Color.BLACK);
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
                if (otherUnit.getEditText().getText().length() > 100)
                { // reached input length limit
                    otherUnit.getEditText().setTextColor(Color.RED);  // change color to red
                    otherUnit.setError("Unit is too long!");  // display error message
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otherUnit.getEditText().getText().length() <= 100)  // if the number of characters is reduced
                { // if the number of characters is reduced
                    // On below part: change back the color to black and remove error message
                    otherUnit.getEditText().setTextColor(Color.BLACK);
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
                if (otherCategory.getEditText().getText().length() > 100)
                {  // reached input length limit
                    otherCategory.getEditText().setTextColor(Color.RED);  // change color to red
                    otherCategory.setError("Category is too long!");  // display error message
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otherCategory.getEditText().getText().length() <= 100)
                { // if the number of characters is reduced
                    // On below part: change back the color to black and remove error message
                    otherCategory.getEditText().setTextColor(Color.BLACK);
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
                if (otherLocation.getEditText().getText().length() > 100)
                {  // reached input length limit
                    otherLocation.getEditText().setTextColor(Color.RED);  // change color to red
                    otherLocation.setError("Location is too long!");  // display error message
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otherLocation.getEditText().getText().length() <= 100)
                { // if the number of characters is reduced
                    // On below part: change back the color to black and remove error message
                    otherLocation.getEditText().setTextColor(Color.BLACK);
                    otherLocation.setError(null);
                }
            }
        });
    }

    /**
     * This gets the icon for ingredient being selected by the user and display it on ImageView.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == ICON_REQUEST && resultCode == RESULT_OK){
                String tag = data.getStringExtra("image tag");
                int res = getResources().getIdentifier(tag, "drawable", this.getPackageName());
                iconCode = res;  // assign the icon code
                ingredientIcon.setImageResource(res);
            }
        }catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will enable the back function to the button on press
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent intentBack = new Intent();
                setResult(Activity.RESULT_CANCELED,intentBack);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method removes all present soft keyboards and is used when user clicks on one of the spinners
     */
    private void removeKeyboard() {
        /* get the input method manager which manages interaction within the system.
         * get System service is used to access Android system-level services like the keyboard
         */
        InputMethodManager inputMethodManager =
                (InputMethodManager) AddEditIngredientActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
        // On below part: Hide the soft keyboards associated with description and amount edit text fields
        inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(amountView.getWindowToken(), 0);
    }

    /**
     * This method validates user input, checking for empty fields or input that is too long
     * @return valid  a boolean value indicating if the user input is accepted or not
     */
    public boolean validateInput() {
        Boolean valid = true;  // input is valid initially
        // On below part: get user input from edit texts and spinners
        String description = descriptionView.getEditText().getText().toString().trim();
        String category = categoryView.getSelectedItem().toString().trim();
        String location = locationView.getSelectedItem().toString().trim();
        String unit = unitView.getSelectedItem().toString().trim();
        String date = dateView.getText().toString().trim();
        String amount = amountView.getText().toString().trim();
        // On below: check if any field is empty
        if (category.isEmpty() || date.isEmpty() || location.isEmpty() || amount.isEmpty() || unit.isEmpty()){
            // On below part: create a toast warning message and return false is any field is empty
            CharSequence text = "Error, Some Fields Are Empty!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            valid = false;
        } else if (amount.equals("0")) { // if amount entered is 0
            // On below part: create a toast warning message and return false
            CharSequence text = "Error, Amount can't be zero!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (description.isEmpty()) { // if description field is empty
            // On below: set error message for the edit text
            descriptionView.setError("Field can't be empty!");
            valid = false;
        } else if (description.length() > 100) { // return false if input is too long
            valid = false;
        }
        if (category.equals("Other")){ // when user selects to input some category dynamically
            String otherCategoryChoice = otherCategory.getEditText().getText().toString().trim();
            if (otherCategoryChoice.isEmpty()){
                // On below part: set edit text error message and return false if edit text is empty
                otherCategory.setError("Field can't be empty!");
                valid = false;
            } else if(otherCategoryChoice.length() > 100){ // return false if input is too long
                valid = false;
            }
        }
        if (location.equals("Other")) { // when user selects to input some location dynamically
            String otherLocationChoice = otherLocation.getEditText().getText().toString().trim();
            if (otherLocationChoice.isEmpty()) {
                // On below part: set edit text error message and return false if edit text is empty
                otherLocation.setError("Field can't be empty!");
                valid = false;
            } else if(otherLocationChoice.length() > 100){ // return false if input is too long
                valid = false;
            }
        }
        if (unit.equals("Other")) { // when user selects to input some unit dynamically
            String otherUnitChoice = otherUnit.getEditText().getText().toString().trim();
            if (otherUnitChoice.isEmpty()) {
                // On below part: set edit text error message and return false if edit text is empty
                otherUnit.setError("Field can't be empty!");
                valid = false;
            } else if(otherUnitChoice.length() > 100){ // return false if input is too long
                valid = false;
            }
        }
        // return the final boolean value after user's complete inputs for add/edit a in-storage ingredient
        return valid;
    }


}