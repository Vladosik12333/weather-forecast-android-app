package by.vb.weather_forecast_project.vo;

public class AIModelResponseVO {
    private final String message;

    public String getMessage() {
        return message;
    }

    private AIModelResponseVO(AIModelResponseVO.Builder builder) {
        this.message = builder.message;
    }

    public static class Builder {
        private String message;

        public AIModelResponseVO.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public AIModelResponseVO build () {
            return new AIModelResponseVO(this);
        }
    }
}
