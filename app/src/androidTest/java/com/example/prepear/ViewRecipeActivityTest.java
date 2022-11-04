package com.example.prepear;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

@RunWith(AndroidJUnit4.class)
public class ViewRecipeActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ViewRecipeListActivity> rule = new ActivityTestRule<>(ViewRecipeListActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void viewRecipeTest() throws Exception{
        solo.assertCurrentActivity("Wrong Activity",ViewRecipeListActivity.class);
        ListView ListView=(ListView)solo.getView(R.id.recipe_listview);
        Integer length = ListView.getCount();
        if (length != 0) {
            solo.clickInList(0);
        }
        solo.assertCurrentActivity("Wrong Activity",ViewRecipeActivity.class);
    }


}
