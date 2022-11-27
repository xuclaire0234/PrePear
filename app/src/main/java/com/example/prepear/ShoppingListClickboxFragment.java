/**
 * Classname: ShoppingListClickboxFragment
 * Version Information: 1.0.0
 * Date: 11/16/2022
 * Author: Jamie Lee
 * Copyright notice:
 */
package com.example.prepear;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Nullable;

public class ShoppingListClickboxFragment extends DialogFragment {
    // declare variables
    private ArrayAdapter<CharSequence> locationSpinnerAdapter;
    private TextView descriptionText;
    private TextView amountText;
    private TextView unitText;
    private TextView categoryText;
    private LinearLayout newLocationLinearLayout;
    private TextView descriptionWordCount;
    private TextView amountWordCount;
    private EditText actualAmountEditText;
    private EditText bestBeforeDateEditText;
    private String bestBeforeDateString;  // best before date string
    private Spinner locationSpinner;
    private EditText locationEditText;
    private DatePickerDialog dialog;      // create datePicker for best before date
    private final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ShoppingListClickboxFragment.OnFragmentInteractionListener listener;

    /**
     * This method defines an interface of methods that the ShoppingListViewModel needs to implement
     * in order to respond to the user clicking Ok buttons.
     *
     * @see
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }

    /**
     * This method creates a new instance of ShoppingListClickboxFragment so user can add
     * the details of ingredient by clicking checkbox
     *
     * @param ingredient {@link IngredientInStorage} that the user clicked on
     * @return fragment the newly created fragment
     */
    public static ShoppingListClickboxFragment newInstance(IngredientInRecipe ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);
        ShoppingListClickboxFragment fragment = new ShoppingListClickboxFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * This method receives the context from ShoppingListViewModel, checks if the context is of type
     * {@link ShoppingListClickboxFragment.OnFragmentInteractionListener} and if it is, it assigns
     * the variable listener to the context, otherwise it raises a runtime error
     * @param  context information about the current state of the app received from ShoppingListViewModel
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnFragmentInteractionListener) getParentFragment();
//        if (getParentFragment() instanceof OnFragmentInteractionListener) {
//            listener = (OnFragmentInteractionListener) getParentFragment();
//        }
//        else {
//            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
//        }
    }

    final String TAG = "Ingredient Storage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Ingredient Storage");

    /**
     * This method creates the add ingredient fragment if the user input is valid
     * and sets errors if the input is invalid
     *
     * @param savedInstanceState {@link Bundle} that stores an ingredient {@link IngredientInStorage} object
     * @return builder a {@link Dialog} object to build the fragment
     */
    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // connects views to its layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.shopping_list_add_ingredient_details, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_ingredient_fragments_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading);
        descriptionText = view.findViewById(R.id.brief_description);
        amountText = view.findViewById(R.id.ingredient_amount);
        unitText = view.findViewById(R.id.ingredient_unit);
        categoryText = view.findViewById(R.id.ingredient_category);
        locationEditText = view.findViewById(R.id.new_ingredient_category_edit_text);
        newLocationLinearLayout = view.findViewById(R.id.new_ingredient_location_linear_layout);
        descriptionWordCount = view.findViewById(R.id.description_word_count);
        amountWordCount = view.findViewById(R.id.amount_word_count);
        actualAmountEditText = view.findViewById(R.id.ingredient_actual_amount);
        bestBeforeDateEditText = view.findViewById(R.id.best_before_date);
        locationEditText = view.findViewById(R.id.new_ingredient_location_edit_text);
        locationSpinner = view.findViewById(R.id.ingredient_location);

        // getting attributes from ingredient and display it on fragment
        Bundle bundle = getArguments();
        IngredientInRecipe ingredient = (IngredientInRecipe) bundle.getSerializable("ingredient");
        descriptionText.setText(ingredient.getBriefDescription());
        amountText.setText(String.valueOf(ingredient.getAmountValue()));
        unitText.setText(ingredient.getUnit());
        categoryText.setText(ingredient.getIngredientCategory());

        // set up the location spinner
        locationSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.locations,
                android.R.layout.simple_spinner_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIngredientLocation = locationSpinner.getSelectedItem().toString();
                if (selectedIngredientLocation.equals("Other")) {
                    newLocationLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    newLocationLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("Add Details For Ingredient");
        builder.setView(view);

        bestBeforeDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a date picker for the best before date of the ingredient
                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH);
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(getContext(), R.style.activity_date_picker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                /* On below parts:
                                 set the day of month , the month of year and the year value in the edit text
                                 if-conditional statement helps to regulate the format of yyyy-mm-dd. */
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
                                bestBeforeDateEditText.setText(bestBeforeDateString);
                            }
                        }, currentYear, currentMonth, currentDay);
                dialog.show();
                // Temporarily remove keyboards before displaying the dialog
                removeKeyboard();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // listener.onOkPressed(new IngredientInStorage(ingredient.getBriefDescription(), ingredient.getIngredientCategory(), bestBeforeDate, location, actualAmount, ingredient.getUnit(), ingredient.getDocumentId(),0));
                // Loop through all the documents in the collection named "Recipes"

                // set date picker dialog for bestBeforeDate
                String actualAmount = actualAmountEditText.getText().toString();
                String bestBeforeDate = bestBeforeDateEditText.getText().toString();
                String location = locationSpinner.getSelectedItem().toString();

                if (location.equals("Other")) {
                    newLocationLinearLayout.setVisibility(View.VISIBLE);
                    location = locationEditText.getText().toString();
                }

                if (actualAmount.equals("") || bestBeforeDate.equals("") || location.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "You did not enter full information.",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (Double.parseDouble(actualAmount) >= ingredient.getAmountValue()) {
                        // shoppingListCheckBox.setChecked(true);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Actual amount is less than needed amount.",
                                Toast.LENGTH_LONG).show();
                    }
                }

                String finalLocation = location;
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable
                            FirebaseFirestoreException error) {
                        String description = ingredient.getBriefDescription();
                        boolean ingredientInStorage = true;
                        // Loop through all the documents in the collection named "Recipes"
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Log.d(TAG, String.valueOf(doc.getData().get("description"))); // Set an error message

                            // Get description and category attributes
                            String descriptionIngredientInStorage = (String) doc.getData().get("description");
                            int ingredientIconCode = Integer.parseInt(doc.getData().get("icon code").toString());
                            String ingredientId = (String) doc.getData().get("document id");
                            double finalActualAmount = Double.parseDouble(actualAmount);

                            // if ingredient is also in Ingredient Storage, update database
                            if (descriptionIngredientInStorage.equals(description)) {
                                db
                                        .collection("Ingredient Storage")
                                        .document(ingredientId)
                                        .update("description", description,
                                                "category", ingredient.getIngredientCategory(),
                                                "bestBeforeDate", bestBeforeDateString,
                                                "amount", finalActualAmount,
                                                "unit", ingredient.getUnit(),
                                                "icon code",ingredientIconCode,
                                                "location", finalLocation);

//                                Toast.makeText(getActivity().getApplicationContext(),
//                                        "Ingredient in storage has been updated",
//                                        Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                ingredientInStorage = false;
                            }
                        }
                        // if ingredient is not in storage, add to database
                        if (ingredientInStorage == false) {
                            Date dateTimeNow = new Date();
                            String documentId = DATEFORMAT.format(dateTimeNow);
//                            HashMap<String, Object> data = new HashMap<>();
//                            data.put("document id", documentId);
//                            data.put("description", description);
//                            data.put("bestBeforeDate", bestBeforeDate);
//                            data.put("location", finalLocation);
//                            data.put("category", ingredient.getIngredientCategory());
//                            data.put("amount", actualAmount);
//                            data.put("unit", ingredient.getUnit());
//                            data.put("icon code", 0);
//
//                            db
//                                    .collection("Ingredient Storage")
//                                    .document(documentId)
//                                    .set(data);
                            IngredientInStorage ingredientToAdd = new IngredientInStorage(description,
                                    ingredient.getIngredientCategory(), bestBeforeDate, finalLocation, actualAmount, ingredient.getUnit(), documentId,0);
                            DatabaseController database = new DatabaseController();
                            database.addIngredientToIngredientStorage(getActivity(), ingredientToAdd);

                            listener.onOkPressed();
                        }
                    }
                });
            }
        });
        return builder.create();
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
        inputMethodManager.hideSoftInputFromWindow(actualAmountEditText.getWindowToken(), 0);
    }
}


