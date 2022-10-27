/*
* Class Name: Ingredient
* Version: 1.0
* Date: Oct 25th, 2022
* Author: Shihao Liu
* Copyright Notice:
* */

package com.example.prepear;


/**/
public class Ingredient {
    String briefDescription;
    String bestBeforeDate;
    String ingredientLocation;
    int amount;
    String unit;
    String ingredientCategory;

    public Ingredient(String briefDescription, int amount, String unit, String ingredientCategory) {
        this.briefDescription = briefDescription;
        this.amount = amount;
        this.unit = unit;
        this.ingredientCategory = ingredientCategory;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIngredientCategory() {
        return ingredientCategory;
    }

    public void setIngredientCategory(String ingredientCategory) {
        this.ingredientCategory = ingredientCategory;
    }
    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    public void setBestBeforeDate(String bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public String getIngredientLocation() {
        return ingredientLocation;
    }

    public void setIngredientLocation(String ingredientLocation) {
        this.ingredientLocation = ingredientLocation;
    }
}
