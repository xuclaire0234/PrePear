/**
 * Classname: MealPlanDailyIngredientCount
 * Version Information: 1.0.0
 * Date: 11/19/2022
 * Author: Yingyue Cao
 * Copyright Notice:
 */
package com.example.prepear.ui.ShoppingList;

import com.example.prepear.IngredientInRecipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This Class sets the controller for the shoppingList items, the controller stores the ingredients.
 * Those ingredients are of type {@link IngredientInRecipe}. The controller could also sort the
 * ingredients by its category and description. And it could reverse the order list.
 */
public class ShoppingListController {

    /**
     * This variable is private and stores all the ingredients which is of type {@link IngredientInRecipe}
     */
    private ArrayList<IngredientInRecipe> ingredients;

    /**
     * The variable is private and stores the sort sequence, which is of type {@link Integer}
     */
    private Integer sortItemRecipe;

    /**
     * This function initializes the recipe controller.
     */
    public ShoppingListController() {
        this.ingredients = new ArrayList<>();
    }

    /**
     * This function add one ingredient to the controller
     * @param ingredient is the ingredient to be added to the controller, which is of type
     *           {@link IngredientInRecipe}
     */
    public void add(IngredientInRecipe ingredient) {
        if (! ingredients.contains(ingredient)) {
            // if the ingredient is not contained in the controller, add it to the list
            this.ingredients.add(ingredient);
        }
    }

    /**
     * This function delete the ingredient from the controller
     * @param ingredient is the ingredient to be deleted from controller, which is of type
     *                   {@link IngredientInRecipe}
     */
     public void deleteIngredient(IngredientInRecipe ingredient) {
        if (this.ingredients.contains(ingredient)) {
            // if the ingredient is contained in the controller, remove it from the controller
            this.ingredients.remove(ingredient);
        }
     }

    /**
     * This function clear all the ingredients form the controller
     */
     public void clear() {
        this.ingredients.clear();
     }

    /**
     * This function gets the number of all the ingredients
     * @return the return is of type {@link Integer}
     */
     public int countIngredients() {
        return this.ingredients.size();
     }

    /**
     * This function gets all the ingredients in the controller
     * @return the return is of type {@link ArrayList}
     */
    public ArrayList<IngredientInRecipe> getIngredients() {
        return ingredients;
    }

    /**
     * This function gets the sort sequence of the controller
     * @return the return is of type {@link Integer}
     */
    public Integer getSortItemRecipe() {
        return sortItemRecipe;
    }

    /**
     * This function sets the sort sequence of the controller
     * @param sortItemRecipe is the sequence the list should be sorted by
     */
    public void setSortItemRecipe(int sortItemRecipe) {
        this.sortItemRecipe = sortItemRecipe;
    }

    /**
     * This function gets the ingredient at a certain position in the list
     * @param index is the index of the ingredient in the list
     * @return the return is of type {@link IngredientInRecipe}
     */
    public IngredientInRecipe getIngredientAt (int index) {
        // If the index is not out of the range, return the ingredient in that index
        if (ingredients.size() > index && index > -1) {
            return ingredients.get(index);
        } else {
            return null;
        }
    }

    /**
     * This function sorts the list of the ingredients
     * @param sortItemRecipe is the sequence that the ingredients should be sorted by
     */
    public void sortIngredient(int sortItemRecipe) {
        this.sortItemRecipe = sortItemRecipe; // set the sort sequence attributes
        if (sortItemRecipe == 0) {
            // if the index of the sequence is 0, sort the list by the ingredient's brief description
            Collections.sort(this.ingredients, new Comparator<IngredientInRecipe>() {
                @Override
                public int compare(IngredientInRecipe ingredient, IngredientInRecipe t1) {
                    return ingredient.getBriefDescription().compareTo(t1.getBriefDescription());
                }
            });
        }else if (sortItemRecipe == 1) {
            // if the index of the sequence is 1, sort the list by the ingredient's category
            Collections.sort(this.ingredients, new Comparator<IngredientInRecipe>() {
                @Override
                public int compare(IngredientInRecipe ingredient, IngredientInRecipe t1) {
                    return ingredient.getIngredientCategory().compareTo(t1.getIngredientCategory());
                }
            });
        }
    }

    /**
     * This function reverse the sequence of the list of the ingredients
     */
    public void reverseOrder() {
        Collections.reverse(this.ingredients);
    }
}
