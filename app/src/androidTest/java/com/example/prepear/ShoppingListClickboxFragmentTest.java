/**
 * Classname: IngredientInRecipeTest
 * Version Information: 1.0.0
 * Date: 11/23/2022
 * Author: Yingyue Cao
 * Copyright Notice:
 */
package com.example.prepear;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.prepear.ui.ShoppingList.ShoppingListFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * This function test the shopping list clickbox fragment
 */
public class ShoppingListClickboxFragmentTest {
    private Solo solo;

    @IdRes
    /**
     * Run before each test to set up activities.
     */
    private final int theme = androidx.appcompat.R.style.Theme_AppCompat_DayNight;

    @Before
    /**
     * Run before each test to set up activities.
     */
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        FragmentScenario<ShoppingListFragment> scenario = FragmentScenario.launchInContainer(ShoppingListFragment.class, null, theme, Lifecycle.State.STARTED);
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * This test is to test if the view of the click box is right
     */
    @Test
    public void testClickBoxView() {
        solo.clickOnText("Shopping List Time Range"); // click outside to exit the pop out window

        // set the from date and the to date
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-30");
        solo.goBack();
        solo.sleep(2000);
        // click on confirm button
        solo.clickOnButton(0);

        // wait until data all fetched from the database
        solo.sleep(10000);

        // get the list view and it's adapter
        ListView view = (ListView) solo.getView(R.id.ingredient_listview);
        Adapter adapter = view.getAdapter();

        if (adapter.getCount() != 0) {
            // if the list view is not empty, click on the check box of it's first item
            View listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));

            // check if all TextViews were filled
            TextView descriptionText = (TextView) solo.getView(R.id.brief_description);
            TextView amountText = (TextView) solo.getView(R.id.ingredient_amount);
            TextView unitText = (TextView) solo.getView(R.id.ingredient_unit);
            TextView categoryText = (TextView) solo.getView(R.id.ingredient_category);
            assertTrue(descriptionText.getText().length() > 0);
            assertTrue(amountText.getText().length() > 0);
            assertTrue(unitText.getText().length() > 0);
            assertTrue(categoryText.getText().length() > 0);
        }

    }

    /**
     * This test the cancel button in fragment
     */
    @Test
    public void testClickBoxCancelButton() {
        solo.clickOnText("Shopping List Time Range");// click outside to exit the pop out window

        // set the from date and the to date
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-30");
        solo.goBack();
        solo.sleep(2000);
        // click on confirm button
        solo.clickOnButton(0);

        solo.sleep(10000);

        // get the list view and it's adapter
        ListView view = (ListView) solo.getView(R.id.ingredient_listview);
        Adapter adapter = view.getAdapter();

        if (adapter.getCount() != 0) {
            // if the list view is not empty, click on the check box of it's first item
            View listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));

            // get the reference to the existing elements on the fragment
            CheckBox check = listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox);
            EditText actualAmount = (EditText) solo.getView(R.id.ingredient_actual_amount);
            EditText bestBeforeDate = (EditText) solo.getView(R.id.best_before_date);

            // set the amount to 3.0 and click the cancel button
            solo.enterText((EditText) solo.getView(R.id.ingredient_actual_amount),"3.0");
            solo.clickOnText("Cancel");
            solo.sleep(3000);

            // click on the checkbox of the first item again
            listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            // set the location and click the cancel button
            solo.clickOnView(solo.getView(R.id.ingredient_location));
            solo.pressMenuItem(0);
            solo.clickOnText("Cancel");
            solo.sleep(3000);

            // click on the checkbox of the first item again
            listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            // set the date and click the cancel button
            solo.enterText((EditText) solo.getView(R.id.best_before_date),"2023-1-30");
            solo.clickOnText("Cancel");
            solo.sleep(3000);
        }
    }

    /**
     * This test the ok button on fragment
     */
    @Test
    public void testOkButton() {
        solo.clickOnText("Shopping List Time Range"); // click outside to exit the pop out window
        // set the from date and the to date
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-30");
        solo.goBack();
        solo.sleep(2000);
        // click on confirm button
        solo.clickOnButton(0);

        solo.sleep(10000);

        // get the list view and it's adapter
        ListView view = (ListView) solo.getView(R.id.ingredient_listview);
        Adapter adapter = view.getAdapter();

        if (adapter.getCount() != 0) {
            // if the list view is not empty, click on the check box of it's first item
            View listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));

            // set the amount only and click the ok button
            solo.enterText((EditText) solo.getView(R.id.ingredient_actual_amount),"3.0");
            solo.clickOnText("OK");
            solo.sleep(3000);

            // click on the checkbox of the first item again
            listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            // set the location only and click the ok button
            solo.clickOnView(solo.getView(R.id.ingredient_location));
            solo.clickOnMenuItem("Bin");
            solo.clickOnText("OK");
            solo.sleep(3000);

            // click on the checkbox of the first item again
            listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            // set the date only and click the ok button
            solo.enterText((EditText) solo.getView(R.id.best_before_date),"2023-1-30");
            solo.goBack();
            solo.sleep(1000);
            solo.clickOnText("OK");
            solo.sleep(3000);

            // click on the checkbox of the first item again
            listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            // set the all three blanks and click the ok button
            solo.enterText((EditText) solo.getView(R.id.ingredient_actual_amount),"3.0");
            solo.enterText((EditText) solo.getView(R.id.best_before_date),"2023-1-30");
            solo.goBack();
            solo.clickOnView(solo.getView(R.id.ingredient_location));
            solo.clickOnMenuItem("Bin");
            solo.sleep(1000);
            solo.clickOnText("OK");
            solo.sleep(3000);
        }

    }


}
