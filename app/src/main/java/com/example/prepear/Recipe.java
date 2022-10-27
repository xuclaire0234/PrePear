package com.example.prepear;

import java.util.ArrayList;

public class Recipe {
    String title;
    Integer preparationTime;
    Integer numberOfServings;
    String recipeCategory;
    String comments;
    ArrayList<Ingredient> listOfIngredients;

    public Recipe(String title, Integer preparationTime,Integer numberOfServings, String recipeCategory, String comments) {
        this.title = title;
        this.preparationTime = preparationTime;
        this.numberOfServings = numberOfServings;
        this.recipeCategory = recipeCategory;
        this.comments = comments;
        this.listOfIngredients = new ArrayList<Ingredient>();
    }


    public Ingredient getIngredientsByIndex(Integer indexOfIngredient) {
        return this.listOfIngredients.get(indexOfIngredient);
    }

    public int getIngredientsSize(){
        return this.listOfIngredients.size();
    }

    public void setIngredient(Ingredient ingredient){
        this.listOfIngredients.add(ingredient);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

