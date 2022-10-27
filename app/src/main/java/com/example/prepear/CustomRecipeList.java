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
        TextView sortValue = view.findViewById(R.id.sort_value_text);

        recipeTitle.setText(recipe.getTitle());
        if (this.sortItemRecipe == 0) {
            sortValue.setText("");
        }else if (this.sortItemRecipe == 1) {
            sortValue.setText(String.valueOf(recipe.getPreparationTime()));
        }else if (this.sortItemRecipe == 2) {
            sortValue.setText(String.valueOf(recipe.getNumberOfServings()));
        }else if (this.sortItemRecipe == 3) {
            sortValue.setText(recipe.getRecipeCategory());
        }

        return view;

    }
}