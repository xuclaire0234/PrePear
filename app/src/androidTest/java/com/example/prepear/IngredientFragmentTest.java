/**
 * Class Name: Ingredient
 * Version: 1.0
 * Create Date: Oct 25th, 2022
 * Last Edit Date: Nov 3rd, 2022
 * Author: Jingyi Xu
 * Copyright Notice:
 */
package com.example.prepear;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for IngredientFragment. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class IngredientFragmentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    /**
     * Run before each test to set up activities.
     */
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     * Test if we sort the existing {@link IngredientInStorage} shown in the listView after clicking description(ascending)
     */
    public void testSort1(){
        solo.clickOnImageButton(0);
        solo.clickOnText("IngredientStorage");
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("description");
        solo.sleep(5000);
    }

    @Test
    /**
     * Test if we sort the existing {@link IngredientInStorage}  shown in the listView after clicking best before date (oldest to newest)
     */
    public void testSort2(){
        solo.clickOnImageButton(0);
        solo.clickOnText("IngredientStorage");
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("best before");
        solo.sleep(5000);
    }

    @Test
    /**
     * Test if we can sort the existing {@link IngredientInStorage}  shown in the listView after clicking location(ascending by default)
     */
    public void testSort3(){
        solo.clickOnImageButton(0);
        solo.clickOnText("IngredientStorage");
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("location");
        solo.sleep(5000);
    }


    @Test
    /**
     * Test if we can sort the existing {@link IngredientInStorage}  shown in the listView after clicking category
     */
    public void testSort4(){
        solo.clickOnImageButton(0);
        solo.clickOnText("IngredientStorage");
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("category");
        solo.sleep(5000);
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    // must include in each activity
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }



}