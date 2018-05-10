package com.dawit.android.ad340;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainTest {

    @Rule
    public ActivityTestRule<Main> mActivityTestRule = new ActivityTestRule<>(Main.class);

    @Test
    public void mainTest() {

        //check if activity displaying the text box
        String message = "Any message";
        ViewInteraction editText = onView(
                allOf(withId(R.id.editText), withContentDescription("Please, input the text"),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        //check if it's reading saved data
        ViewInteraction editText2 = onView(withText(message));
        ViewInteraction button = onView(
                allOf(withId(R.id.button),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        SharedPreferences sharedPreferences;



        //if fail
        editText.perform(replaceText(""));
        button.perform(click());
        onView(withText("Please enter the value")).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        //if success
        editText.perform(replaceText(message));
        Activity activity = mActivityTestRule.getActivity();
        button.perform(click());

        sharedPreferences = mActivityTestRule.getActivity().getPreferences(mActivityTestRule.getActivity().MODE_PRIVATE);
        //Test if data was stored
        Assert.assertEquals(message, sharedPreferences.getString("com.dawit.android.ad340.main.key", "No Value"));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
