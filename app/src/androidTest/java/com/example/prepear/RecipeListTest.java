/**
 * Classname: RecipeListTest
 * Version Information: 1.0.0
 * Date: 11/4/2022
 * Author: Jiayin He
 * Copyright Notice:
 */
package com.example.prepear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.view.View;
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

/**
 * Test class for Recipe List part. All the UI tests are written here. Robotium test framework is
 *  used.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ViewRecipeListActivity> rule = new ActivityTestRule<>(ViewRecipeListActivity.class, true, true);

    @Before
    /**
     * Run before each test to set up activities.
     */
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     * Test if user can go to view recipe activity by clicking one of the recipes from recipe list.
     * Test if user can get back to the view recipe list activity by clicking return button in the
     * view recipe list activity.
     */
    public void testGoToViewRecipeActivity() {
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        ListView recipeListView = (ListView) solo.getView(R.id.recipe_listview);

        /* check if there is enough recipes in listview to be used for testing */
        int length = recipeListView.getCount();
        if (length != 0) {
            solo.clickInList(0);
            solo.assertCurrentActivity("Wrong Activity", ViewRecipeActivity.class);

            /* check return button in view recipe activity */
            View returnButton = solo.getView(R.id.return_button);
            solo.clickOnView(returnButton);
            solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        }
    }

    @Test
    /**
     * Test if sort button works correctly.
     */
    public void testSort() {
        /* check to see if sort button works */
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_spinner));
        solo.clickOnMenuItem("Title");
    }

    @Test
    /**
     * Test if user can add and edit recipe correctly by going to the add edit recipe activity.
     */
    public void testAddEditRecipe() {
        /* check the add recipe floating action button */
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        View fab = solo.getView(R.id.add_recipe_button);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Wrong Activity", AddEditRecipeActivity.class);

        /* check cancel button of add edit recipe activity */
        solo.enterText((EditText) solo.getView(R.id.title_EditText), "Ice Cream");
        View cancelButton = solo.getView(R.id.cancel_button);
        solo.clickOnView(cancelButton);
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);

        /* check entering stuff to add edit recipe activity */
        solo.clickOnView(fab);
        solo.enterText((EditText) solo.getView(R.id.title_EditText), "Ice Cream");
        solo.enterText((EditText) solo.getView(R.id.preparation_time_EditText), "32");
        solo.enterText((EditText) solo.getView(R.id.number_of_servings_EditText), "2");
        solo.clickOnView(solo.getView(R.id.recipe_category_Spinner));
        solo.clickOnMenuItem("Dessert");
        solo.enterText((EditText) solo.getView(R.id.comments_EditText), "None");

        /* check cancel button in add ingredient in recipe fragment */
        View addIngredientButton = solo.getView(R.id.add_ingredient_in_recipe_button);
        solo.clickOnView(addIngredientButton);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.recipe_add_ingredient_fragment);
        solo.enterText((EditText) solo.getView(R.id.description_edit_text), "Milk");
        solo.clickOnText("Cancel");
        assertFalse(solo.searchText("Milk"));

        /* check confirm button in add ingredient in recipe fragment */
        addIngredientButton = solo.getView(R.id.add_ingredient_in_recipe_button);
        solo.clickOnView(addIngredientButton);
        solo.enterText((EditText) solo.getView(R.id.description_edit_text), "Milk");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount_edit_text), "10");
        solo.clickOnView(solo.getView(R.id.ingredient_unit_edit_text));
        solo.clickOnMenuItem("ml");
        solo.clickOnView(solo.getView(R.id.ingredient_category_edit_text));
        solo.clickOnMenuItem("Eggs, milk, and milk product");
        solo.clickOnText("Confirm");
        assertTrue(solo.waitForText("Milk", 1, 1000));

        /* check the cancel button in edit ingredient in recipe fragment */
        solo.clickOnText("Milk");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.recipe_edit_ingredient_fragment);
        solo.clearEditText((EditText) solo.getView(R.id.brief_description));
        solo.enterText((EditText) solo.getView(R.id.brief_description), "Egg");
        solo.clickOnText("Cancel");
        assertTrue(solo.searchText("Milk"));

        /* check the OK button in edit ingredient in recipe fragment */
        solo.clickOnText("Milk");
        solo.clearEditText((EditText) solo.getView(R.id.ingredient_amount));
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount), "5");
        solo.clickOnText("OK");
        assertTrue(solo.searchText("5"));

        /* check the delete button in edit ingredient in recipe fragment */
        solo.clickOnText("Milk");
        solo.clickOnText("Delete");
        assertFalse(solo.searchText("Milk"));

        /* check commit button in add edit recipe activity*/
        View commitButton = solo.getView(R.id.commit_button);
        solo.clickOnView(commitButton);
        solo.waitForText("Ice Cream", 1, 1000);

        /* check edit button in view recipe activity */
        solo.clickOnText("Ice Cream");
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeActivity.class);
        View editRecipeButton = solo.getView(R.id.edit_button);
        solo.clickOnView(editRecipeButton);
        solo.assertCurrentActivity("Wrong Activity", AddEditRecipeActivity.class);

        /* check commit button in add edit recipe activity*/
        solo.clearEditText((EditText) solo.getView(R.id.title_EditText));
        solo.enterText((EditText) solo.getView(R.id.title_EditText), "Ice Cream Cone");
        commitButton = solo.getView(R.id.commit_button);
        solo.clickOnView(commitButton);
        assertTrue(solo.searchText("Ice Cream Cone"));
    }

    @Test
    /**
     * Test if user can delete recipe correctly by going to the view recipe activity.
     */
    public void testDeleteRecipe () {
        /* check the add recipe floating action button */
        solo.assertCurrentActivity("Wrong Activity", ViewRecipeListActivity.class);
        View fab = solo.getView(R.id.add_recipe_button);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Wrong Activity", AddEditRecipeActivity.class);

        /* check entering stuff to add edit recipe activity */
        solo.enterText((EditText) solo.getView(R.id.title_EditText), "Orange Juice");
        solo.enterText((EditText) solo.getView(R.id.preparation_time_EditText), "15");
        solo.enterText((EditText) solo.getView(R.id.number_of_servings_EditText), "3");
        solo.clickOnView(solo.getView(R.id.recipe_category_Spinner));
        solo.clickOnMenuItem("Dessert");
        solo.enterText((EditText) solo.getView(R.id.comments_EditText), "None");

        /* check commit button in add edit recipe activity*/
        View commitButton = solo.getView(R.id.commit_button);
        solo.clickOnView(commitButton);
        solo.waitForText("Orange Juice", 1, 1000);

        /* check delete button in view recipe activity */
        solo.clickOnText("Orange Juice");
        View deleteButton = solo.getView(R.id.delete_button);
        solo.clickOnView(deleteButton);
        solo.sleep(5000);
        assertFalse(solo.searchText("Orange Juice"));
    }

    @After
    /**
     * Run after each test to close activities.
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}