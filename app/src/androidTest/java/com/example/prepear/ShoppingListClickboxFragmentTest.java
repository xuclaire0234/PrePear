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

public class ShoppingListClickboxFragmentTest {
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
    public void testClickBoxView() {
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
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));

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

    @Test
    public void testClickBoxCancelButton() {
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
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            CheckBox check = listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox);

            EditText actualAmount = (EditText) solo.getView(R.id.ingredient_actual_amount);
            EditText bestBeforeDate = (EditText) solo.getView(R.id.best_before_date);

            solo.enterText((EditText) solo.getView(R.id.ingredient_actual_amount),"3.0");
            solo.clickOnText("Cancel");
            solo.sleep(3000);

            listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            solo.clickOnView(solo.getView(R.id.ingredient_location));
            solo.pressMenuItem(0);
            solo.clickOnText("Cancel");
            solo.sleep(3000);

            listElement = view.getChildAt(0);
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));
            solo.enterText((EditText) solo.getView(R.id.best_before_date),"2023-1-30");
            solo.clickOnText("Cancel");
            solo.sleep(3000);
        }
    }

    @Test
    public void testOkButton() {
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
            solo.clickOnView(listElement.findViewById(R.id.ingredient_in_shopping_list_CheckBox));



        }

    }


}
