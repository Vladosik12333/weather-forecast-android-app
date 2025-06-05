package by.vb.weather_forecast_project.enums;

public enum WeatherConditionEnum {
    RAIN("rain"), CLEAR("clear"), CLOUDS("clouds"), SNOW("snow");

    private final String rowName;

    WeatherConditionEnum(String rowName) {
        this.rowName = rowName;
    }

    public String getRowName() {
        return rowName;
    }
}
