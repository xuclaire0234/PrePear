/*
 * Class Name: IngredientInStorage
 * Version: 1.0
 * Date: Oct 25th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 * */

package com.example.prepear;



/**/
public class IngredientInStorage extends Ingredient{
    private String bestBeforeDate;
    private String location;

    public IngredientInStorage(String briefDescription, String bestBeforeDate, String location, String unit, int amount, String ingredientCategory) {
        super(briefDescription, amount, unit, ingredientCategory);
        this.bestBeforeDate = bestBeforeDate;
        this.ingredientCategory = ingredientCategory;

    }

}
