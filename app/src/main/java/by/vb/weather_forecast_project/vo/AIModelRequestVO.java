package by.vb.weather_forecast_project.vo;

public class AIModelRequestVO {
    private final String message;

    public String getMessage() {
        return message;
    }

    private AIModelRequestVO(Builder builder) {
        this.message = builder.message;
    }

    public static class Builder {
        private String message;

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public AIModelRequestVO build () {
            return new AIModelRequestVO(this);
        }
    }
}
