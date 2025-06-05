package by.vb.weather_forecast_project.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.openai.client.okhttp.OpenAIOkHttpClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.Executors;

import by.vb.weather_forecast_project.constant.AIModelConstants;
import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.DateConstants;
import by.vb.weather_forecast_project.model.AIModelRepository;
import by.vb.weather_forecast_project.model.RequestCallback;
import by.vb.weather_forecast_project.vo.AIModelRequestVO;
import by.vb.weather_forecast_project.vo.AIModelResponseVO;
import by.vb.weather_forecast_project.vo.WeatherForecastVO;
import by.vb.weather_forecast_project.model.WeatherForecastRepository;
import by.vb.weather_forecast_project.view.WeatherForecastActivityView;
import okhttp3.OkHttpClient;

public class WeatherForecastController {
    private final WeatherForecastActivityView view;
    private final WeatherForecastRepository forecastRepository;
    private final AIModelRepository aiModelRepository;

    public WeatherForecastController(WeatherForecastActivityView view, Context context) {
        this.view = view;
        this.forecastRepository = new WeatherForecastRepository(context, new Handler(Looper.getMainLooper()), new OkHttpClient());
        this.aiModelRepository = new AIModelRepository(
                context,
                new Handler(Looper.getMainLooper()),
                new OpenAIOkHttpClient.Builder()
                        .apiKey(AIModelConstants.API_KEY_AI_MODEL)
                        .organization(AIModelConstants.ORGANIZATION_AI_MODEL)
                        .project(AIModelConstants.PROJECT_AI_MODEL)
                        .build(),
                Executors.newSingleThreadExecutor());
    }

    public void fetchWeatherForecast(String placeName, long forecastDateTimestamp) {
        if (forecastDateTimestamp <= 0) {
            view.showError(ApplicationConstants.NOT_EXPECTED_ERROR_MESSAGE);
            return;
        }

        LocalDateTime forecastDate = Instant.ofEpochMilli(forecastDateTimestamp).atZone(ZoneOffset.UTC).toLocalDateTime().withHour(DateConstants.INTERMEDIATE_DATE_TIME);

        view.showLoading();

        forecastRepository.getWeatherForecast(placeName, forecastDate, new RequestCallback<WeatherForecastVO>() {
            @Override
            public void onSuccess(WeatherForecastVO weatherForecastVO) {
                view.showWeatherForecast(weatherForecastVO);

                AIModelRequestVO aiModelRequestVO = new AIModelRequestVO.Builder().setMessage(String.format(AIModelConstants.AI_MODEL_MESSAGE_TEMPLATE, weatherForecastVO.getMainCondition(), weatherForecastVO.getTemperature(), weatherForecastVO.getDescription())).build();

                aiModelRepository.fetchAIModelResponse(aiModelRequestVO, new RequestCallback<AIModelResponseVO>() {
                    @Override
                    public void onSuccess(AIModelResponseVO aiModelResponseVO) {
                        view.showAIModelRecommendation(aiModelResponseVO);
                        view.hideLoading();
                    }

                    @Override
                    public void onFailure(String error) {
                        AIModelResponseVO aiModelResponseVO = new AIModelResponseVO.Builder().setMessage(error).build();
                        view.showAIModelRecommendation(aiModelResponseVO);
                        view.hideLoading();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                view.showError(error);
            }
        });
    }
}