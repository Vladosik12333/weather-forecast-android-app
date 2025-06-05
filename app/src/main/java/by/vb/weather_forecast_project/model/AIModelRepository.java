package by.vb.weather_forecast_project.model;

import android.content.Context;
import android.os.Handler;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.util.Optional;
import java.util.concurrent.Executor;

import by.vb.weather_forecast_project.constant.AIModelConstants;
import by.vb.weather_forecast_project.util.NetworkConnectivity;
import by.vb.weather_forecast_project.vo.AIModelRequestVO;
import by.vb.weather_forecast_project.vo.AIModelResponseVO;

public class   AIModelRepository {
    private final OpenAIClient client;
    private final Context context;
    private final Handler handler;
    private final Executor executor;

    public AIModelRepository(Context context, Handler handler, OpenAIClient client, Executor executor) {
        this.context = context.getApplicationContext();
        this.handler = handler;
        this.client = client;
        this.executor = executor;
    }

    public void fetchAIModelResponse(AIModelRequestVO requestVO, RequestCallback<AIModelResponseVO> callback) {
        executor.execute(new ThreadRequest(handler, client, requestVO, callback));
    }

    private class ThreadRequest implements Runnable {
        private final OpenAIClient client;
        private final Handler handler;
        private final RequestCallback<AIModelResponseVO> callback;
        private final AIModelRequestVO requestVO;

        ThreadRequest(Handler handler, OpenAIClient client, AIModelRequestVO requestVO, RequestCallback<AIModelResponseVO> callback) {
            this.handler = handler;
            this.client = client;
            this.callback = callback;
            this.requestVO = requestVO;
        }

        @Override
        public void run() {
            if (!NetworkConnectivity.isAvailable(context)) {
                handleError(callback);
                return;
            }

            ChatCompletionCreateParams requestParams = ChatCompletionCreateParams.builder()
                    .addUserMessage(requestVO.getMessage())
                    .model(ChatModel.GPT_4O_MINI)
                    .build();

            try {
                ChatCompletion chatCompletion = client.chat().completions().create(requestParams);

                Optional<String> modelRecommendationOptional = chatCompletion.choices().get(0).message().content();

                if (modelRecommendationOptional.isEmpty()) throw new Exception(AIModelConstants.BASIC_ERROR_AI_MODEL_MESSAGE);

                AIModelResponseVO aiModelResponseVO = new AIModelResponseVO.Builder().setMessage(modelRecommendationOptional.get()).build();

                handleSuccess(aiModelResponseVO, callback);
            } catch (Exception exception) {
                handleError(callback);
            }
        }

        private void handleSuccess(AIModelResponseVO aiModelResponseVO, RequestCallback<AIModelResponseVO> callback) {
            handler.post(() ->
                    callback.onSuccess(aiModelResponseVO));
        }

        private void handleError(RequestCallback<AIModelResponseVO> callback) {
            handler.post(() -> {
                callback.onFailure(AIModelConstants.BASIC_ERROR_AI_MODEL_MESSAGE);
            });
        }
    }
}
