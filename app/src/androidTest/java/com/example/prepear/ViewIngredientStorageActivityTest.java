package com.example.prepear;

import android.widget.Adapter;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Test class for AddEditIngredientFragment. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class ViewIngredientStorageActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     * Test if we sort the existing ingredients shown in the listView after clicking description(ascending)
     */
    public void testSort1(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("description");
        /*
            "Fennel seeds will switch the position with "eggs"
         */
        solo.sleep(5000);
    }

    @Test
    /**
     * Test if we sort the existing ingredient shown in the listView after clicking best before (oldest to newest)
     */
    public void testSort2(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("best before");
        solo.sleep(5000);
    }

    @Test
    /**
     * Test if we can sort the existing ingredient shown in the listView after clicking location(ascending by default)
     */
    public void testSort3(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("location");
        solo.sleep(5000);
    }

    @Test
    public void testSort4(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
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