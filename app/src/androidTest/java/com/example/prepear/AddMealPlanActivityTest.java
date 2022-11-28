/**
 * Class Name: AddMealPlanActivityTest
 * Version Information: Version 1.0
 * Date: Nov 25, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */
package com.example.prepear;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;
import static org.junit.Assert.assertTrue;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import java.time.LocalDate;

import androidx.annotation.IdRes;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.example.prepear.ui.Recipe.RecipeFragment;
import com.example.prepear.ui.ShoppingList.ShoppingListFragment;
import com.robotium.solo.Solo;

import org.checkerframework.checker.units.qual.C;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * This class tests the AddMealPlanActivity using Robotium
 */
public class AddMealPlanActivityTest {
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
     * Test adding a daily meal plan from the ingredient storage
     */
    @Test
    @SuppressWarnings("deprecation")
    public void TestAddingFromIngredientStorage() {
        solo.clickOnView(solo.getView(R.id.add_meal_plan_button)); // click the add meal plan button
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // leave only start and end dates empty
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        // leave start date empty
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.confirm));
        // leave end date empty
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.confirm));
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
        // enter start and end dates
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.sleep(2000);
        solo.clickOnButton("OK");
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnButton("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        // check that amount edit text is visible
        assertTrue(solo.getView(R.id.amount_layout).getVisibility() == View.VISIBLE);
        // enter 0 for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "0");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
        // leave amount empty
        solo.clearEditText((EditText) solo.getView(R.id.amount));
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        solo.sleep(2000);
        // check that the meal plan was added to the list
        assertTrue(solo.searchText(date));
        // delete item added
        solo.clickLongOnText(date);
        solo.clickOnButton(1);
    }

    /**
     *  Test adding a daily meal plan from the recipe folder
     */
    @Test
    @SuppressWarnings("deprecation")
    public void TestAddingFromRecipeFolder(){
        solo.clickOnView(solo.getView(R.id.add_meal_plan_button)); // click the add meal plan button
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // enter start and end dates
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.sleep(2000);
        solo.clickOnButton("OK");
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnButton("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.recipe_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        // check that amount edit text is visible
        assertTrue(solo.getView(R.id.number_of_servings_layout).getVisibility() == View.VISIBLE);
        // enter 0 for amount
        solo.enterText((EditText) solo.getView(R.id.number_of_servings), "0");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
        // leave amount empty
        solo.clearEditText((EditText) solo.getView(R.id.number_of_servings));
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.number_of_servings), "2");
        solo.clickOnView(solo.getView(R.id.confirm));
        solo.sleep(2000);
        // check that the meal plan was added to the list
        assertTrue(solo.searchText(date));
        // delete item added
        solo.clickLongOnText(date);
        solo.clickOnButton(1);
    }

    /**
     *  Test adding many daily meal plans
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testAddingMultiplePlans() {
        // Add a daily meal plan
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(3);
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        solo.clickOnView(solo.getView(R.id.start_date));
        solo.sleep(2000);
        solo.setDatePicker(0,start.getYear() , start.getMonthValue()-1, start.getDayOfMonth());
        solo.clickOnText("OK");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.end_date));
        solo.setDatePicker(0,end.getYear() , end.getMonthValue()-1, end.getDayOfMonth());
        solo.sleep(2000);
        solo.clickOnText("OK");
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.sleep(2000);
        solo.clickOnButton(1);
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        // check that the meal plan was added to the list
        for (LocalDate day = start; day.isBefore(end) || day.isEqual(end);
             day = day.plusDays(1)) {
            solo.sendKey(Solo.DOWN);
            solo.sleep(2000);
            assertTrue(solo.searchText(String.valueOf(day)));
            solo.clickLongOnText(String.valueOf(day));
            solo.sleep(2000);
            solo.clickOnButton(1);
        }
    }

    /**
     *  Test adding a meal plan by selecting the meal first, then entering the dates
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testChoosingMealBeforeDates(){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // Add a daily meal plan
        solo.clickOnView(solo.getView(R.id.add_meal_plan_button)); // click the add meal plan button
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        // enter start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnText("OK");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.confirm));
        // check that the meal plan was added to the list
        solo.sleep(2000);
        // check that the meal plan was added to the list
        assertTrue(solo.searchText(date));
        // delete item added
        solo.clickLongOnText(date);
        solo.clickOnButton(1);
    }



}