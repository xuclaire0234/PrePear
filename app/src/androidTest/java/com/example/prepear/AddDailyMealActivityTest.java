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

import androidx.annotation.IdRes;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class tests the AddDailyMealActivity class using Robotium
 */
public class AddDailyMealActivityTest {
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

    /**
     * Test adding a daily meal from the ingredient storage
     */
    @Test
    @SuppressWarnings("deprecation")
    public void addMealFromIngredientStorage(){
        // Add a daily meal plan
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        // enter start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select a recipe and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        // enter a valid input for the number of servings
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.clickOnView(solo.getView(R.id.confirm));
        // click on the entered daily meal plan
        solo.clickOnText(date);
        // click the add meal button
        solo.clickOnView(solo.getView(R.id.add_daily_meal_button));
        assertEquals(View.INVISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        assertFalse(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        // click add meal from recipes button
        solo.clickOnView(solo.getView(R.id.pick_in_storage_meal_button));
        // select meal and press confirm
        solo.clickInList(1);
        solo.clickOnButton(1);
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        assertTrue(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        assertEquals(View.VISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        // test leaving the number of servings 0
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "");
        solo.clickOnView(solo.getView(R.id.add_meal_in_plan_button));
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering 0 for the number of servings
        solo.clearEditText((EditText) solo.getView(R.id.meal_amount_input));
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "0");
        solo.clickOnView(solo.getView(R.id.add_meal_in_plan_button));
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering a valid number of servings
        solo.clearEditText((EditText) solo.getView(R.id.meal_amount_input));
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "3");
        solo.clickOnView(solo.getView(R.id.add_meal_in_plan_button));
        solo.assertCurrentActivity("Wrong Activity", ViewDailyMealPlanActivity.class);
        // choose time for the meal
        solo.clickOnView(solo.getView(R.id.edit_time_button));
        solo.clickOnButton(1);
        solo.clickOnView(solo.getView(R.id.back_button));
        // delete item added
        assertTrue(solo.searchText(date));
        solo.clickLongOnText(date);
        solo.clickOnButton(1);
    }

    /**
     * Test adding a daily meal from Recipes
     */
    @Test
    @SuppressWarnings("deprecation")
    public void addMealFromRecipes(){
        // Add a daily meal plan
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        // enter start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select a recipe and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        // enter a valid input for the number of servings
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.clickOnView(solo.getView(R.id.confirm));
        // click on the entered daily meal plan
        solo.clickOnText(date);
        // click the add meal button
        solo.clickOnView(solo.getView(R.id.add_daily_meal_button));
        assertEquals(View.INVISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        assertFalse(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        // click add meal from recipes button
        solo.clickOnView(solo.getView(R.id.pick_recipe_meal_button));
        // select meal and press confirm
        solo.clickInList(1);
        solo.clickOnButton(1);
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        assertTrue(solo.getView(R.id.add_meal_in_plan_button).isEnabled());
        assertEquals(View.VISIBLE, solo.getView(R.id.meal_amount_input).getVisibility());
        // test leaving the number of servings 0
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "");
        solo.clickOnView(solo.getView(R.id.add_meal_in_plan_button));
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering 0 for the number of servings
        solo.clearEditText((EditText) solo.getView(R.id.meal_amount_input));
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "0");
        solo.clickOnView(solo.getView(R.id.add_meal_in_plan_button));
        solo.assertCurrentActivity("Wrong Activity", AddDailyMealActivity.class);
        // test entering a valid number of servings
        solo.clearEditText((EditText) solo.getView(R.id.meal_amount_input));
        solo.enterText((EditText) solo.getView(R.id.meal_amount_input), "3");
        solo.clickOnView(solo.getView(R.id.add_meal_in_plan_button));
        solo.assertCurrentActivity("Wrong Activity", ViewDailyMealPlanActivity.class);
        // choose time for the meal
        solo.clickOnView(solo.getView(R.id.edit_time_button));
        solo.clickOnButton(1);
        solo.clickOnView(solo.getView(R.id.back_button));
        // delete item added
        assertTrue(solo.searchText(date));
        solo.clickLongOnText(date);
        solo.clickOnButton(1);
    }
}