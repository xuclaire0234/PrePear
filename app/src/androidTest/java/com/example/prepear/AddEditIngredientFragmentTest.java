package com.example.prepear;

import android.widget.EditText;

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
 * Test class for AddEditIngredientFragment. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class AddEditIngredientFragmentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    /**
     * Run before each test to set up activities.
     */
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
        solo.clickOnText("select date:");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-13");
        solo.goBack();
        solo.sleep(3000);
        solo.clickOnText("OK");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);

    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient fragment
     * and Test if it is directed to ViewIngredientStorage class after clicking OK button in fragment
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will show on the screen and the database get updated
     */
    public void testAddIngredient(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(4);
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.sleep(3000);
        solo.clickOnButton("OK");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
    }

    @Test
    /**
     * Test if we can view the ingredient shown in the listView
     * Adding a new ingredient will also add it into the database
     * Result: all the info of the new ingredient will be shown on the screen
     */
    public void testViewIngredientDetail(){
        // Add a new ingredient first
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "testViewIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Shelf");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(9);
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Meat, sausages, and fish");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-12-03");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"15.32");
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); //test if activity successfully transfers.

        // Try to click the ingredient just added in the listView and have a look about the detail
        solo.clickOnMenuItem("testViewIngredient");
        solo.sleep(3000);
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); // test if activity successfully transfers.
        solo.sleep(3000);
    }

    @Test
    /**
     * Test if we can edit the content of an existing ingredient shown in the listView
     * Adding a new ingredient will also add it into the database
     * Result: some info of the new ingredient will be edited to different content
     */
    public void testEditIngredientDetail(){
        // Add a new ingredient first
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "testEditIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Box");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(4);
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fruits");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-12");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"122.62");
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); //test if activity successfully transfers.

        // Try to click the ingredient just added in the listView and
        // Edit some information of that ingredient
        solo.sleep(3000);
        solo.clickOnMenuItem("testEditIngredient");
        solo.clearEditText((EditText) solo.getView(R.id.brief_description)); // clear the current text
        solo.enterText((EditText) solo.getView(R.id.brief_description), "EditedIngredient"); // change info in an EditText
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.ingredient_unit)); // change info in a spinner
        solo.pressMenuItem(7);
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.sleep(3000);
    }

    @Test
    /**
     * Test if we can delete any existing ingredient shown in the listView
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will be deleted from both screen and database
     */
    public void testDelete(){
        // Add a new ingredient first
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "testDeleteIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Fridge");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(4);
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Vegetables");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-23");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"27.89");
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); //test if activity successfully transfers.

        // Searching the one that just added in the database and delete it
        solo.clickOnMenuItem("testDeleteIngredient");
        solo.sleep(3000);
        solo.clickOnButton("Delete");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.sleep(3000);
    }

    /**
     * Test if we want to add a new ingredient but leave the spinners empty
     * there will be a Toast message shown on the screen
     * the program will treat this behaviour as an illegal action
     * Result: Toast message pops up
     */
    @Test
    public void testIfSpinnerEmpty(){
        // test if leave only one spinner empty
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "test");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Fridge");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(4);
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-23");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"27.89");
        solo.clickOnButton("OK");
        solo.searchText("Error, Some Fields Are Empty!"); // check if there is a Toast message
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); //test if activity successfully transfers.

        // test if leave two spinners empty
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "test");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(4);
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-23");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"27.89");
        solo.clickOnButton("OK");
        solo.searchText("Error, Some Fields Are Empty!"); // check if there is a Toast message
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); //test if activity successfully transfers.

        // test if leave three spinners empty
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "test");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-23");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"27.89");
        solo.clickOnButton("OK");
        solo.searchText("Error, Some Fields Are Empty!"); // check if there is a Toast message
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); //test if activity successfully transfers.
    }

    /** This method tests if the toast message will be
     * displayed when user enters 0 for the amount
     */
    @Test
    public void testZeroAmount(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "testForAmount");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(4);
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"0");
        solo.clickOnButton("OK");
        solo.searchText("Error, Amount Can Not Be Zero!");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);

    }
    /** This method tests if toast message will be displayed when user
     * leaves everything in the fragment empty
     */
    @Test
    public void testEmptyIngredient(){
        solo.clickOnButton("Ingredient Storage");
        solo.clickOnImageButton(0);
        solo.clickOnButton("OK");
        solo.searchText("Error, Some Fields Are Empty!");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
    }

    /** This method tests if toast message will be displayed when user
     * one or both edit text fields empty
     */
    @Test
    public void testEmptyEditText(){
        // test if we leave only 1 edit text field empty
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(4);
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"4");
        solo.clickOnButton("OK");
        solo.searchText("Error, Some Fields Are Empty!");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);

        // test if we leave both edit text fields empty
        solo.clickOnImageButton(0);
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.pressMenuItem(5);
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fruits");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.goBack();
        solo.clickOnButton("OK");
        solo.searchText("Error, Some Fields Are Empty!");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);

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