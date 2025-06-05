package by.vb.weather_forecast_project.constant;

import by.vb.weather_forecast_project.BuildConfig;

public class ForecastConstants {
    public static final String PARSING_ERROR_MESSAGE = "Parsing weather forecast data error";;
    public static final String PLACE_NOT_FOUND_ERROR_MESSAGE = "Such city not found";
    public static final String WEATHER_FORECAST_API_KEY = BuildConfig.FORECAST_API_KEY;
    public static final String WEATHER_FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&appid=%s";

    private ForecastConstants () {}
}
