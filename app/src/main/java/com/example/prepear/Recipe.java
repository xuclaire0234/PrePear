/**
 * Classname: Recipe
 * Version Information: 1.0.0
 * Date: 11/2/2022
 * Author: Yingyue Cao
 * Copyright notice:
 */

package com.example.prepear;

import android.net.Uri;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This Class creates an object to represent a recipe with its image URI {@link String},
 * title {@link String}, preparation time {@link Integer}, number of servings {@link Integer},
 * recipe category {@link String}, comments {@link String}, list of its ingredients {@link ArrayList}
 * and id to stored in the database {@link String}
 * @author Yingyue
 * @version 1.0
 */
public class Recipe implements Serializable {

    /**
     * This variable is private and stores uri of recipe's image as {@link String}
     */
    private String imageURI;
    /**
     * This variable is private and stores title of recipe as {@link String}
     */
    private String title;
    /**
     * This variable is private and stores preparation time of recipe as {@link Integer}
     */
    private Integer preparationTime;
    /**
     * This variable is private and stores number of serving as {@link Integer}
     */
    private Integer numberOfServings;
    /**
     * This variable is private and stores the category of recipe as {@link String}
     */
    private String recipeCategory;
    /**
     * This variable is private and stores the comment of the recipe as {@link String}
     */
    private String comments;
    /**
     * This variable is private and stores all the ingredients needed by this recipe as {@link ArrayList}
     */
    private ArrayList<IngredientInRecipe> listOfIngredients;
    /**
     * This variable is private and stores the id of the recipe as {@link String}
     */
    private String id;

    /**
     * This is the constructor to create the object of Recipe
     * @param imageURI This is uri of the image which is of type {@link String}
     * @param title This is the title of the image which is of type {@link String}
     * @param preparationTime This is the preparation time of the image which is of type {@link Integer}
     * @param numberOfServings This is the number of serving of the image which is of type {@link Integer}
     * @param recipeCategory This is the category of the recipe of the image which is of type {@link String}
     * @param comments This is the comment of the recipe of the image which is of type {@link String}
     */
    public Recipe(String imageURI, String title, Integer preparationTime,Integer numberOfServings, String recipeCategory, String comments) {
        this.imageURI = imageURI;
        this.title = title;
        this.preparationTime = preparationTime;
        this.numberOfServings = numberOfServings;
        this.recipeCategory = recipeCategory;
        this.comments = comments;
        this.listOfIngredients = new ArrayList<IngredientInRecipe>();
    }

    /**
     * This function returns the uri of image
     * @return The return type is of type {@link String}
     */
    public String getImageURI() {
        return this.imageURI;
    }

    /**
     * This function sets the uri of the image
     * @param imageURI This is the uri of the image which is of type {@link String}
     */
    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    /**
     * This function returns the title of the recipe
     * @return The return type is of type {@link String}
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * This function sets the title of the image
     * @param title This is the title of the recipe, which is of type {@link String}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This function returns the preparation time of the recipe
     * @return The return is of type {@link Integer}
     */
    public Integer getPreparationTime() {
        return this.preparationTime;
    }

    /**
     * This function sets the preparation time
     * @param preparationTime This is the preparation time of the recipe, which is of type {@link Integer}
     */
    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    /**
     * This function returns the number of serving
     * @return The return is of type {@link Integer}
     */
    public Integer getNumberOfServings() {
        return this.numberOfServings;
    }

    /**
     * This function sets the number of serving
     * @param numberOfServings This is the number of serving, which is of type {@link Integer}
     */
    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    /**
     * This function returns the category of the recipe
     * @return The return is of type {@link String}
     */
    public String getRecipeCategory() {
        return this.recipeCategory;
    }

    /**
     * This function sets the category of the recipe
     * @param recipeCategory This is the recipe category, which is of type {@link String}
     */
    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    /**
     * This function returns the comment of the recipe
     * @return The return is of type {@link String}
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * This function sets the comment of the recipe
     * @param comments This is the comment of the recipe, which is of type {@link String}
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * This function gets all the ingredients of the recipe
     * @return The return is of type {@link ArrayList}
     */
    public ArrayList<IngredientInRecipe> getListOfIngredients() {
        return this.listOfIngredients;
    }

    /**
     * This function sets all the ingredients of the recipe
     * @param listOfIngredients This is the list of all ingredients in the recipe, which is of type
     *                          {@link ArrayList}
     */
    public void setListOfIngredients(ArrayList<IngredientInRecipe> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    /**
     * This function returns the ingredient at a certain location of the recipe
     * @param indexOfIngredient This is the index of the ingredient's location, which is of type
     *                          {@link Integer}
     * @return
     */
    public IngredientInRecipe getIngredientByIndex(Integer indexOfIngredient) {
        return this.listOfIngredients.get(indexOfIngredient);
    }

    /**
     * This function adds a single ingredients to the recipe
     * @param ingredient This is the ingredients needed to add to the recipe, which is of type
     *                          {@link IngredientInRecipe}
     */
    public void addIngredientToRecipe(IngredientInRecipe ingredient){
        this.listOfIngredients.add(ingredient);
    }

    /**
     * This function deletes all the ingredients in a recipe
     */
    public void deleteAllIngredients(){
        this.listOfIngredients = new ArrayList<IngredientInRecipe>();
    }

    /**
     * This function returns the id of the recipe in the database
     * @return The return is of type {@link String}
     */
    public String getId() {
        return this.id;
    }

    /**
     * This function sets the id of the recipe in the database
     * @param id This is the id of the recipe in the database, which is of type {@link String}
     */
    public void setId(String id) {
        this.id = id;
    }
}
