package by.vb.weather_forecast_project.model;

public interface RequestCallback<T> {
    void onSuccess(T weatherForecastVO);

    void onFailure(String error);
}