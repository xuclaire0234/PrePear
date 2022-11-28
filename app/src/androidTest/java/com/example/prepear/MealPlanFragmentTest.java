/**
 * Class Name: MealPlanFragmentTest
 * Version Information: Version 1.0
 * Date: Nov 25, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import static android.app.PendingIntent.getActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MealPlanFragmentTest {
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
        FragmentScenario<MealPlanFragment> scenario = FragmentScenario.launchInContainer(MealPlanFragment.class, null, theme, Lifecycle.State.STARTED);

    }


    /**
     * Test if there is a pop up DatePicker while clicking the Edittext
     */
    @Test
    @SuppressWarnings("deprecation")
    public void checkFloatingButton() {
        solo.assertCurrentActivity("Wrong Activity", FragmentScenario.EmptyFragmentActivity.class);
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
        solo.goBack();
        /*
         * using a try catch since this case is already tested in AddDailyMealPlanActivityTest
         * and ViewDailyMealPlanActivityTest
         */
        try {
            solo.clickInList(0);
            solo.assertCurrentActivity("Wrong Activity", ViewDailyMealPlanActivity.class);
            solo.goBack();
        } finally {
            solo.assertCurrentActivity("Wrong Activity", FragmentScenario.EmptyFragmentActivity.class);
        }
    }


}