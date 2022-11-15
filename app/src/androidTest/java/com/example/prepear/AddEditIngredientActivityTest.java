package com.example.prepear;

import static org.junit.Assert.assertTrue;


import android.view.View;
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
 * Test class for AddEditIngredientActivity. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class AddEditIngredientActivityTest {
    private Solo solo;
    private IngredientController ingredientController;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    /**
     * Run before each test to set up activities.
     */
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        IngredientController ingredientController = new IngredientController();
    }

    public void mockIngredient(){
        // Add a new ingredient first
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Shelf");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Meat, sausages, and fish");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-12-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"15.32");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); //test if activity successfully transfers.
    }

    @Test
    /**
     * Test if there is a pop up DatePicker while clicking the Edittext
     */
    public void checkDatePickerInActivity() {
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.clickOnText("select date:");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-13");
        solo.clickOnText("OK");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class);

    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient activity
     * and Test if it is directed to ViewIngredientStorage class after clicking CONFIRM button
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will show on the screen and the database get updated
     */
    public void testAddingWithOutCustomAttributes(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");
    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient activity
     * and Test if it is directed to ViewIngredientStorage class after clicking CONFIRM button
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will show on the screen and the database get updated
     */
    public void testAddingWithCustomLocation(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        assertTrue(solo.getView(R.id.other_location).getVisibility() == View.VISIBLE);
        solo.enterText((EditText) solo.getView(R.id.other_location_editText), "testLocation");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");

    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient activity
     * and Test if it is directed to ViewIngredientStorage class after clicking CONFIRM button
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will show on the screen and the database get updated
     */
    public void testAddingWithCustomUnit(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        assertTrue(solo.getView(R.id.other_unit).getVisibility() == View.VISIBLE);
        solo.enterText((EditText) solo.getView(R.id.other_unit_editText), "testUnit");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");
    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient activity
     * and Test if it is directed to ViewIngredientStorage class after clicking CONFIRM button
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will show on the screen and the database get updated
     */
    public void testAddingWithCustomCategory(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        assertTrue(solo.getView(R.id.other_category).getVisibility() == View.VISIBLE);
        solo.enterText((EditText) solo.getView(R.id.other_category_editText), "testCategory");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");
    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient activity
     * and Test if it is directed to ViewIngredientStorage class after clicking CONFIRM button
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will show on the screen and the database get updated
     */
    public void testAddingWithCustomAttributes(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        assertTrue(solo.getView(R.id.other_location).getVisibility() == View.VISIBLE);
        solo.enterText((EditText) solo.getView(R.id.other_location_editText), "testLocation");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        solo.enterText((EditText) solo.getView(R.id.other_unit_editText), "testUnit");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        solo.enterText((EditText) solo.getView(R.id.other_category_editText), "testCategory");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");    }

    @Test
    /**
     * Test if we can add an ingredient in Add Ingredient activity
     * and Test if it is directed to ViewIngredientStorage class after clicking CONFIRM button
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will show on the screen and the database get updated
     */
    public void testAddingWith2CustomAttributes(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        assertTrue(solo.getView(R.id.other_location).getVisibility() == View.VISIBLE);
        solo.enterText((EditText) solo.getView(R.id.other_location_editText), "testLocation");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("Other");
        solo.sleep(3000);
        solo.enterText((EditText) solo.getView(R.id.other_unit_editText), "testUnit");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fruits");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");    }

    @Test
    /**
     * Test if we can view the ingredient shown in the listView
     * Adding a new ingredient will also add it into the database
     * Result: all the info of the new ingredient will be shown on the screen
     */
    public void testViewIngredientDetail(){
        mockIngredient();
        // Try to click the ingredient just added in the listView and have a look about the detail
        solo.pressMenuItem(0);
        solo.sleep(3000);
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class); // test if activity successfully transfers.
        solo.sleep(3000);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");
    }

    @Test
    /**
     * Test if we can edit the content of an existing ingredient shown in the listView
     * Adding a new ingredient will also add it into the database
     * Result: some info of the new ingredient will be edited to different content
     */
    public void testEditIngredientDetail(){
        // Add a new ingredient first
        mockIngredient();
        // Try to click the ingredient just added in the listView and
        // Edit some information of that ingredient
        solo.sleep(3000);
        solo.pressMenuItem(0);
        solo.clearEditText((EditText) solo.getView(R.id.brief_description_editText)); // clear the current text
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "EditedIngredient"); // change info in an EditText
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.ingredient_unit)); // change info in a spinner
        solo.pressMenuItem(7);
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.sleep(3000);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");
    }

    @Test
    /**
     * Test if we can delete any existing ingredient shown in the listView
     * Adding a new ingredient will also add it into the database
     * Result: the new ingredient will be deleted from both screen and database
     */
    public void testDelete(){
        // Add a new ingredient first
        mockIngredient();
        // Searching the one that just added in the database and delete it
        solo.clickOnMenuItem("testIngredient");
        solo.sleep(3000);
        solo.clickOnButton("Delete");
        assertTrue(ingredientController.getIngredients().size() == 0);
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
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "test");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Fridge");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-23");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"27.89");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class); //test if activity successfully transfers.
        solo.goBackToActivity("ViewIngredientStorageActivity");

        // test if leave two spinners empty
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "test");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("g");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-23");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"27.89");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class); //test if activity successfully transfers.
        solo.goBackToActivity("ViewIngredientStorageActivity");

        // test if leave three spinners empty
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description), "test");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-23");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"27.89");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class); //test if activity successfully transfers.
        solo.goBackToActivity("ViewIngredientStorageActivity");

    }

    /** This method tests if the toast message will be
     * displayed when user enters 0 for the amount
     */
    @Test
    public void testZeroAmount(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testForAmount");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("lb");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"0");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class);

    }
    /** This method tests if toast message will be displayed when user
     * leaves everything in the fragment empty
     */
    @Test
    public void testEmptyIngredient(){
        solo.clickOnButton("Ingredient Storage");
        solo.clickOnImageButton(0);
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class);
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
        solo.clickOnMenuItem("g");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fats and oils");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"4");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class);
        solo.goBackToActivity("ViewIngredientStorageActivity");

        // test if we leave both edit text fields empty
        solo.clickOnImageButton(0);
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Freezer");
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("g");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fruits");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", AddEditIngredientActivity.class);

    }

    /**
     * This method tests if user can choose icon
     */
    @Test
    public void testAddIcon(){
        solo.clickOnButton("Ingredient Storage"); //Click Ingredient Storage Button
        solo.clickOnImageButton(0);
        solo.sleep(2000);
        solo.clickOnButton("Choose");
        solo.assertCurrentActivity("wrong Activity", ChooseIngredientIconActivity.class);
        solo.clickOnView(solo.getView(R.id.banana));
        solo.enterText((EditText) solo.getView(R.id.brief_description_editText), "testIngredient");
        solo.clickOnView(solo.getView(R.id.ingredient_location));
        solo.clickOnMenuItem("Bin");
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.ingredient_unit));
        solo.clickOnMenuItem("kg");
        solo.clickOnView(solo.getView(R.id.ingredient_category));
        solo.clickOnMenuItem("Fruit");
        solo.enterText((EditText) solo.getView(R.id.bestBeforeDate),"2022-11-03");
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount),"1.2");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", ViewIngredientStorageActivity.class);
        solo.clickOnMenuItem("testIngredient");
        solo.clickOnButton("DELETE");
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