package com.example.prepear;

import java.io.Serializable;

import io.grpc.internal.DnsNameResolver;

public class IngredientInRecipe implements Serializable {
    String briefDescription;
    Integer amount;
    String unit;
    String ingredientCategory;
    String id;

    public IngredientInRecipe(String briefDescription, int amount, String unit, String ingredientCategory) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
