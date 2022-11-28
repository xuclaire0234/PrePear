/**
 * Classname: IngredientController
 * Version Information: 1.0.0
 * Date: 11/10/2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/*
 * This class is responsible for adding, deleting, and editing ingredients in the
 * ingredientStorageDataList, as well as sorting ingredients based on the user selected option
 * */
public class IngredientController {
    private ArrayList<IngredientInStorage> ingredientStorageDataList;

    /**
     * This function initializes the ingredient controller.
     * @param ingredientStorageDataList a {@link ArrayList} store all the ingredient in storage
     */
    public IngredientController(ArrayList<IngredientInStorage> ingredientStorageDataList){
        this.ingredientStorageDataList = ingredientStorageDataList;
    }

    /**
     * another constructor without parameter
     */
    public IngredientController(){
        this.ingredientStorageDataList = new ArrayList<IngredientInStorage>();
    }


    /**
     * This function add a new ingredient to the ingredient data list
     * @param ingredientToAdd a {@link IngredientInStorage} is needed to be added into ingredientStorageDataList
     */
    public void addIngredient(IngredientInStorage ingredientToAdd) {
        this.ingredientStorageDataList.add(ingredientToAdd);
    }


    /**
     * This function replaces an {@link IngredientInStorage} in the data list with a new {@link IngredientInStorage}
     * @param index a {@link Integer} as index for ingredientStorageDateList
     * @param ingredientToEdit a {@link IngredientInStorage} needed to be edited
     */
    public void replaceIngredient(int index, IngredientInStorage ingredientToEdit) {
        this.ingredientStorageDataList.set(index, ingredientToEdit);
    }

    /**
     * This function delete existed {@link IngredientInStorage} in the ingredientStorageDataList
     * @param ingredientToDelete a {@link IngredientInStorage} needed to be removed
     */
    public void removeIngredient(IngredientInStorage ingredientToDelete) {
        this.ingredientStorageDataList.remove(ingredientToDelete);
    }

    /**
     * This function returns the list of {@link IngredientInStorage}
     * @return The return is of type {@link ArrayList}
     */
    public ArrayList<IngredientInStorage> getIngredients() {
        return this.ingredientStorageDataList;
    }


    /**
     * This method sorts all {@link IngredientInStorage} according to the user's selection made for sorting
     * @param userSelectedSortChoice a {@link String} as the user's selection for sorting all {@link IngredientInStorage}
     */
    public void SortInStorageIngredients(String userSelectedSortChoice){
        if (Objects.equals(userSelectedSortChoice, "description(ascending)")) {
            // sort by ingredient's description A->Z alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBriefDescription().compareTo(ingredient2.getBriefDescription());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "description(descending)")) {
            // sort by ingredient's description Z->A alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBriefDescription().compareTo(ingredient1.getBriefDescription());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "best before (oldest to newest)")) {
            // sort by ingredient's best before date from the most recently expired to the most newest expired
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBestBeforeDate().compareTo(ingredient2.getBestBeforeDate());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "best before (newest to oldest)")) {
            // sort by ingredient's best before date from the most newest expired to the most recently expired
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBestBeforeDate().compareTo(ingredient1.getBestBeforeDate());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "location(ascending)")) {
            // sort by ingredient's location A->Z alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getLocation().compareTo(ingredient2.getLocation());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "location(descending)")) {
            // sort by ingredient's location Z->A alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getLocation().compareTo(ingredient1.getLocation());
                }
            });
        }
        else if (Objects.equals(userSelectedSortChoice, "category(ascending)")) {
            // sort by ingredient's category A->Z alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getIngredientCategory().compareTo(ingredient2.getIngredientCategory());
                }
            });
        } else if (Objects.equals(userSelectedSortChoice, "category(descending)")) {
            // sort by ingredient's category Z->A alphabetically
            Collections.sort(this.ingredientStorageDataList, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getIngredientCategory().compareTo(ingredient1.getIngredientCategory());
                }
            });
        }
    }
}
