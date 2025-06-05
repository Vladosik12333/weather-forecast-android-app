package by.vb.weather_forecast_project.controller;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.DateConstants;
import by.vb.weather_forecast_project.view.SearchActivityView;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {
    @Mock
    private SearchActivityView searchActivityView;

    private SearchController searchController;

    @Before
    public void setUp() {
        searchController = new SearchController(searchActivityView);
    }

    @After
    public void turnDown() {
        searchController = null;
    }

    @Test
    public void handleSubmitMethodCall_withCityAndTimestamp_shallCallViewSuccessMethod() {
        Instant instant = fillTimestamp();
        String city = "Warsaw";

        searchController.handleSubmit(city);

        verify(searchActivityView, atLeastOnce()).showWeatherForecast(city,
                instant.atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Test
    public void handleSubmitMethodCall_withoutTimestamp_shallCallViewSuccessMethod() {
        String city = "Warsaw";

        searchController.handleSubmit(city);

        verify(searchActivityView, atLeastOnce()).showError(ApplicationConstants.EMPTY_FIELD_ERROR_MESSAGE);
    }

    @Test
    public void handleSubmitMethodCall_withoutCity_shallCallViewSuccessMethod() {
        fillTimestamp();
        String city = "";

        searchController.handleSubmit(city);

        verify(searchActivityView, atLeastOnce()).showError(ApplicationConstants.EMPTY_FIELD_ERROR_MESSAGE);
    }

    @Test
    public void handleSubmitMethodCall_withoutCityAndTimestamp_shallCallViewSuccessMethod() {
        String city = "";

        searchController.handleSubmit(city);

        verify(searchActivityView, atLeastOnce()).showError(ApplicationConstants.EMPTY_FIELD_ERROR_MESSAGE);
    }

    @Test
    public void handleForecastDateMethodCall_withTimestamp_shallCallViewSuccessMethod() {
        Instant instant = fillTimestamp();
        String formattedDate = LocalDate.ofInstant(instant, ZoneOffset.UTC).format(DateConstants.DATE_FORMAT);

        verify(searchActivityView, atLeastOnce()).showForecastDate(formattedDate);
    }

    private Instant fillTimestamp() {
        Instant instant = Instant.now();
        long timestamp = instant.toEpochMilli();

        searchController.handleForecastDate(timestamp);

        return instant;
    }
}
