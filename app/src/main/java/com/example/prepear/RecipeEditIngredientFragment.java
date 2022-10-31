/*
 * Class Name: RecipeAddIngredientFragment
 * Version Information: Version 1.0
 * Date: Oct 26th, 2022
 * Author: Jamie Lee
 */
package com.example.prepear;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import javax.annotation.Nullable;

/*
 */
public class RecipeEditIngredientFragment extends DialogFragment {
    // declare variables
    private ArrayAdapter<CharSequence> unitSpinnerAdapter;
    private ArrayAdapter<CharSequence> categorySpinnerAdapter;
    private EditText descriptionText;
    private EditText amountText;
    private Spinner unitSpinner;
    private Spinner categorySpinner;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onDeletePressed(IngredientInRecipe ingredient);
        void onOkPressed(IngredientInRecipe ingredient);
    }

    static RecipeEditIngredientFragment newInstance(IngredientInRecipe ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        RecipeEditIngredientFragment fragment = new RecipeEditIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_ingredient_fragment, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_ingredient_fragments_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading);
        descriptionText = view.findViewById(R.id.description_edit_text);
        amountText = view.findViewById(R.id.ingredient_amount_edit_text);
        unitSpinner = view.findViewById(R.id.ingredient_unit_edit_text);
        categorySpinner = view.findViewById(R.id.ingredient_category_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("Edit Ingredient");
        Bundle bundle = getArguments();

        unitSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.units,
                android.R.layout.simple_spinner_item);
        unitSpinner.setAdapter(unitSpinnerAdapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ingredient_categories,
                android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        IngredientInRecipe ingredient = (IngredientInRecipe) bundle.getSerializable("ingredient");
        descriptionText.setText(ingredient.getBriefDescription());
        amountText.setText(ingredient.getAmount().toString());
        unitSpinner.setSelection(unitSpinnerAdapter.getPosition(ingredient.getUnit()));
        categorySpinner.setSelection(categorySpinnerAdapter.getPosition(ingredient.getIngredientCategory()));

        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = descriptionText.getText().toString();
                        int amount = Integer.parseInt(amountText.getText().toString());
                        String unit = unitSpinner.getSelectedItem().toString();
                        String category = categorySpinner.getSelectedItem().toString();
                        listener.onDeletePressed(new IngredientInRecipe(description, amount, unit, category));
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = descriptionText.getText().toString();
                        int amount = Integer.parseInt(amountText.getText().toString());
                        String unit = unitSpinner.getSelectedItem().toString();
                        String category = categorySpinner.getSelectedItem().toString();
                        ingredient.setBriefDescription(description);
                        ingredient.setAmount(amount);
                        ingredient.setUnit(unit);
                        ingredient.setIngredientCategory(category);
                        listener.onOkPressed(new IngredientInRecipe(description, amount, unit, category));
                    }
                }).create();
    }
}