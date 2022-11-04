package com.example.prepear;

import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;

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

@RunWith(AndroidJUnit4.class)
public class AddEditRecipeActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<AddEditRecipeActivity> rule = new ActivityTestRule<>(AddEditRecipeActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testAddIngredientFragment() {
        solo.clickOnButton("Recipes Folder"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.title_EditText), "Pancake");

        solo.clickOnButton("Recipes Folder"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.title_EditText), "Pancake");
        solo.clickOnView(solo.getView(R.id.preparation_time_EditText));
        solo.clickOnMenuItem("23");
        solo.clickOnView(solo.getView(R.id.number_of_servings_EditText));
        solo.clickOnMenuItem("2");
        solo.clickOnView(solo.getView(R.id.comments_EditText));
        solo.clickOnMenuItem("");
        solo.clickOnView(solo.getView(R.id.ingredient_unit_edit_text));
        solo.clickOnMenuItem("");

        solo.clickOnText("ADD INGREDIENT");
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
