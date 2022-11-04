/**
 * Classname: DatabaseController
 * Version Information: 1.0.0
 * Date: 11/3/2022
 * Author: Yingyue Cao
 * Copyright Notice:
 */
package com.example.prepear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecipeController {

    /**
     * Create list for Recipe class
     */
    private ArrayList<Recipe> recipes;
    private Integer sortItemRecipe;

    /**
     * This function initializes the recipe controller.
     */
    public RecipeController() {
        this.recipes = new ArrayList<Recipe>();
    }

    /**
     * This function add a new recipe to recipes list
     */
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    /**
     * This function delete existed recipe in recipes list
     */
    public void deleteRecipe(Recipe recipe) {
        if (recipes.contains(recipe)) {
            this.recipes.remove(recipe);
        }
    }

    /**
     * This function edit existed recipe in recipes list
     */
    public void editRecipe(Recipe recipeToEdit, Recipe newestVersionOfRecipe) {
        if (this.recipes.contains(recipeToEdit)){
            Integer indexOfRecipeToEdit;
            indexOfRecipeToEdit = this.recipes.indexOf(recipeToEdit);
            this.recipes.set(indexOfRecipeToEdit,newestVersionOfRecipe);
        }
    }

    public void editRecipe(Integer index, Recipe newestVersionOfRecipe) {
        this.recipes.set(index,newestVersionOfRecipe);
    }

    /**
     * This function returns the number of recipe in recipes list
     * @return The return is of type {@link int}
     */
    public int countRecipes() {
        return this.recipes.size();
    }

    /**
     * This function returns the list of recipes
     * @return The return is of type {@link ArrayList}
     */
    public ArrayList<Recipe> getRecipes() {
        return this.recipes;
    }

    /**
     * This function set the list of recipes
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * This function returns the recipe at given index
     * @return The return is of type {@link Recipe}
     */
    public Recipe getRecipeAt(Integer index) {
        if (index < this.recipes.size()) {
            return this.recipes.get(index);
        } else {
            return null;
        }
    }

    /**
     * This function returns index of given recipe
     * @return The return is of type {@link Integer}
     */
    public Integer getRecipeIndex(Recipe recipe) {
        if (this.recipes.contains(recipe)) {
            return recipes.indexOf(recipe);
        }else {
            return -1;
        }
    }

    /**
     * This function clear the recipes in the list
     */
    public void clearAllRecipes() {
        this.recipes.clear();
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

    /**
     * This function sorts the recipe list
     * @param sortItemRecipe his is the item the recipes list should be sorted by, which is of type
     *       {@link Integer}
     */
    public void sortRecipe(int sortItemRecipe) {
        this.sortItemRecipe = sortItemRecipe;
        if (sortItemRecipe == 0) {
            /* If the sort item was 0, this means that the user require the recipe to be sorted by
             * the title */
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getTitle().compareTo(t1.getTitle());
                }
            }); /* Sort the recipe by title */
        }else if (sortItemRecipe == 1) {
            /* If the sort item was 1, this means that the user require the recipe to be sorted by
             * the reparation time */
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getPreparationTime().compareTo(t1.getPreparationTime());
                }
            }); /* Sort the recipe by preparation time */
        }else if (sortItemRecipe == 2) {
            /* If the sort item was 2, this means that the user require the recipe to be sorted by
             * the number of servings */
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getNumberOfServings().compareTo(t1.getNumberOfServings());
                }
            }); /* Sort the recipe by number of servings */
        }else if (sortItemRecipe == 3) {
            /* If the sort item was 3, this means that the user require the recipe to be sorted by
             * the recipe category */
            Collections.sort(this.recipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getRecipeCategory().compareTo(t1.getRecipeCategory());
                }
            }); /* Sort the recipe by recipe category */
        }
    }


}