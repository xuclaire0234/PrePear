package com.example.prepear;

import static com.google.android.material.color.MaterialColors.getColor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;

/**
 * This class creates the add/edit ingredient fragment allowing the user to add an ingredient, view
 * it and make changes to its attributes
 * @author: Marafi Mergani, Shihao Liu
 * @version: 1.0
 */

public class AddEditIngredientFragment extends DialogFragment {
    /* initialize variables for EditText, DatePickerDialog,
       and fragment interaction listener objects
       <access_identifier> variableName;
     */
    private EditText descriptionView; //
    private EditText categoryView; //
    private EditText dateView; //
    private EditText locationView; //
    private EditText amountView; //
    private EditText unitView; //
    private String bbd_str; //
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_ingredient_fragment,
                null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.add_ingredient_custom_title, null);
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
                                // set day of month , month and year value in the edit text
                                // if condition statement helps to regulate the format.
                                if(monthOfYear < 9 && dayOfMonth < 10 ){
                                    bbd_str = year + "-" +"0"+ (monthOfYear + 1) + "-" + "0"+ dayOfMonth;
                                } else if(dayOfMonth < 10){
                                    bbd_str = year + "-" + (monthOfYear + 1) + "-" + "0"+ dayOfMonth;
                                }else if(monthOfYear < 9){
                                    bbd_str = year + "-" +"0"+ (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                else{
                                    bbd_str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;}
                                dateView.setText(bbd_str);
                            }
                        }, currentYear, currentMonth, currentDay);
                dialog.show();
            }
        });

        descriptionView = view.findViewById(R.id.brief_description);
        descriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionView.setFocusable(true);
                if(descriptionView.getText().toString().isEmpty()) {
                    descriptionView.setError("Cannot leave Ingredient Name Empty");
                    descriptionView.requestFocus();
                }
            }
        });

        categoryView = view.findViewById(R.id.ingredient_category);
        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryView.setFocusable(true);
                if(categoryView.getText().toString().isEmpty()) {
                    categoryView.setError("Cannot leave Ingredient Category Empty");
                    categoryView.requestFocus();
                }
            }
        });

        locationView = view.findViewById(R.id.ingredient_location);
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationView.setFocusable(true);
                if (locationView.getText().toString().isEmpty()) {
                    locationView.setError(
                            "Cannot leave Location Empty, Please choose correct location.");
                    locationView.requestFocus();
                }
            }
        });

        amountView = view.findViewById(R.id.ingredient_amount);
        amountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountView.setFocusable(true);
                if(amountView.getText().toString().isEmpty()) {
                    amountView.setError("Cannot leave Ingredient amount empty");
                    amountView.requestFocus();
                } else if(amountView.getText().toString().equals("0")) {
                    amountView.setError("Cannot enter zero for ingredient amount");
                    amountView.requestFocus();
                }
            }
        });

        unitView = view.findViewById(R.id.ingredient_unit);
        unitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitView.setFocusable(true);
                if(unitView.getText().toString().isEmpty()) {
                    unitView.setError("Cannot leave unit cost of Ingredient empty");
                    unitView.requestFocus();
                } else if(unitView.getText().toString().equals("0")) {
                    unitView.setError("Cannot enter zero for ingredient unit cost");
                    unitView.requestFocus();
                }
            }
        });

        Bundle args = getArguments();
        // on below: if ingredient already exists (the case of editing an item)
        if (args != null) {
            IngredientInStorage ingredientInStorage = (IngredientInStorage) args.getSerializable(
                    "IngredientInStorage");
            /* set the old values of the text fields */
            descriptionView.setText(ingredientInStorage.getBriefDescription());
            categoryView.setText(ingredientInStorage.getIngredientCategory());
            dateView.setText(ingredientInStorage.getBestBeforeDate());
            locationView.setText(ingredientInStorage.getLocation());
            amountView.setText(ingredientInStorage.getAmount());
            unitView.setText(ingredientInStorage.getUnit());

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
                            if (description.isEmpty()) {
                                return;
                            }
                            String category = categoryView.getText().toString();
                            if (category.isEmpty()) {
                                return;
                            }
                            String date = dateView.getText().toString();
                            if (date.isEmpty()) {
                                return;
                            }
                            String location = locationView.getText().toString();
                            if (location.isEmpty()) {
                                return;
                            }
                            String amount = amountView.getText().toString();
                            int amount_int = Integer.parseInt(amount);
                            if (amount.isEmpty() || amount.equals("0")) {
                                return;
                            }
                            String unit = unitView.getText().toString();
                            if (unit.isEmpty()) {
                                return;
                            }
                            /* set the same ingredient object with the new user input */
                            ingredientInStorage.setBriefDescription(description);
                            ingredientInStorage.setIngredientCategory(category);
                            ingredientInStorage.setBestBeforeDate(date);
                            ingredientInStorage.setAmount(amount_int);
                            ingredientInStorage.setUnit(unit);
                            ingredientInStorage.setLocation(location);
                            listener.onEditPressed(ingredientInStorage);  // call method to update list adapter

                            // on below: DB's Ingredient Storage Collection will update this clicked ingredient's data
                            collectionReferenceForInStorageIngredients
                                    .document(ingredientInStorage.getBriefDescription())
//                                    .update("description", description,
//                                            "category", category,
//                                            "bestBeforeDate", date,
//                                            "amount", amount,
//                                            "location", location,
//                                            "unit", unit);
                                    .set(ingredientInStorage);
                        }
                    }).create();
        }

        // on below: Adding a new ingredient in storage
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
                        if (description.isEmpty()) {
                            return;
                        }
                        String category = categoryView.getText().toString();
                        if (category.isEmpty()) {
                            return;
                        }
                        String date = dateView.getText().toString();
                        if (date.isEmpty()) {
                            return;
                        }
                        String location = locationView.getText().toString();
                        if(location.isEmpty()){
                            return;
                        }

                        String amount = amountView.getText().toString();
                        int amount_int = Integer.parseInt(amount);
                        if (amount.isEmpty() || amount_int == 0) {
                            return;
                        }
                        String unit = unitView.getText().toString();
                        if (unit.isEmpty()) {
                            return;
                        }
                        IngredientInStorage newIngredient = new IngredientInStorage(description, category,
                                                                date, location, amount_int, unit);
                        listener.onOkPressed(newIngredient);

                        HashMap<String,String> data = new HashMap<>();
                        data.put("description", description);
                        data.put("bestBeforeDate", date);
                        data.put("location", location);
                        data.put("category", category);
                        data.put("amount", amount);
                        data.put("unit",unit);
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


}

