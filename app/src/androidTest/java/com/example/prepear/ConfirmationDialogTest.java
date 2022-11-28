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

import androidx.annotation.IdRes;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class tests the ConfirmationDialog class using Robotium
 */
public class ConfirmationDialogTest {
    private Solo solo;
    @IdRes
    private final int theme = androidx.appcompat.R.style.Theme_AppCompat_DayNight;

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    /**
     * Run before each test to set up activities.
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        FragmentScenario<MealPlanFragment> scenario = FragmentScenario.launchInContainer(MealPlanFragment.class,
                null, theme, Lifecycle.State.STARTED);

    }

    @Test
    @SuppressWarnings("deprecation")
    /**
     * Test the confirmation fragment
     */
    public void checkConfirmationDialog(){
        View button = solo.getView(R.id.add_meal_plan_button);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        solo.clickOnView(button);
        // select start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // check cancel button
        solo.clickInList(1);
        solo.clickOnButton(0);
        // check confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
    }
}