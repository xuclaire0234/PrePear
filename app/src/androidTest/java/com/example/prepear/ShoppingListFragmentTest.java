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

public class ShoppingListFragmentTest {
    private Solo solo;

    @IdRes
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
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);


    @Test
    public void testFromDatePicker() {
        solo.clickOnText("Shopping List Time Range");
        solo.clickOnView(solo.getView(R.id.fromDate_textView));
        solo.clickOnText("OK");
        solo.sleep(3000);
        solo.clickOnButton(0);
        TextView fromDateText = (TextView) solo.getView(R.id.fromDate_textView);
        assertFalse(fromDateText.getText().length() > 0);
    }

    @Test
    public void testToDatePicker() {
        solo.clickOnText("Shopping List Time Range");
        solo.clickOnView(solo.getView(R.id.toDate_textView));
        solo.clickOnText("OK");
        solo.sleep(3000);
        solo.clickOnButton(0);
        TextView toDateText = (TextView) solo.getView(R.id.toDate_textView);
        assertTrue(toDateText.getText().length() == 0);
    }

    @Test
    public void testDatePicked() {
        solo.clickOnText("Shopping List Time Range");

        EditText fromDateText = (EditText) solo.getView(R.id.fromDate_textView);
        EditText toDateText = (EditText) solo.getView(R.id.toDate_textView);

        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-10-01");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        assertFalse(fromDateText.getText().length() > 0);
        assertFalse(toDateText.getText().length() > 0);

        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        assertFalse(fromDateText.getText().length() > 0);
        assertFalse(toDateText.getText().length() > 0);

        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-10-01");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        assertFalse(fromDateText.getText().length() > 0);
        assertFalse(toDateText.getText().length() > 0);

        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-11");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-11");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);
        assertEquals(fromDateText.getText().toString(),"2022-11-11");
        assertEquals(toDateText.getText().toString(),"2022-12-11");
    }

    @Test
    public void testListView() {
        solo.clickOnText("Shopping List Time Range");
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-30");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);

        solo.sleep(10000);

        ListView view = (ListView) solo.getView(R.id.ingredient_listview);
        Adapter adapter = view.getAdapter();
        if (adapter.getCount() != 0) {
            View listElement = view.getChildAt(0);
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

    @Test
    public void testSortAndReverse() {
        solo.clickOnText("Shopping List Time Range");
        solo.enterText((EditText) solo.getView(R.id.fromDate_textView), "2022-11-01");
        solo.goBack();
        solo.enterText((EditText) solo.getView(R.id.toDate_textView), "2022-12-30");
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnButton(0);

        solo.sleep(10000);

        ListView view = (ListView) solo.getView(R.id.ingredient_listview);
        Adapter adapter = view.getAdapter();
        ImageButton reverseButton = (ImageButton) solo.getView(R.id.sort_button);
        Drawable drawable = reverseButton.getBackground();
        Drawable newDrawable;

        if (adapter.getCount() == 1) {
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
            newDrawable = reverseButton.getBackground();
            assertNotEquals(newDrawable,drawable);
        } else if (adapter.getCount() > 1) {
            View listElement1;
            View listElement2;
            TextView Text1;
            TextView Text2;
            Integer compare;


            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.clickOnMenuItem("Description");
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.brief_description_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.brief_description_TextView_shopping);

            solo.sleep(2000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            solo.sleep(4000);
            assertTrue(compare >= 0);
            solo.sleep(2000);

            solo.clickOnView(solo.getView(R.id.sort_button));
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.brief_description_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.brief_description_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare <= 0);
            newDrawable = reverseButton.getBackground();
            assertNotEquals(newDrawable,drawable);
            solo.sleep(2000);

            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.clickOnMenuItem("Category");
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare <= 0);
            solo.sleep(2000);

            solo.clickOnView(solo.getView(R.id.sort_button));
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare <= 0);
            newDrawable = reverseButton.getBackground();
            assertNotEquals(newDrawable,drawable);
            solo.sleep(2000);

            solo.clickOnView(solo.getView(R.id.sort_spinner));
            solo.pressMenuItem(0);
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare <= 0);
            solo.sleep(2000);

            solo.clickOnView(solo.getView(R.id.sort_button));
            listElement1 = view.getChildAt(0);
            listElement2 = view.getChildAt(1);
            Text1 = listElement1.findViewById(R.id.ingredient_category_TextView_shopping);
            Text2 = listElement2.findViewById(R.id.ingredient_category_TextView_shopping);
            solo.sleep(4000);
            compare = Text1.getText().toString().compareTo(Text2.getText().toString());
            assertTrue(compare >= 0);
            newDrawable = reverseButton.getBackground();
            assertNotEquals(newDrawable,drawable);
            solo.sleep(2000);
        }

    }
}
