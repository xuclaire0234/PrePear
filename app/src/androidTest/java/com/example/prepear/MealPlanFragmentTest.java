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

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MealPlanFragmentTest {
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
     * Test if there is a pop up DatePicker while clicking the Edittext
     */
    @Test
    @SuppressWarnings("deprecation")
    public void checkFloatingButton() {
        solo.sleep(2000);
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
    }
}
