package com.example.prepear;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
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

import com.example.prepear.ui.Recipe.RecipeFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListTestEspresso {
    @Rule
        public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testAddDeleteRecipe() {
//        onView(withId(R.id.create_account)).perform(click());
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

    @Test
    public void testAddEditRecipe() {
        // checks cancel button of add edit recipe activity
        onView(withId(R.id.add_recipe_button)).perform(click());
        onView(withId(R.id.cancel_button)).perform(ScrollToAction.betterScrollTo()).perform(click());

        // checks entering stuff to add edit recipe activity
        onView(withId(R.id.add_recipe_button)).perform(click());
        onView(withId(R.id.title_EditText)).perform(ViewActions.typeText("Ice Cream"));
        onView(withId(R.id.preparation_time_EditText)).perform(ViewActions.typeText("32"));
        onView(withId(R.id.number_of_servings_EditText)).perform(ViewActions.typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.recipe_category_Spinner)).perform(click());
        onView(withText("Dessert")).perform(click());

        // checks cancel button of add edit ingredient fragment
        onView(withId(R.id.add_ingredient_in_recipe_button)).perform(click());
        onView(withText("Cancel")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        // checks confirm button in add edit ingredient fragment
        onView(withId(R.id.add_ingredient_in_recipe_button)).perform(click());
//        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("Milk"));
        onView(withId(R.id.ingredient_amount_edit_text)).perform(ViewActions.typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_unit_edit_text)).perform(click());
        onView(withText("L")).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.ingredient_category_edit_text)).perform(click());
        onView(withText("Other")).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.new_ingredient_category_edit_text)).perform(ViewActions.typeText("Liquid"), closeSoftKeyboard());
        onView(withText("Confirm")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.ingredient_in_recipe_ListView)).check(matches(hasDescendant(withText(containsString("Milk")))));

        // checks ok button in add edit ingredient fragment
        onData(anything()).inAdapterView(withId(R.id.ingredient_in_recipe_ListView)).atPosition(0).perform(click());
        onView(withId(R.id.ingredient_amount_edit_text)).perform(ViewActions.clearText()).perform(ViewActions.typeText("0.5"), closeSoftKeyboard());
        onView(withText("OK")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.ingredient_in_recipe_ListView)).check(matches(not(hasDescendant(withText(containsString("1"))))));

        // checks delete button in add edit ingredient fragment
        onData(anything()).inAdapterView(withId(R.id.ingredient_in_recipe_ListView)).atPosition(0).perform(click());
        onView(withText("Delete")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.ingredient_in_recipe_ListView)).check(matches(not(hasDescendant(withText(containsString("Milk"))))));

        // checks commit button in add edit recipe activity
        onView(withId(R.id.commit_button)).perform(ScrollToAction.betterScrollTo()).perform(click());
        onView(withId(R.id.recipe_listview)).check(matches(hasDescendant(withText(containsString("Ice Cream")))));

    }
}
