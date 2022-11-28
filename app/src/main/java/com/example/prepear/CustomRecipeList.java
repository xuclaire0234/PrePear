/**
 * Classname: CustomRecipeList
 * Version Information: 1.0.0
 * Date: 11/2/2022
 * Author: Yingyue Cao
 * Copyright notice:
 */
package com.example.prepear;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

/**
 * This is a class that keeps track of a list of {@link Recipe} object and set the way Recipe shown
 * on the ViewRecipeListActivity
 */
public class CustomRecipeList extends ArrayAdapter<Recipe> {

    /**
     * Thia variable keeps list of objects that are of type {@link Recipe}
     */
    private RecipeController recipes;
    /**
     * This variable set the context which is of type {@link Context}
     */
    private Context context;
    /**
     * This variable stores the way the list of Recipe needed to be sorted, which is of type
     * {@link Integer}
     */
    private int sortItemRecipe = 0;

    /**
     * This is the constructor to create the object of CustomCityList
     * @param context This is the context which is of type {@link Context}
     * @param recipes This is the list of recipes which is of type {@link Recipe}
     */
    public CustomRecipeList(Context context, RecipeController recipes) {
        super(context,0, recipes.getRecipes());
        this.recipes = recipes;
        this.context = context;
    }

    /**
     * This function sets the style of the listView to view recipes
     * @param position This is the index of the recipe needed to view, which is of type {@link Integer}
     * @param convertView This is the convert view which is of type {@link View}
     * @param parent This is the parent view which is of type {@link ViewGroup}
     * @return The return is of type {@link View}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            /* If the convertView is not null, link the view with the content_recipe.xml */
            view = LayoutInflater.from(context).inflate(R.layout.content_recipe, parent,false);
        }

        /*
         * All variables are defined and assigned value here below
         */
        Recipe recipe = recipes.getRecipeAt(position); /* This variable stores the specific Recipe needed to
        * show on the ListView */
        TextView recipeTitle = view.findViewById
                (R.id.recipe_text); /* This variable linked to the textview to show the title */
        TextView recipeNumberOfServings = view.findViewById
                (R.id.number_of_servings_texts_view_recipe_list); /* This variable linked to the
         * TextView to show the number of servings */
        TextView recipePreparationTime = view.findViewById
                (R.id.preparation_time_texts_view_recipe_list); /* This variable linked to the
         * TextView to show the preparation time */
        TextView recipeCategory = view.findViewById
                (R.id.recipe_category_texts_view_recipe_list); /* This variable linked to the
         * TextView to show the recipe category */
        ImageView recipeImageView = view.findViewById
                (R.id.imageView_in_recipe_list); /* This variable linked to the ImageView to show
         * the image of the recipe */

        /*
         * Set colors grey to all the textview except for the textview to show the title
         */
        recipeNumberOfServings.setTextColor(Color.parseColor("#ACACAC"));
        recipePreparationTime.setTextColor(Color.parseColor("#ACACAC"));
        recipeCategory.setTextColor(Color.parseColor("#ACACAC"));

        /*
         * Get and set values to title, number of servings, preparation time and recipe category
         */
        recipeTitle.setText(recipe.getTitle());
        recipeNumberOfServings.setText("Number Of Servings: " + recipe.getNumberOfServings().toString());
        recipePreparationTime.setText("Preparation Time: "+recipe.getPreparationTime().toString());
        recipeCategory.setText("Recipe Category: " + recipe.getRecipeCategory());
        /* Load and set the image from Firebase storage by URI and set to the imageview */
        Glide.with(getContext())
                .load(recipe.getImageURI()).into(recipeImageView);

        /*
         * Set the format of the listView when different types of sort are applied
         */
        if (this.sortItemRecipe == 0) {
            /* If the user wanted to sort by title, nothing would change */
        }else if (this.sortItemRecipe == 1) {
            /* If the user wanted to sort by preparation time, the text for preparation time should
            * be set to black */
            recipePreparationTime.setTextColor(Color.parseColor("#FF000000"));
        }else if (this.sortItemRecipe == 2) {
            /* If the user wanted to sort by number of servings, the text for number of servings
             * should be set to black */
            recipeNumberOfServings.setTextColor(Color.parseColor("#FF000000"));
        }else if (this.sortItemRecipe == 3) {
            /* If the user wanted to sort by ingredient category, the text for ingredient category
             * should be set to black */
            recipeCategory.setTextColor(Color.parseColor("#FF000000"));
        }

        return view;
    }

    /**
     * This function returns the item the recipes list should be sorted by
     * @return The return is of type {@link Integer}
     */
    public int getSortItemRecipe() {
        return this.sortItemRecipe;
    }

    /**
     * This function sets the item the recipes list should be sorted by
     * @param sortItemRecipe This is the item the recipes list should be sorted by, which is of type
     *       {@link Integer}
     */
    public void setSortItemRecipe(int sortItemRecipe) {this.sortItemRecipe = sortItemRecipe;}
}