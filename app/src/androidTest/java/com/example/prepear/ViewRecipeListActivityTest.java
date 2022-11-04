package com.example.prepear;

import android.widget.Adapter;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ViewRecipeListActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ViewRecipeListActivity> rule = new ActivityTestRule<>(ViewRecipeListActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testViewRecipeList() {
        solo.assertCurrentActivity("Wrong Activity",ViewRecipeListActivity.class);
        ListView ListView=(ListView)solo.getView(R.id.recipe_listview);
        Integer length = ListView.getCount();
        if (length != 0) {
            solo.clickInList(0);
        }
    }

    @Test
    public void testSortTitle() {
        solo.clickOnButton("Recipes Folder"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("Title");
        solo.sleep(5000);
    }

    @Test
    public void testSortPreparationTime() {
        solo.clickOnButton("Recipes Folder"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("Preparation Time");
        solo.sleep(5000);
    }

    @Test
    public void testSortNumberOfServing() {
        solo.clickOnButton("Recipes Folder"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("Number of Serving");
        solo.sleep(5000);
    }

    @Test
    public void testSortRecipeCategory() {
        solo.clickOnButton("Recipes Folder"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("Recipe Category");
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
