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
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.example.prepear.ui.Recipe.RecipeFragment;
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
     * Test adding a daily meal plan from the ingredient storage
     */
    @Test
    @SuppressWarnings("deprecation")
    public void TestAddingFromIngredientStorage() {
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());//        // leave start date empty
        // leave start and end dates empty
        solo.sleep(2000);
        solo.clickOnButton("CONFIRM");
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
        solo.sleep(3000);
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
        // leave amount empty
        solo.clearEditText((EditText) solo.getView(R.id.amount));
        solo.sleep(3000);
        solo.clickOnButton("CONFIRM");
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.sleep(3000);
        solo.clickOnButton("CONFIRM");
        solo.sleep(3000);
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
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());//        // leave start date empty
        // leave start and end dates empty
        solo.sleep(2000);
        solo.clickOnButton("CONFIRM");
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
        RadioButton rb = (RadioButton) solo.getView(R.id.recipe_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);
        // check that amount edit text is visible
        assertTrue(solo.getView(R.id.number_of_servings_layout).getVisibility() == View.VISIBLE);
        // enter 0 for amount
        solo.enterText((EditText) solo.getView(R.id.number_of_servings), "0");
        solo.sleep(3000);
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddMealPlanActivity.class);
        // leave amount empty
        solo.clearEditText((EditText) solo.getView(R.id.number_of_servings));
        solo.sleep(3000);
        solo.clickOnButton("CONFIRM");
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.number_of_servings), "2");
        solo.sleep(3000);
        solo.clickOnButton("CONFIRM");
        solo.sleep(3000);
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
    public void testAddingMultiplePlans() throws ParseException {
        // Add a daily meal plan
        solo.clickOnButton("Log In"); // click log in Button
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startDay = Calendar.getInstance();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        String date = "2022-11-27";
//        startDay.setTime(Objects.requireNonNull(s.parse(date)));
//        Log.d(TAG, "testAddingMultiplePlans: " + startDay.getTime());
//        LocalDate start = s.parse(date);
//        String startDate = LocalDate.parse(date).toString();
//        startDay.setTime(start);

        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        // enter start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), LocalDate.parse(date).toString());
        solo.clickOnText("OK");
//        startDay.add(Calendar.DATE,1);
//        startDate = LocalDate.parse(date).plusDays(2).toString();
        solo.enterText((EditText) solo.getView(R.id.end_date), (LocalDate.parse(date).plusDays(3).toString()));
        solo.clickOnButton(0);
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);

        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        solo.sleep(3000);

        solo.clickOnButton("CONFIRM");
        // check that the meal plan was added to the list
        assertTrue(solo.searchText("2023-09-10"));
        assertTrue(solo.searchText("2023-09-11"));
        assertTrue(solo.searchText("2023-09-12"));
        assertTrue(solo.searchText("2023-09-13"));
        assertTrue(solo.searchText("2023-09-14"));
        assertTrue(solo.searchText("2023-09-15"));
        // delete the added meal plans
        // delete item added
        solo.clickLongOnText("2023-09-11");
        solo.clickOnText("CONFIRM");
        solo.clickLongOnText("2023-09-12");
        solo.clickOnText("CONFIRM");
        solo.clickLongOnText("2023-09-13");
        solo.clickOnText("CONFIRM");
        solo.clickLongOnText("2023-09-14");
        solo.clickOnText("CONFIRM");
        solo.clickLongOnText("2023-09-15");
        solo.clickOnText("CONFIRM");
        solo.clickLongOnText("2023-09-10");
        solo.clickOnButton("CONFIRM");

    }

    /**
     *  Test adding a meal plan by selecting the meal first, then entering the dates
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testChoosingMealBeforeDates(){
        // Add a daily meal plan
        solo.clickOnButton("Log In"); // click log in Button
        solo.sleep(3000);
        solo.clickOnImageButton(0);  // click the navigation button
        solo.clickOnText("MealPlan");
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button
        // click on ingredient radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.ingredient_radioButton);
        solo.clickOnView(rb);
        // select an ingredient and click confirm button
        solo.clickInList(0);
        solo.clickOnText("CONFIRM");
        // enter a valid input for amount
        solo.enterText((EditText) solo.getView(R.id.amount), "2");
        // enter start and end dates
        solo.enterText((EditText) solo.getView(R.id.start_date), "2023-09-10");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.end_date), "2023-09-10");
        solo.clickOnText("OK");
        solo.sleep(3000);

        solo.clickOnButton("CONFIRM");
        // check that the meal plan was added to the list
        solo.sleep(3000);
        // check that the meal plan was added to the list
        assertTrue(solo.searchText("2023-09-10"));
        // delete item added
        solo.clickLongOnText("2023-09-10");
        solo.clickOnButton("CONFIRM");
    }
}
