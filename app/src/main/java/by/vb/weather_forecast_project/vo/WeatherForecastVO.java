package by.vb.weather_forecast_project.vo;

import java.time.ZonedDateTime;

public class WeatherForecastVO {
    private final String cityName;
    private final double temperature;
    private final double temperatureFeels;
    private final String description;
    private final String mainCondition;
    private final String iconUrl;
    private final double windSpeed;
    private final int humidity;
    private final int pressure;
    private final int visibility;
    private final ZonedDateTime sunrise;
    private final ZonedDateTime sunset;

    private WeatherForecastVO(Builder builder) {
        this.cityName = builder.cityName;
        this.temperature = builder.temperature;
        this.temperatureFeels = builder.temperatureFeels;
        this.description = builder.description;
        this.mainCondition = builder.mainCondition;
        this.iconUrl = builder.iconUrl;
        this.windSpeed = builder.windSpeed;
        this.humidity = builder.humidity;
        this.pressure = builder.pressure;
        this.visibility = builder.visibility;
        this.sunrise = builder.sunrise;
        this.sunset = builder.sunset;
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTemperatureFeels() {
        return temperatureFeels;
    }

    public String getDescription() {
        return description;
    }

    public String getMainCondition() {
        return mainCondition;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity () {
        return humidity;
    }
    public int getPressure () {
        return pressure;
    }

    public int getVisibility() {
        return visibility;
    }

    public ZonedDateTime getSunrise() {
        return sunrise;
    }

    public ZonedDateTime getSunset() {
        return sunset;
    }

    public static class Builder {
        private String cityName;
        private double temperature;
        private double temperatureFeels;
        private String description;
        private String mainCondition;
        private String iconUrl;
        private double windSpeed;
        private int humidity;
        private int pressure;
        private int visibility;
        private ZonedDateTime sunrise;
        private ZonedDateTime sunset;

        public Builder setCityName(String cityName) {
            this.cityName = cityName;
            return this;
        }

        public Builder setTemperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder setTemperatureFeels(double temperatureFeels) {
            this.temperatureFeels = temperatureFeels;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setMainCondition(String mainCondition) {
            this.mainCondition = mainCondition;
            return this;
        }

        public Builder setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder setHumidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder setPressure(int pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder setVisibility(int visibility) {
            this.visibility = visibility;
            return this;
        }

        public Builder setSunrise(ZonedDateTime sunrise) {
            this.sunrise = sunrise;
            return this;
        }

        public Builder setSunset(ZonedDateTime sunset) {
            this.sunset = sunset;
            return this;
        }

        public WeatherForecastVO build () {
            return new WeatherForecastVO(this);
        }
    }
}