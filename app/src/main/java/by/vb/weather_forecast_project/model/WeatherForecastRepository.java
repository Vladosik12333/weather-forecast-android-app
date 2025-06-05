package by.vb.weather_forecast_project.model;

import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.ForecastConstants;
import by.vb.weather_forecast_project.util.NetworkConnectivity;
import by.vb.weather_forecast_project.vo.WeatherForecastVO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WeatherForecastRepository {
    private final OkHttpClient client;
    private final Context context;
    private final Handler handler;

    public WeatherForecastRepository(Context context, Handler handler, OkHttpClient client) {
        this.context = context.getApplicationContext();
        this.handler = handler;
        this.client = client;
    }

    public void getWeatherForecast(String placeName, LocalDateTime forecastDate, RequestCallback<WeatherForecastVO> callback) {
        if (!NetworkConnectivity.isAvailable(context)) {
            handleError(ApplicationConstants.NO_CONNECTION_ERROR_MESSAGE, callback);
            return;
        }

        String url = String.format(ForecastConstants.WEATHER_FORECAST_URL, placeName, ForecastConstants.WEATHER_FORECAST_API_KEY);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException error) {
                handleError(error.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    handleError(response, callback);
                    return;
                }

                try {
                    JSONObject weatherForecastDateJSONObject = getWeatherForecastDateJSONObject(response.body(), forecastDate);

                    if (weatherForecastDateJSONObject == null)
                        throw new JSONException(ApplicationConstants.NOT_EXPECTED_ERROR_MESSAGE);

                    WeatherForecastVO weatherForecastVO = parseWeatherForecastData(weatherForecastDateJSONObject);

                    handleSuccess(weatherForecastVO, callback);
                } catch (JSONException | NullPointerException | IOException exception) {
                    handleError(ForecastConstants.PARSING_ERROR_MESSAGE, callback);
                }
            }
        });
    }

    private void handleSuccess(WeatherForecastVO weatherForecastVO, RequestCallback<WeatherForecastVO> callback) {
        handler.post(() ->
                callback.onSuccess(weatherForecastVO));
    }

    private void handleError(Response response, RequestCallback<WeatherForecastVO> callback) {
        String message;

        switch (response.code()) {
            case ApplicationConstants.BAD_REQUEST_CODE:
                message = ForecastConstants.PLACE_NOT_FOUND_ERROR_MESSAGE;
                break;
            default:
                message = ApplicationConstants.NOT_EXPECTED_ERROR_MESSAGE;
                break;
        }

        handler.post(() -> {
            callback.onFailure(message);
        });
    }

    private void handleError(String response, RequestCallback<WeatherForecastVO> callback) {
        handler.post(() -> {
            callback.onFailure(response);
        });
    }

    private JSONObject getWeatherForecastDateJSONObject(ResponseBody responseBody, LocalDateTime forecastDate) throws IOException, JSONException {
        if (responseBody == null) throw new NullPointerException();

        JSONObject json = new JSONObject(responseBody.string());
        JSONArray weatherForecastList = json.getJSONArray("list");

        for (int i = 0; i < weatherForecastList.length(); i++) {
            JSONObject weatherForecast = weatherForecastList.getJSONObject(i);
            long weatherForecastTimestamp = weatherForecast.getLong("dt");

            LocalDateTime weatherForecastDate = Instant.ofEpochSecond(weatherForecastTimestamp).atZone(ZoneOffset.UTC).toLocalDateTime();
            if (forecastDate.isEqual(weatherForecastDate)) {
                weatherForecast.put("city", json.getJSONObject("city"));
                return weatherForecast;
            }
        }

        return null;
    }

    private WeatherForecastVO parseWeatherForecastData(JSONObject weatherForecastJSONObject) throws JSONException {
        WeatherForecastVO.Builder builder = new WeatherForecastVO.Builder();

        builder.setCityName(weatherForecastJSONObject.getJSONObject("city").getString("name"))
                .setTemperature(weatherForecastJSONObject.getJSONObject("main").getDouble("temp"))
                .setTemperatureFeels(weatherForecastJSONObject.getJSONObject("main").getDouble("feels_like"))
                .setHumidity(weatherForecastJSONObject.getJSONObject("main").getInt("humidity"))
                .setDescription(weatherForecastJSONObject.getJSONArray("weather")
                        .getJSONObject(0).getString("description"))
                .setMainCondition(weatherForecastJSONObject.getJSONArray("weather")
                        .getJSONObject(0).getString("main"))
                .setIconUrl("https://openweathermap.org/img/wn/" + weatherForecastJSONObject.getJSONArray("weather")
                        .getJSONObject(0).getString("icon") + "@2x.png")
                .setWindSpeed(weatherForecastJSONObject.getJSONObject("wind").getDouble("speed"))
                .setPressure(weatherForecastJSONObject.getJSONObject("main").getInt("pressure"))
                .setVisibility(weatherForecastJSONObject.getInt("visibility"))
                .setSunrise(Instant.ofEpochSecond(weatherForecastJSONObject.getJSONObject("city").getLong("sunrise"))
                        .atZone(ZoneId.of(ZoneId.systemDefault().getId())))
                .setSunset(Instant.ofEpochSecond(weatherForecastJSONObject.getJSONObject("city").getLong("sunset"))
                        .atZone(ZoneId.of(ZoneId.systemDefault().getId())));

        return builder.build();
    }
}