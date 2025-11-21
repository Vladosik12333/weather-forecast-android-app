package by.vb.weather_forecast_project.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.DateConstants;
import by.vb.weather_forecast_project.view.SearchActivityView;

public class SearchController {
    private final SearchActivityView view;
    private LocalDate originalDate;
private String example;
    public SearchController(SearchActivityView view) {
        this.view = view;
    }

    public void handleSubmit(String placeName) {
        if (placeName.isEmpty() || originalDate == null) {
            view.showError(ApplicationConstants.EMPTY_FIELD_ERROR_MESSAGE);
        } else {
            view.showWeatherForecast(placeName, originalDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
    }

    public void handleForecastDate(long timestamp) {
        originalDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.UTC).toLocalDate();
        String formattedDate = originalDate.format(DateConstants.DATE_FORMAT);
        view.showForecastDate(formattedDate);
    }
}