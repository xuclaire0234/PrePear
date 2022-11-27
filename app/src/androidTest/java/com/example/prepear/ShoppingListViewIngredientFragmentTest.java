package com.example.prepear;

import static org.junit.Assert.assertTrue;

import android.widget.Adapter;
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

public class ShoppingListViewIngredientFragmentTest {

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
    public void testView() {
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
            solo.clickInList(0);

            TextView descriptionText = (TextView) solo.getView(R.id.brief_description);
            TextView amountText = (TextView) solo.getView(R.id.ingredient_amount);
            TextView unitText = (TextView) solo.getView(R.id.ingredient_unit);
            TextView categoryText = (TextView) solo.getView(R.id.ingredient_category);
            assertTrue(descriptionText.getText().length() > 0);
            assertTrue(amountText.getText().length() > 0);
            assertTrue(unitText.getText().length() > 0);
            assertTrue(categoryText.getText().length() > 0);

            Integer index = adapter.getCount() - 1;
            if (index != 0) {
                solo.clickInList(index);
                descriptionText = (TextView) solo.getView(R.id.brief_description);
                amountText = (TextView) solo.getView(R.id.ingredient_amount);
                unitText = (TextView) solo.getView(R.id.ingredient_unit);
                categoryText = (TextView) solo.getView(R.id.ingredient_category);
                assertTrue(descriptionText.getText().length() > 0);
                assertTrue(amountText.getText().length() > 0);
                assertTrue(unitText.getText().length() > 0);
                assertTrue(categoryText.getText().length() > 0);
            }
        }
    }
}
