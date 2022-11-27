/**
 * Class Name: ConfirmationDialogTest
 * Version Information: Version 1.0
 * Date: Nov 25, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * This class tests the ConfirmationDialog class using Robotium
 */
public class ConfirmationDialogTest {
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

    @Test
    @SuppressWarnings("deprecation")
    /**
     * Test the confirmation fragment
     */
    public void checkConfirmationDialog(){
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnImageButton(0);
        // select start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), "2023-09-10");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), "2023-09-10");
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // check cancel button
        solo.clickInList(0);
        solo.clickOnButton("CANCEL");
        // check confirm button
        solo.clickInList(0);
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
    }
}