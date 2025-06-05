package by.vb.weather_forecast_project.view;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.DateConstants;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WeatherForecastActivityTest {
    private final String cityName = "Warsaw";
    private final long dateMillis = ZonedDateTime.now(ZoneId.systemDefault()).toLocalDate().plusDays(DateConstants.MAXIMUM_DAYS_FORECAST).minusDays(2).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();

    private ActivityScenario<WeatherForecastActivity> scenario;

    @Before
    public void startScenario() {
        Intents.init();

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), WeatherForecastActivity.class);
        intent.putExtra(ApplicationConstants.PLACE_NAME_KEY, cityName);
        intent.putExtra(ApplicationConstants.DATE_KEY, dateMillis);

        scenario = ActivityScenario.launch(intent);
    }

    @After
    public void endScenario() {
        Intents.release();
        scenario.close();
    }

    @Test
    public void givenRightCityAndDate_whenOpenView_thenLaunchWeatherForecastActivityWithIntent() {
        intended(hasComponent(WeatherForecastActivity.class.getName()));
        intended(hasExtra(ApplicationConstants.PLACE_NAME_KEY, cityName));
        intended(hasExtra(ApplicationConstants.DATE_KEY, dateMillis));
    }
}
