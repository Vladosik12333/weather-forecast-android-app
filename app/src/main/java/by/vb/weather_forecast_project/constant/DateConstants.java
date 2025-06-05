package by.vb.weather_forecast_project.constant;

import java.time.format.DateTimeFormatter;

public class DateConstants {
    public static final int INTERMEDIATE_DATE_TIME = 12;
    public static final int MAXIMUM_DAYS_FORECAST = 4;
    public static final String DATE_PICKER_NAME = "Forecast Date";
    public static final String DATE_PICKER_TAG = "DATE_PICKER_TAG";
    public static final String DATE_RAW_FORMAT = "dd.MM.yyyy";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateConstants.DATE_RAW_FORMAT);
    public static final String TIME_RAW_FORMAT = "hh:mm";
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(DateConstants.TIME_RAW_FORMAT);

    private DateConstants() {}
}
