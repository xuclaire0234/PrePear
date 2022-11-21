/**
 * Classname: ShoppingListViewIngredientFragment
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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingListViewIngredientFragment extends DialogFragment {
    // declare variables
    private ArrayAdapter<CharSequence> unitSpinnerAdapter;
    private ArrayAdapter<CharSequence> categorySpinnerAdapter;
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



    /**
     * This method creates a new instance of ShoppingListViewIngredient so user can view
     * the ingredient in given time period of shopping list
     * @param ingredient {@link IngredientInStorage} that the user clicked on
     * @return fragment the newly created fragment
     */
    static ShoppingListViewIngredientFragment newInstance(IngredientInStorage ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);
        ShoppingListViewIngredientFragment fragment = new ShoppingListViewIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method creates the view ingredient fragment
     * @return builder a {@link Dialog} object to build the fragment
     */
    @SuppressLint("MissingInflatedId")
    @NonNull
    public void onCreateDialog() {
        // connects views to its layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.shopping_list_view_ingredient_details, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_ingredient_fragments_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading);
        descriptionText = view.findViewById(R.id.brief_description);
        amountText = view.findViewById(R.id.ingredient_amount_edit_text);
        unitSpinner = view.findViewById(R.id.ingredient_unit_edit_text);
        unitEditText = view.findViewById(R.id.new_ingredient_unit_edit_text);
        newUnitLinearLayout = view.findViewById(R.id.new_unit_linear_layout);
        categorySpinner = view.findViewById(R.id.ingredient_category_edit_text);
        categoryEditText = view.findViewById(R.id.new_ingredient_category_edit_text);
        newCategoryLinearLayout = view.findViewById(R.id.new_ingredient_category_linear_layout);

        // getting attributes from ingredient
        Bundle bundle = getArguments();
        IngredientInStorage ingredient = (IngredientInStorage) bundle.getSerializable("ingredient");
        descriptionText.setText(ingredient.getBriefDescription());
        amountText.setText(ingredient.getAmountString());
        unitSpinner.setSelection(unitSpinnerAdapter.getPosition(ingredient.getUnit()));;
        categorySpinner.setSelection(categorySpinnerAdapter.getPosition(ingredient.getIngredientCategory()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("View Ingredient");
        builder.setView(view).setNegativeButton("OK", null);
    }
}
