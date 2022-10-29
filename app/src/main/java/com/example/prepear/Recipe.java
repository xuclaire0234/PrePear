package com.example.prepear;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    String imageURI;
    String title;
    Integer preparationTime;
    Integer numberOfServings;
    String recipeCategory;
    String comments;
    ArrayList<IngredientInRecipe> listOfIngredients;

    public Recipe(String imageURI, String title, Integer preparationTime,Integer numberOfServings, String recipeCategory, String comments) {
        this.imageURI = imageURI;
        this.title = title;
        this.preparationTime = preparationTime;
        this.numberOfServings = numberOfServings;
        this.recipeCategory = recipeCategory;
        this.comments = comments;
        this.listOfIngredients = new ArrayList<IngredientInRecipe>();
    }

    public String getImageURI() {
        return this.imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPreparationTime() {
        return this.preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Integer getNumberOfServings() {
        return this.numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public String getRecipeCategory() {
        return this.recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<IngredientInRecipe> getListOfIngredients() {
        return this.listOfIngredients;
    }

    public void setListOfIngredients(ArrayList<IngredientInRecipe> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public IngredientInRecipe getIngredientByIndex(Integer indexOfIngredient) {
        return this.listOfIngredients.get(indexOfIngredient);
    }

    public void addIngredientToRecipe(IngredientInRecipe ingredient){
        this.listOfIngredients.add(ingredient);
    }
}
