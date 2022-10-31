package com.example.prepear;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.Calendar;
import java.util.HashMap;

/**
 * This class creates the add/edit ingredient fragment allowing the user to add an ingredient, view
 * it and make changes to its attributes
 * @author: Marafi Mergani
 * @version: 1
 */

public class AddEditIngredientFragment extends DialogFragment implements
        AdapterView.OnItemSelectedListener{
    /* initialize variables for EditText, DatePickerDialog,
       and fragment interaction listener objects
       <access_identifier> variableName;
     */
    private EditText descriptionView;
    private Spinner categoryView;
    private EditText dateView;
    private Spinner locationView;
    private EditText amountView;
    private Spinner unitView;
    private String bbd_str;  // best before date string
    private OnFragmentInteractionListener listener;
    private DatePickerDialog dialog; // create datePicker for best before date
    private CollectionReference collectionReferenceForInStorageIngredients;

    /* Constructor */
    public AddEditIngredientFragment(CollectionReference collectionReference) {
        this.collectionReferenceForInStorageIngredients = collectionReference;
    }

    /**
     * This method defines an interface of methods that the MainActivity needs to implement
     * in order to respond to the user clicking OK, DELETE, and CANCEL buttons.
     * @see MainActivity
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
    public static AddEditIngredientFragment newInstance(IngredientInStorage ingredientInStorage,
                                                        CollectionReference collectionReference) {
        /* create a fragment, and a bundle, add the ingredient to the bundle, and pass it to the
         fragment
         */
        Bundle args = new Bundle(); // create a bundle to store the ingredient passed in
        args.putSerializable("IngredientInStorage", ingredientInStorage); // add ingredient to bundle
        AddEditIngredientFragment fragment = new AddEditIngredientFragment(collectionReference);
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


        /*assign variables to TextView objects and set on click listeners*/
        dateView = view.findViewById(R.id.bestBeforeDate);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* create a date picker for the best before date of the ingredient */
                Calendar currentDate = Calendar.getInstance();
                int mYear = currentDate.get(Calendar.YEAR);
                int mMonth = currentDate.get(Calendar.MONTH);
                int mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(getContext(), R.style.activity_date_picker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                // if condition statement helps to regulate the format.
                                if (monthOfYear < 9 && dayOfMonth < 10) {
                                    bbd_str = year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                } else if (dayOfMonth < 10) {
                                    bbd_str = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                } else if (monthOfYear < 9) {
                                    bbd_str = year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                } else {
                                    bbd_str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                dateView.setText(bbd_str);
                            }
                        }, mYear, mMonth, mDay);
                dialog.show();
                /* Temporarily remove keyboards before displaying the dialog*/
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(amountView.getWindowToken(), 0);
            }
        });

        descriptionView = view.findViewById(R.id.brief_description);
        descriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionView.setFocusable(true);
                if (descriptionView.getText().toString().isEmpty()) {
                    descriptionView.setError("Cannot leave Ingredient Name Empty");
                    descriptionView.requestFocus();
                }
            }
        });

        categoryView = (Spinner) view.findViewById(R.id.ingredient_category);
        ArrayAdapter adapterForCategories = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapterForCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryView.setAdapter(adapterForCategories);
        categoryView.setOnTouchListener(new View.OnTouchListener() {
            /* Temporarily remove keyboards before displaying spinner */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(amountView.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
                return false;
            }
        });

        locationView = (Spinner) view.findViewById(R.id.ingredient_location);
        ArrayAdapter adapterForLocation = ArrayAdapter.createFromResource(getContext(), R.array.locations,
                android.R.layout.simple_spinner_item);
        adapterForLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationView.setAdapter(adapterForLocation);
        locationView.setOnTouchListener(new View.OnTouchListener() {
            /* Temporarily remove keyboards before displaying spinner */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(amountView.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
                return false;
            }
        });

        amountView = view.findViewById(R.id.ingredient_amount);
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
        ArrayAdapter adapterForUnits = ArrayAdapter.createFromResource(getContext(), R.array.units,
                android.R.layout.simple_spinner_item);
        adapterForUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitView.setAdapter(adapterForUnits);
        unitView.setOnTouchListener(new View.OnTouchListener() {
            /* Temporarily remove keyboards before displaying spinner */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(amountView.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
                return false;
            }
        });


        Bundle args = getArguments();
        if (args != null) { //if object already exists (the case of editing an item)
            IngredientInStorage ingredientInStorage = (IngredientInStorage) args.getSerializable(
                    "IngredientInStorage");
            /* set the old values of the text fields */
            descriptionView.setText(ingredientInStorage.getBriefDescription());
            categoryView.setSelection(adapterForCategories.getPosition(ingredientInStorage.getIngredientCategory()));
            dateView.setText(ingredientInStorage.getBestBeforeDate());
            locationView.setSelection(adapterForLocation.getPosition(ingredientInStorage.getLocation()));
            amountView.setText(String.valueOf(ingredientInStorage.getAmount()));
            unitView.setSelection(adapterForUnits.getPosition(String.valueOf(ingredientInStorage.getUnit())));

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
            title.setText("Edit Ingredient");
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        /* set listener for the delete button and call the
                        onDeletePressed method to delete item
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.onDeletePressed(ingredientInStorage);
                            // delete this clicked ingredient from "Ingredient Storage" Collection
                            collectionReferenceForInStorageIngredients
                                    .document(ingredientInStorage.getBriefDescription())
                                    .delete();
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
                                Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
                                /*View toastView = toast.getView(); // This will return the default View of the Toast.
                                TextView toastMessage = toastView.findViewById(android.R.id.message);
                                toastMessage.setTextSize(30);
                                toastMessage.setTextColor(Color.parseColor("#FFFFFF"));
                                toastMessage.setGravity(Gravity.CENTER);
                                toastMessage.setCompoundDrawablePadding(16);
                                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_error_icon_foreground, 0, 0, 0);
                                toastView.setBackgroundColor(Color.parseColor("#BA5951"));*/
                                toast.show();
                                return;
                            } else if (amount.equals("0")) {
                                CharSequence text = "Error, Amount Can Not Be Zero!";
                                Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
                                /*View toastView = toast.getView(); // This will return the default View of the Toast.
                                TextView toastMessage = toastView.findViewById(android.R.id.message);
                                toastMessage.setTextSize(30);
                                toastMessage.setTextColor(Color.parseColor("#FFFFFF"));
                                toastMessage.setGravity(Gravity.CENTER);
                                toastMessage.setCompoundDrawablePadding(16);
                                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_error_icon_foreground, 0, 0, 0);
                                toastView.setBackgroundColor(Color.parseColor("#BA5951"));*/
                                toast.show();
                                return;
                            }
                            Float amount_int = Float.parseFloat(amount);
                            if (amount.matches("0")) {
                                CharSequence text = "Error, Amount Can Not Be Zero!";
                                Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
                                /*View toastView = toast.getView(); // This will return the default View of the Toast.
                                TextView toastMessage = toastView.findViewById(android.R.id.message);
                                toastMessage.setTextSize(30);
                                toastMessage.setTextColor(Color.parseColor("#FFFFFF"));
                                toastMessage.setGravity(Gravity.CENTER);
                                toastMessage.setCompoundDrawablePadding(16);
                                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_error_icon_foreground, 0, 0, 0);
                                toastView.setBackgroundColor(Color.parseColor("#BA5951"));*/
                                toast.show();
                                return;
                            }
                            /* set the same ingredient object with the new user input */
                            ingredientInStorage.setBriefDescription(description);
                            ingredientInStorage.setIngredientCategory(category);
                            ingredientInStorage.setBestBeforeDate(date);
                            ingredientInStorage.setAmount(amount_int);
                            ingredientInStorage.setUnit(unit);
                            ingredientInStorage.setLocation(location);
                            listener.onEditPressed(ingredientInStorage);  // call function to update
                            // list adapter

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
                            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
                            /*View toastView = toast.getView(); // This will return the default View of the Toast.
                            TextView toastMessage = toastView.findViewById(android.R.id.message);
                            toastMessage.setTextSize(30);
                            toastMessage.setTextColor(Color.parseColor("#FFFFFF"));
                            toastMessage.setGravity(Gravity.CENTER);
                            toastMessage.setCompoundDrawablePadding(16);
                            toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_error_icon_foreground, 0, 0, 0);
                            toastView.setBackgroundColor(Color.parseColor("#BA5951"));*/
                            toast.show();
                            return;
                        } else if (amount.equals("0")) {
                            CharSequence text = "Error, Amount Can Not Be Zero!";
                            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
                            /*View toastView = toast.getView(); // This will return the default View of the Toast.
                            TextView toastMessage = toastView.findViewById(android.R.id.message);
                            toastMessage.setTextSize(30);
                            toastMessage.setTextColor(Color.parseColor("#FFFFFF"));
                            toastMessage.setGravity(Gravity.CENTER);
                            toastMessage.setCompoundDrawablePadding(16);
                            toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_error_icon_foreground, 0, 0, 0);
                            toastView.setBackgroundColor(Color.parseColor("#BA5951"));*/
                            toast.show();
                            return;
                        }
                        Float amount_int = Float.parseFloat(amount);
                        listener.onOkPressed(new IngredientInStorage(
                                description, category, date, location, amount_int, unit));
                        HashMap<String, String> data = new HashMap<>();
                        data.put("description", description);
                        data.put("bestBeforeDate", date);
                        data.put("location", location);
                        data.put("category", category);
                        data.put("amount", amount);
                        data.put("unit", unit);
                        // two ingredients with the same descriptions (as id) should be allowed
                        collectionReferenceForInStorageIngredients
                                .document(description)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // These are a method which gets executed when the task is succeeded
                                        Log.d(description, "Data has been added successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // These are a method which gets executed if there’s any problem
                                        Log.d(description, "Data could not be added!" + e.toString());
                                    }
                                });

                    }
                }).create();
    }

        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){

        }

        @Override
        public void onNothingSelected (AdapterView < ? > parent){

        }


    }

