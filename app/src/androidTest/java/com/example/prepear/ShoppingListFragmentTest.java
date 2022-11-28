/**
 * Classname: ShoppingListFragmentTest
 * Version Information: 1.0.0
 * Date: 11/19/2022
 * Author: Yingyue Cao
 * Copyright Notice:
 */
package com.example.prepear;

import static android.system.Os.close;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class test the fragment to view shopping list and choose the start and end date of the fragment.
 */
public class ShoppingListFragmentTest {

    private Solo solo;


    @IdRes
    /**
     * Set up the theme for the fragment test
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
     * This function test the from date picker in shoppingList Fragment
     */
    @Test
    public void testFromDatePicker() {
        solo.clickOnText("Shopping List Time Range"); // click outside to exit the pop out window
        // double click on the view to awake the data picker
        solo.clickOnView(solo.getView(R.id.fromDate_textView));
        solo.clickOnView(solo.getView(R.id.fromDate_textView));
        // press OK on the date picker
        solo.clickOnText("OK");
        solo.sleep(2000);
        // check if the view was filled with text
        TextView fromDateText = (TextView) solo.getView(R.id.fromDate_textView);
        assertTrue(fromDateText.getText().length() > 0);
    }

    /**
     * This function test the to date picker in shoppingList Fragment
     */
    @Test
    public void testToDatePicker() {
        solo.clickOnText("Shopping List Time Range"); // click outside to exit the pop out window
        // double click on the view to awake the data picker
        solo.clickOnView(solo.getView(R.id.toDate_textView));
        solo.clickOnView(solo.getView(R.id.toDate_textView));
        // press OK on the date picker
        solo.clickOnText("OK");
        solo.sleep(2000);
        // check if the view was filled with text
        TextView toDateText = (TextView) solo.getView(R.id.toDate_textView);
        assertTrue(toDateText.getText().length() > 0);
    }

    /**
     * This function test the confirm button
     */
    @Test
    public void testDatePicked() {
        solo.clickOnText("Shopping List Time Range"); // click outside to exit the pop out window

        // find the reference to the two date texts
        EditText fromDateText = (EditText) solo.getView(R.id.fromDate_textView);
        EditText toDateText = (EditText) solo.getView(R.id.toDate_textView);

        // set the start date later than the end data, check if that works
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-10-01");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        // the edit box should return to previous way after click on CONFIRM
        assertFalse(fromDateText.getText().length() > 0);
        assertFalse(toDateText.getText().length() > 0);

        // set the start date only, check if that works
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        // the edit box should return to previous way after click on CONFIRM
        assertFalse(fromDateText.getText().length() > 0);
        assertFalse(toDateText.getText().length() > 0);

        // set the end date only, check if that works
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-10-01");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        // the edit box should return to previous way after click on CONFIRM
        assertFalse(fromDateText.getText().length() > 0);
        assertFalse(toDateText.getText().length() > 0);

        // set the start data before end date, check if that works
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-11");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-11");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        // the edit box should change after click on CONFIRM
        assertEquals(fromDateText.getText().toString(),"2022-11-11");
        assertEquals(toDateText.getText().toString(),"2022-12-11");
    }

    /**
     * This function test the view of the list view
     */
    @Test
    public void testListView() {
        solo.clickOnText("Shopping List Time Range"); // click outside to exit the pop out window
        // set the start date later than the end data and click on the confirm button
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-30");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);

        solo.sleep(10000);

        // get the list view and its adapter
        ListView view = (ListView) solo.getView(R.id.ingredient_listview);
        Adapter adapter = view.getAdapter();

        if (adapter.getCount() != 0) {
            // if there was content in the list view, check the first item's look
            View listElement = view.getChildAt(0);

            // every filed should be filled
            TextView descriptionText = listElement.findViewById(R.id.brief_description_TextView_shopping);
            TextView amount = listElement.findViewById(R.id.amount_TextView_shopping);
            TextView unit = listElement.findViewById(R.id.unit_TextView_shopping);
            TextView category = listElement.findViewById(R.id.ingredient_category_TextView_shopping);
            assertTrue(descriptionText.getText().length() > 0);
            assertTrue(amount.getText().length() > 0);
            assertTrue(unit.getText().length() > 0);
            assertTrue(category.getText().length() > 0);
        }
    }

    /**
     * This function test the sort and reverse function on the fragment
     */
    @Test
    public void testSortAndReverse() {
        solo.clickOnText("Shopping List Time Range");// click outside to exit the pop out window
        // set the start date later than the end data and click on the confirm button
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-30");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);

        solo.sleep(10000);

        // get the list view and its adapter
        ListView view = (ListView) solo.getView(R.id.ingredient_listview);
        Adapter adapter = view.getAdapter();

        // get the reference to some of the remaining elements
        ImageButton reverseButton = (ImageButton) solo.getView(R.id.sort_button);
        Drawable drawable = reverseButton.getBackground();
        Drawable newDrawable;

        if (adapter.getCount() == 1) {
            // if there is one element in the list view, test the sort and reverse function
            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.pressMenuItem(1);
            assertEquals(adapter.getCount(),1);
            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.pressMenuItem(2);
            assertEquals(adapter.getCount(),1);
            solo.clickOnView(solo.getView(R.id.sort_button));
            assertEquals(adapter.getCount(),1);
            solo.pressMenuItem(0);
            assertEquals(adapter.getCount(),1);
            solo.clickOnView(solo.getView(R.id.sort_spinner));
        } else if (adapter.getCount() > 1) {
            // if there are more than one elements in the list view, test the sort and reverse function

            // reference few of the fragment elements
            View listElement1;
            View listElement2;
            TextView Text1;
            TextView Text2;
            Integer compare; // used to store the compared result


            // sort by the description
            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.clickOnMenuItem("Description");
            // check the sequence
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.brief_description_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.brief_description_TextView_shopping);

            solo.sleep(2000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            solo.sleep(4000);
            assertTrue(compare <= 0);
            solo.sleep(2000);

            // reverse order
            solo.clickOnView(solo.getView(R.id.sort_button));
            // check the sequence
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.brief_description_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.brief_description_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare >= 0);
            solo.sleep(2000);

            // sort by the category
            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.clickOnMenuItem("Category");
            // check the sequence
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare <= 0);
            solo.sleep(2000);

            // reverse order
            solo.clickOnView(solo.getView(R.id.sort_button));
            // check the sequence
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare >= 0);
            solo.sleep(2000);

            // sort by the nothing
            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.clickOnMenuItem("  ----select----  ");
            // check the sequence
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare >= 0);
            solo.sleep(2000);

            // reverse order
            solo.clickOnView(solo.getView(R.id.sort_button));
            // check the sequence
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare <= 0);
            solo.sleep(2000);
        }

    }
}
