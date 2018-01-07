package com.utrobin.luna;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.utrobin.luna.model.Master;
import com.utrobin.luna.ui.view.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by ivan on 12.11.2017.
 */

@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test() throws Exception {
        Thread.sleep(1000);

        // проверяем что список виден
        Espresso.onView(ViewMatchers.withId(R.id.feed_recycler_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        List<Master> masters = activityRule.getActivity().getFeedItems();

        // проходимся по списку
        for (int i = 0; i < 50; ++i) {
            onView(withId(R.id.feed_recycler_view)).perform(RecyclerViewActions.scrollToPosition(i));

            onView(withId(R.id.feed_recycler_view)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(i, clickChildViewWithId(R.id.header)));

            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                    .check(matches(withText(masters.get(i).getName())));

            Espresso.pressBack();
        }
    }

    private static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}