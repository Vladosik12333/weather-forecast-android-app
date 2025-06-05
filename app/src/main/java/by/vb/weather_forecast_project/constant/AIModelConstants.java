package by.vb.weather_forecast_project.constant;

import by.vb.weather_forecast_project.BuildConfig;

public class AIModelConstants {
    public static final String BASIC_ERROR_AI_MODEL_MESSAGE = "Oooops... I'm out of service now. Try later please.";
    public static final String API_KEY_AI_MODEL = BuildConfig.AI_MODEL_API_KEY;
    public static final String ORGANIZATION_AI_MODEL = "org-KxHzjV5aqGmuI3b4hENA2fj7";
    public static final String PROJECT_AI_MODEL = "proj_6uJsXhmnuVpXrpsmB2OolaNm";
    public static final String AI_MODEL_MESSAGE_TEMPLATE = "WRITE ONE RESPONSE IN 25 OR LESS WORDS! DO NOT REPEAT THE TEMPERATURE! Your response is the recommendation what clothes to wear considering these weather conditions: %s , %.2f celsius , %s";

    private AIModelConstants() {}
}
