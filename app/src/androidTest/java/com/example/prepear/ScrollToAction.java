package com.example.prepear;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.AllOf.allOf;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Matcher;

public final class ScrollToAction implements ViewAction {
    private static final String TAG = ScrollToAction.class.getSimpleName();

    @SuppressWarnings("unchecked")
    @Override
    public Matcher<View> getConstraints() {
        return allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), isDescendantOfA(anyOf(
                isAssignableFrom(ScrollView.class), isAssignableFrom(HorizontalScrollView.class), isAssignableFrom(NestedScrollView.class))));
    }

    @Override
    public void perform(UiController uiController, View view) {
        if (isDisplayingAtLeast(80).matches(view)) {
            Log.i(TAG, "View is already displayed. Returning.");
            return;
        }
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
            Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.");
        }
        uiController.loopMainThreadUntilIdle();
        if (!isDisplayingAtLeast(80).matches(view)) {
            throw new PerformException.Builder()
                    .withActionDescription(this.getDescription())
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(new RuntimeException(
                            "Scrolling to view was attempted, but the view is not displayed"))
                    .build();
        }
    }
    public static ViewAction betterScrollTo() {
        return ViewActions.actionWithAssertions(new ScrollToAction());
    }

    @Override
    public String getDescription() {
        return "scroll to";
    }}
