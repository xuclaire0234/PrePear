/**
 * Classname: ViewRecipeTypeMealActivityTest
 * Version Information: 1.0.0
 * Date: 11/27/2022
 * Author: Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.annotation.IdRes;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.MealPlan.MealPlanFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Test class for ViewRecipeTypeMealActivity. All the UI tests are written here. Robotium test framework is
 * used.
 */
@RunWith(AndroidJUnit4.class)
public class ViewRecipeTypeMealActivityTest {
    private Solo solo;

    @IdRes
    private final int theme = androidx.appcompat.R.style.Theme_AppCompat_DayNight;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    /**
     * Run before each test to set up activities.
     */
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        FragmentScenario<MealPlanFragment> scenario = FragmentScenario.launchInContainer(MealPlanFragment.class, null, theme, Lifecycle.State.STARTED);
    }

    @Test
    /**
     * Test viewing the details of recipe type meal activity.
     * Test editing the customized number of servings of the specific ingredient type meal activity.
     * Test deleting a recipe type meal from the daily meal plan.
     */
    public void testViewRecipeTypeActivity() {
        // Navigate from Login page to Meal Planner
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()); // leave start date empty

        if (solo.searchText(date)) {
            solo.clickLongOnText(date);
            solo.clickOnButton(1);
            solo.sleep(3000);
        }

        // add new meal plan
        View button = solo.getView(R.id.add_meal_plan_button);
        solo.clickOnView(button); // click the add meal plan button

        // enter start and end dates
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.start_date), date);
        solo.sleep(2000);
        solo.clickOnButton("OK");
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.end_date), date);
        solo.clickOnButton("OK");

        // click on recipe radio button
        RadioButton rb = (RadioButton) solo.getView(R.id.recipe_radioButton);
        solo.clickOnView(rb);

        // select a recipe and click confirm button
        solo.clickInList(0);
        solo.clickOnButton(1);

        // enter a number of servings and add it to the meal plan
        solo.enterText((EditText) solo.getView(R.id.number_of_servings), "5");
        solo.sleep(3000);
        solo.clickOnView((View) solo.getView(R.id.confirm));
        solo.sleep(3000);
        assertTrue(solo.searchText(date));

        // select the daily meal plan just added and check viewing details of recipe type meal function
        solo.clickOnText(date);
        solo.sleep(3000);
        solo.clickInList(0);
        solo.sleep(3000);
        assertEquals(((EditText) solo.getView(R.id.number_of_servings_EditText)).getText().toString(), "5");

        // check changing the customized number of servings function
        solo.sleep(3000);
        solo.clickOnView((View) solo.getView(R.id.number_of_servings_EditText));
        solo.clearEditText((EditText) solo.getView(R.id.customize_number_of_servings_EditText));
        solo.sleep(3000);
        solo.enterText((EditText) solo.getView(R.id.customize_number_of_servings_EditText), "10");
        solo.clickOnButton(1);
        solo.clickOnView(solo.getView(R.id.NestedScrollView));
        NestedScrollView nestedScrollView = (NestedScrollView) solo.getView(R.id.NestedScrollView);
        nestedScrollView.scrollTo(700, 700);
        solo.clickOnView((View) solo.getView(R.id.commit_button));
        solo.sleep(3000);
        solo.clickInList(0);
        solo.sleep(3000);
        assertEquals(((EditText) solo.getView(R.id.number_of_servings_EditText)).getText().toString(), "10");

        // check deleting the meal from the daily meal plan function
        solo.sleep(3000);
        nestedScrollView = (NestedScrollView) solo.getView(R.id.NestedScrollView);
        nestedScrollView.scrollTo(700, 700);
        solo.sleep(3000);
        solo.clickOnView((View) solo.getView(R.id.delete_button));
    }

    @After
    /**
     * Run after each test to close activities.
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}