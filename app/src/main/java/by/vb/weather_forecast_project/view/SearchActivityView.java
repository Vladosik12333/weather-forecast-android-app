package by.vb.weather_forecast_project.view;

public interface SearchActivityView extends ActivityView {
    void showWeatherForecast(String placeName, long forecastDateTimestamp);

    void showForecastDate(String formattedDate);
}
