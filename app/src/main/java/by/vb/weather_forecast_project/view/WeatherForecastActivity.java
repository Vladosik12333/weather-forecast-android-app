package by.vb.weather_forecast_project.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import by.vb.weather_forecast_project.R;
import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.DateConstants;
import by.vb.weather_forecast_project.controller.WeatherForecastController;
import by.vb.weather_forecast_project.enums.WeatherConditionEnum;
import by.vb.weather_forecast_project.vo.AIModelResponseVO;
import by.vb.weather_forecast_project.vo.WeatherForecastVO;
import by.vb.weather_forecast_project.util.ImageProcessor;

public class WeatherForecastActivity extends AppCompatActivity implements WeatherForecastActivityView {
    private ImageView mainBackground;
    private FrameLayout loaderBackground;
    private ImageView weatherIcon;
    private TextView placeText;
    private TextView temperatureText;
    private TextView temperatureTextFeels;
    private TextView weatherDescription;
    private TextView humidityText;
    private TextView windSpeedText;
    private TextView pressureText;
    private TextView visibilityText;
    private TextView sunriseText;
    private TextView sunsetText;
    private TextView aiModelRecommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        mainBackground = findViewById(R.id.weatherBackground);
        weatherIcon = findViewById(R.id.weatherIcon);
        placeText = findViewById(R.id.cityNameText);
        temperatureText = findViewById(R.id.temperatureText);
        temperatureTextFeels = findViewById(R.id.temperatureTextFeels);
        weatherDescription = findViewById(R.id.weatherDescription);
        loaderBackground = findViewById(R.id.loaderBackground);
        humidityText = findViewById(R.id.humidity);
        windSpeedText = findViewById(R.id.windSpeed);
        pressureText = findViewById(R.id.pressureText);
        visibilityText = findViewById(R.id.visibilityText);
        sunriseText = findViewById(R.id.sunriseText);
        sunsetText = findViewById(R.id.sunsetText);
        aiModelRecommendation = findViewById(R.id.aiModelRecommendation);
        Button goBackBtn = findViewById(R.id.backButton);

        String placeName = getIntent().getStringExtra(ApplicationConstants.PLACE_NAME_KEY);
        long forecastDateTimestamp = getIntent().getLongExtra(ApplicationConstants.DATE_KEY, -1);

        WeatherForecastController controller = new WeatherForecastController(this, this);
        controller.fetchWeatherForecast(placeName, forecastDateTimestamp);

        goBackBtn.setOnClickListener(v -> finish());
    }

    @Override
    public void showWeatherForecast(WeatherForecastVO weatherForecastVO) {
        placeText.setText(weatherForecastVO.getCityName());
        temperatureText.setText(getString(R.string.temperature_format, weatherForecastVO.getTemperature()));
        temperatureTextFeels.setText(getString(R.string.temperature_feels_format, weatherForecastVO.getTemperatureFeels()));
        weatherDescription.setText(weatherForecastVO.getDescription());
        humidityText.setText(getString(R.string.humidity_format, weatherForecastVO.getHumidity()));
        windSpeedText.setText(getString(R.string.wind_speed_format, weatherForecastVO.getWindSpeed()));
        pressureText.setText(getString(R.string.pressure_format, weatherForecastVO.getPressure()));
        visibilityText.setText(getString(R.string.visibility_format, weatherForecastVO.getVisibility()));
        sunriseText.setText(weatherForecastVO.getSunrise().format(DateConstants.TIME_FORMAT));
        sunsetText.setText(weatherForecastVO.getSunset().format(DateConstants.TIME_FORMAT));

        ImageProcessor.loadImage(this, weatherForecastVO.getIconUrl(), weatherIcon);

        int backgroundResId = getBackgroundResource(weatherForecastVO.getMainCondition());
        mainBackground.setImageResource(backgroundResId);
    }

    @Override
    public void showAIModelRecommendation(AIModelResponseVO aiModelResponseVO) {
        aiModelRecommendation.setText(aiModelResponseVO.getMessage());
    }

    @Override
    public void showLoading() {
        loaderBackground.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loaderBackground.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        String messageToShow = String.format(ApplicationConstants.BASIC_ERROR_MESSAGE, message);
        Toast.makeText(this, messageToShow, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private int getBackgroundResource(String condition) {
        WeatherConditionEnum conditionEnum = WeatherConditionEnum.valueOf(condition.toUpperCase());

        switch (conditionEnum) {
            case RAIN:
                return R.drawable.rainy_background;
            case CLEAR:
                return R.drawable.sunny_background;
            case CLOUDS:
                return R.drawable.cloudy_background;
            case SNOW:
                return R.drawable.snow_background;
            default:
                return R.drawable.default_background;
        }
    }
}