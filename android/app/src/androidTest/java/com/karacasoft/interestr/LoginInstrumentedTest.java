package com.karacasoft.interestr;

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Token;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by karacasoft on 30.11.2017.
 */

@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Mock
    InterestrAPI api;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,
            true
    );

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void loginWorksOnRightInput() {

        onView(withId(R.id.etUsername))
                .perform(typeText("KaracaSoft"))
                .check(matches(isDisplayed()));

        onView(withId(R.id.etPassword))
                .perform(typeText("Testing"))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnLogin))
                .perform(click());

        onView(withId(R.id.news_feed_content))
                .check(matches(isDisplayed()));


    }

    @Test
    public void loginWorksOnWrongInput() {

        onView(withId(R.id.etUsername))
                .perform(typeText("Error"))
                .check(matches(isDisplayed()));

        onView(withId(R.id.etPassword))
                .perform(typeText("Testing"))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnLogin))
                .perform(click());

        onView(withText("I am error"))
                .check(matches(isDisplayed()));
    }


    @Test
    public void signupButtonWorks() {
        onView(withId(R.id.btnSignUp))
                .perform(click());

        onView(withId(R.id.sign_up_layout))
                .check(matches(isDisplayed()));
    }

}
