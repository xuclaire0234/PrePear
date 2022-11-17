/**
 * Classname: ShoppingListViewIngredientFragment
 * Version Information: 1.0.0
 * Date: 11/16/2022
 * Author: Jamie Lee
 * Copyright notice:
 */
package com.example.prepear;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

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
    }
}
