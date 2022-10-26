/*
 * Class Name: RecipeAddIngredientFragment
 * Version Information: Version 1.0
 * Date: Oct 26th, 2022
 * Author: Jamie Lee
 */
package com.example.prepear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import javax.annotation.Nullable;

/*

 */
public class RecipeEditIngredientFragment {
    // declare variables
    private EditText descriptionText;
    private EditText amountText;
    private EditText unitText;
    private EditText categoryText;
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

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_ingredient_fragment, null);
        descriptionText = view.findViewById(R.id.description_edit_text);
        amountText = view.findViewById(R.id.ingredient_amount_edit_text);
        unitText = view.findViewById(R.id.ingredient_unit_edit_text);
        categoryText = view.findViewById(R.id.ingredient_category_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = getArguments();

        IngredientInRecipe ingredient = (IngredientInRecipe) bundle.getSerializable("ingredient");
        descriptionText.setText(ingredient.getDescription());
        amountText.setText(ingredient.getAmount());
        unitText.setText(ingredient.getUnit());
        return builder
                .setView(view)
                .setTitle("View/Edit Ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = descriptionText.getText().toString();
                        String amount = amountText.getText().toString();
                        String unit = unitText.getText().toString();
                        String category = categoryText.getText().toString();
                        listener.onDeletePressed(new IngredientInRecipe(description, amount, unit, category));
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = descriptionText.getText().toString();
                        String amount = amountText.getText().toString();
                        String unit = unitText.getText().toString();
                        String category = categoryText.getText().toString();
                        ingredient.setDescription(description);
                        ingredient.setAmount(amount);
                        ingredient.setUnit(unit);
                        ingredient.setCategory(category);
                        listener.onOkPressed(new IngredientInRecipe(description, amount, unit, category));
                    }
                }).create();
    }
}
