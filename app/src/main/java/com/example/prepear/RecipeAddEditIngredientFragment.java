/**
 * Classname: RecipeAddIngredientFragment
 * Version Information: 1.0.0
 * Date: 11/1/2022
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
import android.text.Editable;
import android.text.TextWatcher;
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

/**
 * This class creates a fragment called RecipeAddIngredientFragment. This fragment allow user to
 * add the ingredient to certain recipe with its attributes: description, amount, unit, category.
 * This fragment could be directed from AddEditRecipeActivity.
 */
public class RecipeAddEditIngredientFragment extends DialogFragment {
    /* declare variables */
    private ArrayAdapter<CharSequence> unitSpinnerAdapter;
    private ArrayAdapter<CharSequence> categorySpinnerAdapter;
    private EditText descriptionText;
    private EditText amountText;
    private Spinner unitSpinner;
    private EditText unitEditText;
    private LinearLayout newUnitLinearLayout;
    private Spinner categorySpinner;
    private EditText categoryEditText;
    private LinearLayout newCategoryLinearLayout;
    private OnFragmentInteractionListener listener;
    private TextView descriptionWordCount;
    private TextView amountWordCount;

    /**
     * This method defines an interface of methods that the AddEditRecipeActivity needs to implement
     * in order to respond to the user clicking Confirm buttons.
     * @see AddEditRecipeActivity
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(IngredientInRecipe ingredient);
        void onDeletePressed(IngredientInRecipe ingredient);
        void onOkPressed(IngredientInRecipe ingredient);
    }

    /**
     * This method creates a new instance of RecipeAddIngredientFragment so user can add
     * the ingredient to certain recipe by clicking on it in the AddEditRecipe activity
     * @param ingredient {@link IngredientInRecipe} that the user clicked on
     * @return fragment the newly created fragment
     */
    static RecipeAddEditIngredientFragment newInstance(IngredientInRecipe ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);
        RecipeAddEditIngredientFragment fragment = new RecipeAddEditIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method receives the context from AddEditRecipeActivity, checks if the context is of type
     * {@link OnFragmentInteractionListener} and if it is, it assigns
     * the variable listener to the context, otherwise it raises a runtime error
     * @param  context information about the current state of the app received from AddEditRecipeActivity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method creates the add ingredient fragment if the user input is valid
     * and sets errors if the input is invalid
     * @param  savedInstanceState {@link Bundle} that stores an ingredient {@link IngredientInRecipe} object
     * @return builder a {@link Dialog} object to build the fragment
     */
    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /* connects views to its layout */
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_ingredient_fragment, null);
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

        /* set up word count for amount and description */
        final TextWatcher descriptionTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                descriptionWordCount.setText(String.valueOf(30 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        descriptionText.addTextChangedListener(descriptionTextEditorWatcher);
        final TextWatcher amountTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                amountWordCount.setText(String.valueOf(10 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        amountText.addTextChangedListener(amountTextEditorWatcher);

        /* set up the unit spinner */
        unitSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.units,
                android.R.layout.simple_spinner_item);
        unitSpinner.setAdapter(unitSpinnerAdapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecipeCategory = unitSpinner.getSelectedItem().toString();
                if (selectedRecipeCategory.equals("Other")) {
                    newUnitLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    newUnitLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* set up the category spinner */
        categorySpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ingredient_categories,
                android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecipeCategory = categorySpinner.getSelectedItem().toString();
                if (selectedRecipeCategory.equals("Other")) {
                    newCategoryLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    newCategoryLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* return the added ingredient back to AddEditRecipeActivity */
        Bundle bundle = getArguments();
        if (bundle != null) {
            /* Editing existing ingredient */
            IngredientInRecipe ingredient = (IngredientInRecipe) bundle.getSerializable("ingredient");
            descriptionText.setText(ingredient.getBriefDescription());
            amountText.setText(String.valueOf(ingredient.getAmountString()));
            String unit = ingredient.getUnit();
            List<String> units = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.units)));
            if (units.contains(unit)) {
                unitSpinner.setSelection(unitSpinnerAdapter.getPosition(unit));
            } else {
                unitSpinner.setSelection(unitSpinnerAdapter.getPosition("Other"));
                newUnitLinearLayout.setVisibility(View.VISIBLE);
                unitEditText.setText(unit);
            }
            String ingredientCategory = ingredient.getIngredientCategory();
            List<String> ingredientCategories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.ingredient_categories)));
            if (ingredientCategories.contains(ingredientCategory)) {
                categorySpinner.setSelection(categorySpinnerAdapter.getPosition(ingredientCategory));
            } else {
                categorySpinner.setSelection(categorySpinnerAdapter.getPosition("Other"));
                newCategoryLinearLayout.setVisibility(View.VISIBLE);
                categoryEditText.setText(ingredientCategory);
            }

            /* return the edited ingredient back to AddEditRecipeActivity */
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
            title.setText("Edit Ingredient");
            builder.setView(view)
                    .setNegativeButton("Cancel", null)
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        /* set listener for the Delete button, delete the clicked Ingredient
                        and call onDeletePressed method to delete ingredient item
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String description = descriptionText.getText().toString();
                            String amount = amountText.getText().toString();
                            String unit = unitSpinner.getSelectedItem().toString();
                            if (unit.equals("Other")) {
                                unit = unitEditText.getText().toString();
                            }
                            String category = categorySpinner.getSelectedItem().toString();
                            if (category.equals("Other")) {
                                category = categoryEditText.getText().toString();
                            }
                            listener.onDeletePressed(new IngredientInRecipe(description, amount, unit, category));
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return builder.create();
        } else {
            /* Adding new ingredient */
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
            title.setText("Add Ingredient");
            builder.setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return builder.create();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener((View v) -> {
                Boolean wantToCloseDialog = true;
                String description = descriptionText.getText().toString();
                String amount = amountText.getText().toString();
                String unit = unitSpinner.getSelectedItem().toString();
                if (unit.equals("Other")) {
                    unit = unitEditText.getText().toString();
                }
                String category = categorySpinner.getSelectedItem().toString();
                if (category.equals("Other")) {
                    category = categoryEditText.getText().toString();
                }

                if (description.equals("") || amount.equals("") || unit.equals("") || category.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "You did not enter full information.",
                            Toast.LENGTH_LONG).show();
                    wantToCloseDialog = false;
                } else {
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        IngredientInRecipe ingredient = (IngredientInRecipe) bundle.getSerializable("ingredient");
                        ingredient.setBriefDescription(description);
                        ingredient.setAmountValue(Double.parseDouble(amount));
                        ingredient.setUnit(unit);
                        ingredient.setIngredientCategory(category);
                        listener.onOkPressed(new IngredientInRecipe(description, amount, unit, category));
                    } else {
                        listener.onConfirmPressed(new IngredientInRecipe(description, amount, unit, category));
                    }
                }
                if (wantToCloseDialog) {
                    d.dismiss();
                }
            });
        }
    }
}