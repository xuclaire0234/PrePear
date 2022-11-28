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
 * This class test the ViewDailyMealPlanActivity using Robotium
 */
public class ViewDailyMealPlanActivityTest {
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
     * This method tests the functionality of the ViewDailyMealPlan activity
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testViewDailyMealPlan(){
        // Add a daily meal plan
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        // enter start and end dates
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.sleep(2000);
        solo.clickOnText("OK");
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.sleep(2000);
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        // enter a valid input for amount
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.clickOnView(solo.getView(R.id.confirm));
        solo.sleep(2000);
        // check that the meal plan was added to the list
        assertTrue(solo.searchText(date));
        // click on the entered daily meal plan
        solo.clickOnText(date);
        solo.assertCurrentActivity("Wrong Activity", ViewDailyMealPlanActivity.class);
        // look for the added meal inside the meal plan
        assertTrue(solo.searchText(date));
        // click the add meal button
        solo.clickOnView(solo.getView(R.id.add_daily_meal_button));
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        solo.goBack();
        // click on the added meal
        solo.clickInList(0);
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientTypeMealActivity.class);
        solo.goBack();
        // click the back button
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", FragmentScenario.EmptyFragmentActivity.class);
    }
}