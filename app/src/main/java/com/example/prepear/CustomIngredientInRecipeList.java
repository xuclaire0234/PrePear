/**
* Classname: CustomIngredientInRecipeList
* Version Information: 1.0.0
* Date: 11/2/2022
* Author: Jiayin He
* Copyright notice:
 */

package com.example.prepear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class defines the custom ingredient in recipe list that shows the listview of all ingredients
 * that a recipe needs.
 */
public class CustomIngredientInRecipeList extends ArrayAdapter<IngredientInRecipe> {
    private ArrayList<IngredientInRecipe> ingredientsInRecipe;
    private Context context;

    /**
     * This initializes the new custom ingredient in recipe list.
     * @param context
     * @param ingredientsInRecipe
     */
    public CustomIngredientInRecipeList(Context context, ArrayList<IngredientInRecipe> ingredientsInRecipe) {
        super(context, 0, ingredientsInRecipe);
        this.ingredientsInRecipe = ingredientsInRecipe;
        this.context = context;
    }

    /**
     * This gets the listview of the ingredient in recipe list.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        /* check error */
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_ingredient_in_recipe, parent, false);
        }

        IngredientInRecipe ingredientInRecipe = ingredientsInRecipe.get(position);

        /* connects the the layout with the views */
        TextView briefDescriptionTextView = view.findViewById(R.id.brief_description_TextView);
        TextView amountTextView = view.findViewById(R.id.amount_TextView);
        TextView unitTextView = view.findViewById(R.id.unit_TextView);
        TextView ingredientCategoryTextView = view.findViewById(R.id.ingredient_category_TextView);

        /* sets the detailed information to the view */
        briefDescriptionTextView.setText(ingredientInRecipe.getBriefDescription());
        amountTextView.setText(String.valueOf(ingredientInRecipe.getAmountString()));
        unitTextView.setText(ingredientInRecipe.getUnit());
        ingredientCategoryTextView.setText(ingredientInRecipe.getIngredientCategory());

        return view;
    }
}
