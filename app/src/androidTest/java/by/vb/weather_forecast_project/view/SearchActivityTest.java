package by.vb.weather_forecast_project.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import by.vb.weather_forecast_project.R;
import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.DateConstants;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchActivityTest {
    private ActivityScenario<SearchActivity> scenario;

    @Before
    public void startScenario() {
        Intents.init();
        scenario = ActivityScenario.launch(SearchActivity.class);
    }

    @After
    public void endScenario() {
        Intents.release();
        scenario.close();
    }

    @Test
    public void givenRightCityAndDate_whenSubmitForm_thenLaunchWeatherForecastActivityWithIntent() {
        String cityName = "Warsaw";
        LocalDateTime date = ZonedDateTime.now(ZoneId.systemDefault()).toLocalDate().plusDays(DateConstants.MAXIMUM_DAYS_FORECAST).minusDays(2).atStartOfDay();
        long dateMillis = date.toInstant(ZoneOffset.UTC).toEpochMilli();

        onView(withId(R.id.cityInput)).perform(replaceText(cityName));

        onView(withId(R.id.dateInput)).perform(click());

        onView(withContentDescription(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)))).perform(click());

        onView(withId(com.google.android.material.R.id.confirm_button)).perform(click());

        onView(withId(R.id.getWeatherButton)).perform(click());

        intended(hasComponent(WeatherForecastActivity.class.getName()));
        intended(hasExtra(ApplicationConstants.PLACE_NAME_KEY, cityName));
        intended(hasExtra(ApplicationConstants.DATE_KEY, dateMillis));
    }

    @Test
    public void givenEmptyCityAndDateInput_whenSubmitForm_thenStayOnTheSearchView() {
        onView(withId(R.id.cityInput)).perform(replaceText(""));
        onView(withId(R.id.dateInput)).perform(replaceText(""));

        onView(withId(R.id.getWeatherButton)).check(matches(isDisplayed()));
    }
}
