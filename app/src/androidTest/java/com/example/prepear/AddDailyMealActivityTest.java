/**
 * Class Name: AddDailyMealActivityTest
 * Version Information: Version 1.0
 * Date: Nov 25, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * This class tests the AddDailyMealActivity class using Robotium
 */
public class AddDailyMealActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Run before each test to set up activities.
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Test adding a daily meal from the ingredient storage
     */
    @Test
    @SuppressWarnings("deprecation")
    public void addMealFromIngredientStorage(){
        // Add a daily meal plan
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        // enter start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), "2023-09-10");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), "2023-09-10");
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnText("CONFIRM");
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.clickOnButton("CONFIRM");
        // click on the entered daily meal plan
        solo.clickOnText("2023-09-10");
        // click the add meal button
        solo.clickOnButton("Add A Meal");
        // check if the amount edit text is invisible, and the add meal button is disabled
        assertEquals(View.INVISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        assertFalse(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        // click add meal from ingredient storage button
        solo.clickOnButton("Pick a meal from Ingredient Storage");
        // select meal and press confirm
        solo.clickInList(1);
        solo.clickOnText("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        assertTrue(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        assertEquals(View.VISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        // test leaving the amount empty
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "");
        solo.clickOnButton("Add this meal in");
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering 0 for the amount
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "0");
        solo.clickOnButton("Add this meal in");
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering a valid amount
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "3");
        // add meal
        solo.clickOnButton("Add this meal in");
        solo.assertCurrentActivity("Wrong Activity", ViewDailyMealPlanActivity.class);
        // check if meal is added to the list
        assertTrue(solo.searchText("Amount: 3"));
        solo.clickOnButton("BACK");
        // check that the meal plan was added to the list
        assertTrue(solo.searchText("2023-09-10"));
        // delete item added
        solo.clickLongOnText("2023-09-10");
        solo.clickOnButton("Confirm");
    }

    /**
     * Test adding a daily meal from Recipes
     */
    @Test
    @SuppressWarnings("deprecation")
    public void addMealFromRecipes(){
        // Add a daily meal plan
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        // enter start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), "2023-09-10");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), "2023-09-10");
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select a recipe and click confirm button
        solo.clickInList(0);
        solo.clickOnText("CONFIRM");
        // enter a valid input for the number of servings
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.clickOnButton("CONFIRM");
        // click on the entered daily meal plan
        solo.clickOnText("2023-09-10");
        // click the add meal button
        solo.clickOnButton("Add A Meal");
        assertEquals(View.INVISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        assertFalse(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        // click add meal from recipes button
        solo.clickOnButton("Pick a meal from Recipe Folder");
        // select meal and press confirm
        solo.clickInList(1);
        solo.clickOnText("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        assertTrue(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        assertEquals(View.VISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        // test leaving the number of servings 0
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "");
        solo.clickOnButton("Add this meal in");
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering 0 for the number of servings
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "0");
        solo.clickOnButton("Add this meal in");
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering a valid number of servings
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "3");
        solo.clickOnButton("Add this meal in");
        solo.assertCurrentActivity("Wrong Activity", ViewDailyMealPlanActivity.class);
        // check if meal is added to the list
        assertTrue(solo.searchText("Amount: 3"));
        solo.clickOnButton("BACK");
        // check that the meal plan was added to the list
        assertTrue(solo.searchText("2023-09-10"));
        // delete item added
        solo.clickLongOnText("2023-09-10");
        solo.clickOnButton("Confirm");
    }
}