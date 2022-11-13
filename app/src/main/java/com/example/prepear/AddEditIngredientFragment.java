/**
 * Class Name: ViewIngredientStorage
 * Version Information: Version 1.0
 * Create Date: Oct 25th, 2022
 * Authors: Shihao Liu, Marafi Mergani, Jingyi Xu
 * Copyright Notice:
 */
package com.example.prepear;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class creates the add/edit ingredient fragment allowing the user to add an ingredient,
 * view it and make changes to its attributes
 * @version: 1.0
 * Author: Jiayin
 */

public class AddEditIngredientFragment extends DialogFragment implements
        AdapterView.OnItemSelectedListener{
    /* initialize variables for EditText, DatePickerDialog, and fragment interaction listener objects */
    private EditText descriptionView;
    private Spinner categoryView;
    private EditText dateView;
    private Spinner locationView;
    private EditText amountView;
    private Spinner unitView; // Spinner for picking ingredient unit
    private String bestBeforeDateString;  // best before date string
    private OnFragmentInteractionListener listener;
    private DatePickerDialog dialog; // create datePicker for best before date
    private CollectionReference collectionReferenceForInStorageIngredients;
    private final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * This method defines an interface of methods that the MainActivity needs to implement
     * in order to respond to the user clicking OK, DELETE, and CANCEL buttons.
     * @see LoginActivity
     */
    public interface OnFragmentInteractionListener {
        /* list of methods implemented in main activity class
         */
        void onOkPressed(IngredientInStorage ingredientInStorage);
        void onDeletePressed(IngredientInStorage ingredientInStorage);
        void onEditPressed(IngredientInStorage ingredientInStorage);
    }

    /**
     * This method creates a new instance of AddIngredientFragment to allow the user to edit
     * the ingredient after clicking on it in the ViewIngredientStorage activity
     * @param ingredientInStorage an object of type {@link IngredientInStorage}
     *                            that the user clicked on
     * @return fragment the newly created fragment
     */
    public static AddEditIngredientFragment newInstance(IngredientInStorage ingredientInStorage) {
        /* create a fragment, and a bundle, add the ingredient to the bundle, and pass it to the
         fragment
         */
        Bundle args = new Bundle(); // create a bundle to store the ingredient passed in
        args.putSerializable("IngredientInStorage", ingredientInStorage); // add ingredient to bundle
        AddEditIngredientFragment fragment = new AddEditIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method receives the context from MainActivity, checks if the context is of type
     * {@link OnFragmentInteractionListener} and if it is, it assigns
     * the variable listener to the context, otherwise it raises a runtime error
     * @param  context information about the current state of the app received from MainActivity
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method creates the add/edit ingredient fragment if the user input is valid
     * and sets errors if the input is invalid
     * @param  savedInstanceState an object of type {@link Bundle} that stores an
     *                            {@link IngredientInStorage} object
     * @return builder a {@link Dialog} object to build the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.add_ingredient_fragment, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(
                R.layout.add_ingredient_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading4);

        /* assign variables to TextView objects and set on click listeners */
        dateView = view.findViewById(R.id.bestBeforeDate);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* create a date picker for the best before date of the ingredient */
                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH);
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(getContext(), R.style.activity_date_picker,
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

        descriptionView = view.findViewById(R.id.brief_description);  // link variable with layout object
        descriptionView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionView.setFocusable(true); // make the field focusable to receive keyboard, etc
                if (descriptionView.getText().toString().isEmpty()) {
                    // On above: check if description edit description field is empty and set an error message if empty
                    descriptionView.setError("Cannot leave Ingredient Name Empty");
                    descriptionView.requestFocus();  // focus on field when left empty
                }
            }
        });

        categoryView = (Spinner) view.findViewById(R.id.ingredient_category);
        // On below: create array adapter for the spinner from array in strings.xml file
        ArrayAdapter adapterForCategories = ArrayAdapter.createFromResource(getContext(),
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

        locationView = (Spinner) view.findViewById(R.id.ingredient_location);
        // On below: create array adapter for the spinner from array in strings.xml file
        ArrayAdapter adapterForLocation = ArrayAdapter.createFromResource(getContext(), R.array.locations,
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

        amountView = view.findViewById(R.id.ingredient_amount); // grab the amount EditText object
        amountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountView.setFocusable(true);
                if (amountView.getText().toString().isEmpty()) {
                    amountView.setError("Cannot leave Ingredient amount empty");
                    amountView.requestFocus();
                } else if (amountView.getText().toString().equals("0")) {
                    amountView.setError("Cannot enter zero for ingredient amount");
                    amountView.requestFocus();
                }
            }
        });

        unitView = (Spinner) view.findViewById(R.id.ingredient_unit);
        // create array adapter for the spinner from array in strings.xml file
        ArrayAdapter adapterForUnits = ArrayAdapter.createFromResource(getContext(), R.array.units,
                android.R.layout.simple_spinner_item);
        adapterForUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitView.setAdapter(adapterForUnits);
        unitView.setOnTouchListener( new View.OnTouchListener() {
            /*Temporarily remove keyboards before displaying spinner*/
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeKeyboard();  // remove all keyboards when spinner is on display
                return false;
            }
        });

        Bundle args = getArguments();  // get fragment arguments to access ingredient passed in
        if (args != null) { // if object already exists (the case of editing an item)
            IngredientInStorage ingredientInStorage = (IngredientInStorage) args.getSerializable(
                    "IngredientInStorage"); // get ingredient from bundle
            /* set the previous values of the text fields and spinners */
            descriptionView.setText(ingredientInStorage.getBriefDescription()); //  set ingredient description
            categoryView.setSelection(adapterForCategories
                    .getPosition(ingredientInStorage.getIngredientCategory())); // set the category spinner to the right item
            dateView.setText(ingredientInStorage.getBestBeforeDate()); // set the best before date
            locationView.setSelection(adapterForLocation
                    .getPosition(ingredientInStorage.getLocation())); // set the location spinner to the right item
            amountView.setText(String.valueOf(ingredientInStorage.getAmountValue())); // set the amount of the ingredient
            unitView.setSelection(adapterForUnits
                    .getPosition(ingredientInStorage.getUnit())); // set the unit spinner to the right item

            // On below part: View/Edit Fragment for updating in-storage ingredient's detailed information after clicking it on ListView and
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
            title.setText("View/Edit Ingredient");
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        /* set listener for the delete button and call the
                        onDeletePressed method to delete item
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // delete this clicked ingredient from "Ingredient Storage" Collection
                            DatabaseController databaseController = new DatabaseController();
                            databaseController.deleteIngredientFromIngredientStorage(getActivity(), ingredientInStorage);
                            listener.onDeletePressed(ingredientInStorage);
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /* set listener for the Ok button, get user input,
                        and call onEditPressed method to update the list Adapter
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            /* get user input,
                             * ensure that the location entered is valid
                             * and that no text field is left empty
                             * */
                            String description = descriptionView.getText().toString();
                            String category = categoryView.getSelectedItem().toString();
                            String date = dateView.getText().toString();
                            String location = locationView.getSelectedItem().toString();
                            String amount = amountView.getText().toString();
                            String unit = unitView.getSelectedItem().toString();
                            if (description.isEmpty() || category.isEmpty() || date.isEmpty()
                                    || location.isEmpty() || amount.isEmpty() || unit.isEmpty()) {
                                CharSequence text = "Error, Some Fields Are Empty!";
                                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                                return;
                            } else if (amount.equals("0")) {
                                CharSequence text = "Error, Amount Can Not Be Zero!";
                                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                                return;
                            }
                            double amountValue = Double.parseDouble(amount);
                            if (amount.matches("0")) {
                                CharSequence text = "Error, Amount Can Not Be Zero!";
                                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                                return;
                            }
                            /* set the same ingredient object with the new user input */
                            ingredientInStorage.setBriefDescription(description);
                            ingredientInStorage.setIngredientCategory(category);
                            ingredientInStorage.setBestBeforeDate(date);
                            ingredientInStorage.setAmountValue(amountValue);
                            ingredientInStorage.setUnit(unit);
                            ingredientInStorage.setLocation(location);
                            // on below:
                            DatabaseController database = new DatabaseController();
                            database.editIngredientInIngredientStorage(getActivity(), ingredientInStorage);
                            listener.onEditPressed(ingredientInStorage);
                        }
                    }).create();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("Add Ingredient");
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    /* set listener for the Ok button, get user input,
                    and call onOkPressed method to add ingredient item
                     */

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /* get user input,
                         * ensure that the location entered is valid
                         * and that no text field is left empty
                         * */
                        String description = descriptionView.getText().toString();
                        String category = categoryView.getSelectedItem().toString();
                        String date = dateView.getText().toString();
                        String location = locationView.getSelectedItem().toString();
                        String unit = unitView.getSelectedItem().toString();
                        String amount = amountView.getText().toString();

                        if (description.isEmpty() || category.isEmpty() || date.isEmpty()
                                || location.isEmpty() || amount.isEmpty() || unit.isEmpty()) {
                            CharSequence text = "Error, Some Fields Are Empty!";
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                            return;
                        } else if (amount.equals("0")) {
                            CharSequence text = "Error, Amount Can Not Be Zero!";
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                            return;
                        }
                        int iconCode = 1;
                        // key: value pair as a element in HashMap/Map
                        Date dateTimeNow = new Date();
                        String documentId = DATEFORMAT.format(dateTimeNow);
                        IngredientInStorage ingredientToAdd = new IngredientInStorage(description,
                                category, date, location, amount, unit, documentId,iconCode);
                        DatabaseController database = new DatabaseController();
                        database.addIngredientToIngredientStorage(getActivity(), ingredientToAdd);
                        listener.onOkPressed(ingredientToAdd);
                    }
                }).create();
    }


    /**
     * This method removes all present soft keyboards and is used when user clicks on one of the spinners
     */
    private void removeKeyboard() {
        /* get the input method manager which manages interaction within the system.
           get System service is used to access Android system-level services like the keyboard */
        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // Hide the soft keyboards associated with description and amount edit text fields
        inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(amountView.getWindowToken(), 0);
    }

    @Override
    public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){

    }

    @Override
    public void onNothingSelected (AdapterView < ? > parent){

    }


}