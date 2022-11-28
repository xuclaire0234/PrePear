/**
 * Class Name: ViewDailyMealPlanActivityTest
 * Version Information: Version 1.0
 * Date: Nov 25, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * This class test the ViewDailyMealPlanActivity using Robotium
 */
public class ViewDailyMealPlanActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    /**
     * Run before each test to set up activities.
     */
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * This method tests the functionality of the ViewDailyMealPlan activity
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testViewDailyMealPlan(){
        // Add a daily meal plan
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        solo.clickOnImageButton(0);
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

        solo.clickOnButton("CONFIRM");
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount_edit_text), "2");
        solo.clickOnButton("CONFIRM");
        // check that the meal plan was added to the list
        assertTrue(solo.searchText("2023-09-10"));
        // click on the entered daily meal plan
        solo.clickOnText("2023-09-10");
        solo.assertCurrentActivity("Wrong Activity", ViewDailyMealPlanActivity.class);
        // look for the added meal inside the meal plan
        assertTrue(solo.searchText("2023-09-10"));
        // click the add meal button
        solo.clickOnButton("Add A Meal");
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        solo.goBack();
        // click on the added meal
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientTypeMealActivity.class);
        solo.goBack();
        // click the back button
        solo.clickOnButton("BACK");
    }
}