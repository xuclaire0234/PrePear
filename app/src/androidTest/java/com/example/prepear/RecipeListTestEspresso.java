package com.example.prepear;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.Espresso.onView;

import static com.google.common.base.CharMatcher.is;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

import static java.util.EnumSet.allOf;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListTestEspresso {
    @Rule
    public ActivityScenarioRule<ViewRecipeListActivity> activityRule =
            new ActivityScenarioRule<>(ViewRecipeListActivity.class);

    @Test
    public void testAddDeleteRecipe () {
        // checks adding new recipe called orange juice to recipe list
        onView(withId(R.id.add_recipe_button)).perform(click());
        onView(withId(R.id.title_EditText)).perform(ViewActions.typeText("Orange Juice"));
        onView(withId(R.id.preparation_time_EditText)).perform(ViewActions.typeText("15"));
        onView(withId(R.id.number_of_servings_EditText)).perform(ViewActions.typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.recipe_category_Spinner)).perform(click());
        onView(withText("Dessert")).perform(click());
        onView(withId(R.id.commit_button)).perform(ScrollToAction.betterScrollTo()).perform(click());

        // checks deleting the newly added recipe from recipe list
        onData(anything()).inAdapterView(withId(R.id.recipe_listview)).atPosition(3).perform(click());
        onView(withId(R.id.delete_button)).perform(ScrollToAction.betterScrollTo()).perform(click());
        onView(withId(R.id.recipe_listview)).check(matches(not(hasDescendant(withText(containsString("Orange Juice"))))));
    }
}
