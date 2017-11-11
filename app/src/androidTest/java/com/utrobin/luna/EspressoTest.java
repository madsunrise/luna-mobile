package com.utrobin.luna;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.utrobin.luna.ui.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



/**
 * Created by ivan on 12.11.2017.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        try {
            Thread.sleep(4000); // dirty hack
        }
        catch (Exception ex) { }
        Espresso.onView(ViewMatchers.withId(R.id.feed_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(10));
    }
}

