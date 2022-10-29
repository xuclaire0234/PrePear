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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomRecipeList extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private Context context;
    private int sortItemRecipe = 0;

    public CustomRecipeList(Context context, ArrayList<Recipe> recipes) {
        super(context,0, recipes);
        this.recipes = recipes;
        this.context = context;
    }


    public int getSortItemRecipe() {
        return this.sortItemRecipe;
    }
    public void setSortItemRecipe(int index) {this.sortItemRecipe = index;}

    public void sortRecipe(int index) {
        this.sortItemRecipe = index;
        if (index == 0) {
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getTitle().compareTo(t1.getTitle());
                }
            });
        }else if (index == 1) {
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getPreparationTime().compareTo(t1.getPreparationTime());
                }
            });
        }else if (index == 2) {
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getNumberOfServings().compareTo(t1.getNumberOfServings());
                }
            });
        }else if (index == 3) {
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getRecipeCategory().compareTo(t1.getRecipeCategory());
                }
            });
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_recipe, parent,false);
        }

        Recipe recipe = recipes.get(position);

        TextView recipeTitle = view.findViewById(R.id.recipe_text);
        TextView recipeNumberOfServings = view.findViewById(R.id.number_of_servings_texts_view_recipe_list);
        TextView recipePreparationTime = view.findViewById(R.id.preparation_time_texts_view_recipe_list);
        TextView recipeCategory = view.findViewById(R.id.recipe_category_texts_view_recipe_list);
        ImageView recipeImageView = view.findViewById(R.id.imageView_in_recipe_list);

        recipeNumberOfServings.setTextColor(Color.parseColor("#ACACAC"));
        recipePreparationTime.setTextColor(Color.parseColor("#ACACAC"));
        recipeCategory.setTextColor(Color.parseColor("#ACACAC"));


        recipeTitle.setText(recipe.getTitle());
        recipeNumberOfServings.setText("Number Of Servings: " + recipe.getNumberOfServings().toString());
        recipePreparationTime.setText("Preparation Time: "+recipe.getPreparationTime().toString());
        recipeCategory.setText("Recipe Category: " + recipe.getRecipeCategory());
        Glide.with(getContext())
                .load(recipe.getImageURI()).into(recipeImageView);
        if (this.sortItemRecipe == 0) {

        }else if (this.sortItemRecipe == 1) {
            recipePreparationTime.setTextColor(Color.parseColor("#FF000000"));
        }else if (this.sortItemRecipe == 2) {
            recipeNumberOfServings.setTextColor(Color.parseColor("#FF000000"));
        }else if (this.sortItemRecipe == 3) {
            recipeCategory.setTextColor(Color.parseColor("#FF000000"));
        }

        return view;

    }
}