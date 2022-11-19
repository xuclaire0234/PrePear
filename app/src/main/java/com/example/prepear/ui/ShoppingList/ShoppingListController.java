package com.example.prepear.ui.ShoppingList;

import com.example.prepear.IngredientInRecipe;
import com.example.prepear.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShoppingListController {
    private ArrayList<IngredientInRecipe> ingredients;
    private Integer sortItemRecipe;

    public ShoppingListController() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(IngredientInRecipe ingredient) {
        this.ingredients.add(ingredient);
    }
     public void deleteRecipe(IngredientInRecipe ingredient) {
        if (this.ingredients.contains(ingredient)) {
            this.ingredients.remove(ingredient);
        }
     }

     public void clear() {
        this.ingredients.clear();
     }

     public int countIngredients() {
        return this.ingredients.size();
     }


    public ArrayList<IngredientInRecipe> getIngredients() {
        return ingredients;
    }

    public Integer getSortItemRecipe() {
        return sortItemRecipe;
    }

    public void setSortItemRecipe(int sortItemRecipe) {
        this.sortItemRecipe = sortItemRecipe;
    }

    public IngredientInRecipe getIngredientAt (int index) {
        return ingredients.get(index);
    }

    public void sortIngredient(int sortItemRecipe) {
        if (sortItemRecipe == 0) {
            Collections.sort(this.ingredients, new Comparator<IngredientInRecipe>() {
                @Override
                public int compare(IngredientInRecipe ingredient, IngredientInRecipe t1) {
                    return ingredient.getBriefDescription().compareTo(t1.getBriefDescription());
                }
            });
        }else if (sortItemRecipe == 1) {
            Collections.sort(this.ingredients, new Comparator<IngredientInRecipe>() {
                @Override
                public int compare(IngredientInRecipe ingredient, IngredientInRecipe t1) {
                    return ingredient.getIngredientCategory().compareTo(t1.getIngredientCategory());
                }
            });
        }
    }
}
