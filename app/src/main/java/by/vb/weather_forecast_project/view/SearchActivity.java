package by.vb.weather_forecast_project.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;

import by.vb.weather_forecast_project.constant.ApplicationConstants;
import by.vb.weather_forecast_project.constant.DateConstants;
import by.vb.weather_forecast_project.controller.SearchController;
import by.vb.weather_forecast_project.R;

public class SearchActivity extends AppCompatActivity implements SearchActivityView {
    private EditText placeInput;
    private EditText dateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Weather_Forecast_Project);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        placeInput = findViewById(R.id.cityInput);
        dateInput = findViewById(R.id.dateInput);
        Button weatherBtn = findViewById(R.id.getWeatherButton);

        SearchController controller = new SearchController(this);

        addCalendar(controller);

        weatherBtn.setOnClickListener(view -> {
            String placeName = placeInput.getText().toString().trim();
            controller.handleSubmit(placeName);
        });
    }

    @Override
    public void showWeatherForecast(String placeName, long forecastDateTimestamp) {
        Intent intent = new Intent(this, WeatherForecastActivity.class);
        intent.putExtra(ApplicationConstants.PLACE_NAME_KEY, placeName);
        intent.putExtra(ApplicationConstants.DATE_KEY, forecastDateTimestamp);
        startActivity(intent);
    }

    @Override
    public void showForecastDate(String formattedDate) {
        dateInput.setText(formattedDate);
    }

    @Override
    public void showError(String message) {
        String messageToShow = String.format(ApplicationConstants.BASIC_ERROR_MESSAGE, message);
        Toast.makeText(this, messageToShow, Toast.LENGTH_LONG).show();
    }

    private void addCalendar (SearchController controller) {
        ZonedDateTime todayZonedDate = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime todayUTCDate = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime allowedStartDate = todayZonedDate.isAfter(todayUTCDate) ? todayZonedDate : todayUTCDate;

        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(CompositeDateValidator.allOf(Arrays.asList(DateValidatorPointForward.from(allowedStartDate.toInstant().toEpochMilli()),
                        DateValidatorPointBackward.before(todayUTCDate.plusDays(DateConstants.MAXIMUM_DAYS_FORECAST).toInstant().toEpochMilli()))));

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(DateConstants.DATE_PICKER_NAME)
                .setCalendarConstraints(calendarConstraintsBuilder.build())
                .setTheme(R.style.CustomMaterialDatePickerTheme).build();

        dateInput.setOnClickListener(view -> datePicker.show(getSupportFragmentManager(), DateConstants.DATE_PICKER_TAG));

        datePicker.addOnPositiveButtonClickListener(controller::handleForecastDate);
    }
}