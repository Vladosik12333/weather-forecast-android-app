package by.vb.weather_forecast_project.view;

import by.vb.weather_forecast_project.vo.AIModelResponseVO;
import by.vb.weather_forecast_project.vo.WeatherForecastVO;

public interface WeatherForecastActivityView extends ActivityView {
    void showWeatherForecast(WeatherForecastVO weatherForecastVO);
    void showAIModelRecommendation(AIModelResponseVO aiModelResponseVO);
    void showLoading();
    void hideLoading();
}
