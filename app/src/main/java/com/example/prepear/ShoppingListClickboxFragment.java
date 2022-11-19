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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class ShoppingListClickboxFragment extends DialogFragment {
    // declare variables
    private ArrayAdapter<CharSequence> unitSpinnerAdapter;
    private ArrayAdapter<CharSequence> categorySpinnerAdapter;
    private ArrayAdapter<CharSequence> locationSpinnerAdapter;
    private TextView descriptionText;
    private TextView amountText;
    private Spinner unitSpinner;
    private TextView unitEditText;
    private LinearLayout newUnitLinearLayout;
    private Spinner categorySpinner;
    private TextView categoryEditText;
    private LinearLayout newCategoryLinearLayout;
    private TextView descriptionWordCount;
    private TextView amountWordCount;
    private EditText actualAmountEditText;
    private EditText bestBeforeDateEditText;
    private Spinner locationSpinner;
    private EditText locationEditText;
    private ShoppingListClickboxFragment.OnFragmentInteractionListener listener;

    /**
     * This method defines an interface of methods that the ShoppingListViewModel needs to implement
     * in order to respond to the user clicking Ok buttons.
     * @see ShoppingListViewModel
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }
    /**
     * This method creates a new instance of ShoppingListClickboxFragment so user can add
     * the details of ingredient by clicking checkbox
     * @param ingredient {@link IngredientInStorage} that the user clicked on
     * @return fragment the newly created fragment
     */
    static ShoppingListClickboxFragment newInstance(IngredientInStorage ingredient) {
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
        if (context instanceof ShoppingListClickboxFragment.OnFragmentInteractionListener) {
            listener = (ShoppingListClickboxFragment.OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method creates the add ingredient fragment if the user input is valid
     * and sets errors if the input is invalid
     * @param  savedInstanceState {@link Bundle} that stores an ingredient {@link IngredientInStorage} object
     * @return builder a {@link Dialog} object to build the fragment
     */
    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // connects views to its layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.shopping_list_view_ingredient_details, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_ingredient_fragments_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading);
        descriptionText = view.findViewById(R.id.description_edit_text);
        amountText = view.findViewById(R.id.ingredient_amount_edit_text);
        unitSpinner = view.findViewById(R.id.ingredient_unit_edit_text);
        unitEditText = view.findViewById(R.id.new_ingredient_unit_edit_text);
        newUnitLinearLayout = view.findViewById(R.id.new_unit_linear_layout);
        categorySpinner = view.findViewById(R.id.ingredient_category_edit_text);
        categoryEditText = view.findViewById(R.id.new_ingredient_category_edit_text);
        newCategoryLinearLayout = view.findViewById(R.id.new_ingredient_category_linear_layout);
        descriptionWordCount = view.findViewById(R.id.description_word_count);
        amountWordCount = view.findViewById(R.id.amount_word_count);
        actualAmountEditText = view.findViewById(R.id.ingredient_actual_amount);
        bestBeforeDateEditText = view.findViewById(R.id.best_before_date);
        locationEditText = view.findViewById(R.id.new_ingredient_location_edit_text);
        locationSpinner = view.findViewById(R.id.ingredient_location);

        // getting attributes from ingredient and display it on fragment
        Bundle bundle = getArguments();
        IngredientInStorage ingredient = (IngredientInStorage) bundle.getSerializable("ingredient");
        descriptionText.setText(ingredient.getBriefDescription());
        amountText.setText(ingredient.getAmountString());
        unitSpinner.setSelection(unitSpinnerAdapter.getPosition(ingredient.getUnit()));;
        categorySpinner.setSelection(categorySpinnerAdapter.getPosition(ingredient.getIngredientCategory()));

        // set up the location spinner
        locationSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.locations,
                android.R.layout.simple_spinner_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIngredientLocation = locationSpinner.getSelectedItem().toString();
                if (selectedIngredientLocation.equals("Other")) {
                    newUnitLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    newUnitLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("Add Details For Ingredient");
        builder.setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    // need to work on adding details
    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Bundle bundle = getArguments();
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener((View v) -> {
                // still needs to change bestBeforeDate to date picker
                Boolean wantToCloseDialog = true;
                String actualAmount = actualAmountEditText.getText().toString();
                String bestBeforeDate = bestBeforeDateEditText.getText().toString();
                String location = locationSpinner.getSelectedItem().toString();
                if (location.equals("Other")) {
                    location = locationEditText.getText().toString();
                }
                if (actualAmount.equals("") || bestBeforeDate.equals("") || location.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "You did not enter full information.",
                            Toast.LENGTH_LONG).show();
                    wantToCloseDialog = false;
                } else {
                    IngredientInStorage ingredient = (IngredientInStorage) bundle.getSerializable("ingredient");
                    ingredient.setAmountValue(Double.parseDouble(actualAmount));
                    ingredient.setBestBeforeDate(bestBeforeDate);
                    ingredient.setLocation(location);
                    listener.onOkPressed();
                    // listener.onOkPressed(new IngredientInStorage(actualAmount, bestBeforeDate, location));
                }
                if (wantToCloseDialog) {
                    d.dismiss();
                }

            });
        }
    }
}


