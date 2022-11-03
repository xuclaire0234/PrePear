package com.example.prepear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

/**
 * Test class for AddEditIngredientFragment. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class AddEditIngredientFragmentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     * Test if there is a pop up DatePicker while clicking the Edittext
     */
    public void checkDatePickerInFragment() {
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "Carrots");
        solo.clickOnView(solo.getView(R.id.bestBeforeDate));
        solo.waitForView(R.style.activity_date_picker,5,2000);
        solo.clickOnText("OK");
    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient fragment
     * and if it is directed to ViewIngredientStorage class after clicking OK button in fragment
     */
    public void testAddIngredient(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "Carrots");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");

        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        //solo.searchText("2022-12-04");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnText("OK");
    }

    @Test
    /**
     * Test if we can view the ingredient shown in the listView
     */
    public void testViewIngredientDetail(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorage.class);
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "Carrots");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.sleep(2000);
        solo.clickOnText("OK");
        // Get ViewIngredientStorage Activity to access its variables and methods.
        ViewIngredientStorage viewIngredientStorage = (ViewIngredientStorage) solo.getCurrentActivity();
        Adapter adapter = viewIngredientStorage.ingredientStorageListAdapter;
        IngredientInStorage ingredientInStorage = (IngredientInStorage) adapter.getItem(0);
        solo.sleep(3000);
        solo.clickOnText(ingredientInStorage.getBriefDescription());
        solo.sleep(3000);
        assertEquals("nn",ingredientInStorage.getBriefDescription());
        solo.searchText("Carrots");
        solo.sleep(5000);
    }

    @Test
    /**
     * Test if we can edit the content of an existing ingredient shown in the listView
     */
    public void testEditIngredientDetail(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorage.class);
        ViewIngredientStorage viewIngredientStorage = (ViewIngredientStorage) solo.getCurrentActivity();
        Adapter adapter = viewIngredientStorage.ingredientStorageListAdapter;
        solo.sleep(3000);
        solo.clickOnMenuItem("nn");
        solo.clearEditText((EditText) solo.getView(R.id.brief_description));
        solo.enterText((EditText) solo.getView(R.id.brief_description), "Carrots");
        solo.sleep(3000);
        solo.clickOnText("OK");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorage.class);
        IngredientInStorage ingredientInStorage = (IngredientInStorage) adapter.getItem(0);
        solo.sleep(3000);
        assertEquals("Carrots",ingredientInStorage.getBriefDescription());
    }

    @Test
    /**
     * Test if we can delete any existing ingredient shown in the listView
     */
    public void testDelete(){
        // Searching one that has already in the database and delete it
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorage.class);
        ViewIngredientStorage viewIngredientStorage = (ViewIngredientStorage) solo.getCurrentActivity();
        Adapter adapter = viewIngredientStorage.ingredientStorageListAdapter;
        // Change the text to search
        solo.clickOnMenuItem("tetst");
        solo.sleep(3000);
        solo.clickOnText("Delete");
        solo.sleep(3000);
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