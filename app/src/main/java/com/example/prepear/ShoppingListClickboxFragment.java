/**
 * Classname: ShoppingListClickboxFragment
 * Version Information: 1.0.0
 * Date: 11/16/2022
 * Author: Jamie Lee
 * Copyright notice:
 */
package com.example.prepear;

import static com.google.common.reflect.Reflection.getPackageName;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    // declare all variables that used to linked to xml objects
    TextView descriptionText;
    TextView amountText;
    TextView unitText;
    TextView categoryText;
    LinearLayout newLocationLinearLayout;
    TextView descriptionWordCount;
    TextView amountWordCount;
    EditText actualAmountEditText;
    EditText bestBeforeDateEditText;
    String bestBeforeDateString;  // best before date string
    Spinner locationSpinner;
    EditText locationEditText;

    // declare the adapter for the spinner
    private ArrayAdapter<CharSequence> locationSpinnerAdapter;

    // define the date picker dialog
    private DatePickerDialog dialog;      // create datePicker for best before date

    // define the date format
    private final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    /*
     * define variables needed to connect database
     */
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
                    // if user select other, ask user to dynamically input location
                    newLocationLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    // if the user does not select other, dynamically input should be turn off
                    newLocationLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * set up the title style of the dialog fragment
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("Add Details For Ingredient");
        builder.setView(view);

        // create a date picker for the best before date of the ingredient
        bestBeforeDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        builder.setNegativeButton("Cancel", null); // if the user select cancel, nothing happens
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // get the amount, actualBestBeforeDate, location text from the layout
                String actualAmount = actualAmountEditText.getText().toString();
                String actualBestBeforeDate = bestBeforeDateEditText.getText().toString();
                String location = locationSpinner.getSelectedItem().toString();

                if (location.equals("Other")) {
                    // if the user select other, take the input new location text as location
                    newLocationLinearLayout.setVisibility(View.VISIBLE);
                    location = locationEditText.getText().toString();
                }

                if (actualAmount.equals("") || actualBestBeforeDate.equals("") || location.equals("")) {
                    // if any of the blanks were empty, warn the user
                    Toast.makeText(getActivity().getApplicationContext(), "You did not enter full information.",
                            Toast.LENGTH_LONG).show();
                } else {

                    if (Double.parseDouble(actualAmount) >= ingredient.getAmountValue()) {

                    } else {
                        // if the actual amount is less than the required one, warn the user
                        Toast.makeText(getActivity().getApplicationContext(), "Actual amount is less than needed amount.",
                                Toast.LENGTH_LONG).show();
                    }
                    // add or update input details to database
                    String finalLocation = location;

                    // get all ingredient in storage from the database
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String description = ingredient.getBriefDescription(); // get the brief description of the ingredient to add
                            boolean ingredientInStorage = true; //  set ture if the ingredient to add is not existed in database
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Log.d(TAG, String.valueOf(doc.getData().get("description"))); // Set an error message
                                // Get description, unit, amount, location, best before date, category attributes
                                String descriptionIngredientInStorage = (String) doc.getData().get("description");
                                int ingredientIconCode = Integer.parseInt(doc.getData().get("icon code").toString());
                                String ingredientId = (String) doc.getData().get("document id");
                                String storageIngredientUnit = (String) doc.getData().get("unit");
                                String storageBestBeforeDate = (String) doc.getData().get("bestBeforeDate");
                                String storageLocation = (String) doc.getData().get("location");
                                Number storageAmount = (Number) doc.getData().get("amount");

                                // get the actual amount
                                double finalActualAmount = Double.parseDouble(actualAmount);

                                if (descriptionIngredientInStorage.equals(description)
                                        && storageBestBeforeDate.equals(actualBestBeforeDate)
                                        && storageLocation.equals(finalLocation)) {
                                    /*
                                     * if the description, best before date, location were the same, we could see this as
                                     * the ingredient to add is existing in database. So the existing ingredient should be updated
                                     */
                                    if (storageIngredientUnit.equals(ingredient.getUnit())) {
                                        // if the units are the same, update directly
                                        finalActualAmount = finalActualAmount + storageAmount.doubleValue();
                                    } else {
                                        // if the units are not the same, convert to be one
                                        finalActualAmount = finalActualAmount + unitConvert(storageIngredientUnit, storageAmount.doubleValue());
                                    }
                                    db
                                            .collection("Ingredient Storage")
                                            .document(ingredientId)
                                            .update("description", description,
                                                    "category", ingredient.getIngredientCategory(),
                                                    "bestBeforeDate", actualBestBeforeDate,
                                                    "amount", finalActualAmount,
                                                    "unit", ingredient.getUnit(),
                                                    "icon code",ingredientIconCode,
                                                    "location", storageLocation);

                                    ingredientInStorage = false;
                                    break;
                                }
                            }
                            // if ingredient is not in ingredient storage database, add to database
                            if (ingredientInStorage) {
                                // get the date as the ingredient's id
                                Date dateTimeNow = new Date();
                                String documentId = DATEFORMAT.format(dateTimeNow);

                                // set the ingredient's icon code
                                int iconCode = getResources().getIdentifier("ic_baseline_add_photo_alternate_24", "drawable", "com.example.prepear");

                                // use the database controller to add the ingredient into storage
                                IngredientInStorage ingredientToAdd = new IngredientInStorage(description,
                                        ingredient.getIngredientCategory(), actualBestBeforeDate, finalLocation, actualAmount, ingredient.getUnit(), documentId, iconCode);
                                DatabaseController database = new DatabaseController();
                                database.addIngredientToIngredientStorage(getActivity(), ingredientToAdd);

                            }
                        }
                    });
                }
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

    /**
     * This function transferred and unified the unit
     * @param unit the unit is of type {@link String}
     * @param amount the amount is of type {@link Double}
     * @return the return is transferred the amount, which is of type {@link Double}
     */
    private Double unitConvert(String unit, Double amount) {
        /* Since we identify the given unit is solid unit
         * We should get scaling number according to standard unit (g) and given unit
         */
        Double scale = 1.0;
        if (unit.equals("kg")) {
            scale = 1000.0;
        }else if (unit.equals("oz")) {
            scale = 28.3495;
        }else if (unit.equals("lb")) {
            scale = 453.592;
        }else if (unit.equals("l")) {
            /* Since we identify the given unit is liquid unit
             * We should get scaling number according to standard unit (ml) and given unit
             */
            scale = 1000.0;
        }

        // return the calculated amount by multiply both given amount and scaling number
        return amount * scale;
    }
}


