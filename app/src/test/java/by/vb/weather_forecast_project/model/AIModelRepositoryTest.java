package by.vb.weather_forecast_project.model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Handler;

import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.services.blocking.ChatService;
import com.openai.services.blocking.chat.ChatCompletionService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import by.vb.weather_forecast_project.constant.AIModelConstants;
import by.vb.weather_forecast_project.util.NetworkConnectivity;
import by.vb.weather_forecast_project.vo.AIModelRequestVO;
import by.vb.weather_forecast_project.vo.AIModelResponseVO;

@RunWith(MockitoJUnitRunner.class)
public class AIModelRepositoryTest {
    @Mock
    private Context mockContext;
    @Mock
    private Handler mockHandler;
    @Mock
    private OpenAIClient mockClient;
    MockedStatic<NetworkConnectivity> mockNetworkUtil = mockStatic(NetworkConnectivity.class);
    @Mock
    private RequestCallback<AIModelResponseVO> mockCallback;

    private AIModelRepository repository;

    @Before
    public void setUp() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(mockHandler).post(any(Runnable.class));

        repository = new AIModelRepository(mockContext, mockHandler,
                mockClient, Runnable::run);
    }

    @After
    public void tearDown() {
        mockNetworkUtil.close();
    }

    @Test
    public void fetchAIModelResponseMethodCall_whenNetworkConnectivityFails_thenShouldTriggerErrorHandler() {
        mockNetworkUtil.when(() -> NetworkConnectivity.isAvailable(any())).thenReturn(false);

        repository.fetchAIModelResponse(new AIModelRequestVO.Builder().setMessage("Right message to AI Model").build(), mockCallback);

        verify(mockHandler).post(any(Runnable.class));
        verify(mockClient, never()).chat();
        verify(mockCallback).onFailure(argThat(message ->
                message != null && message.equals(AIModelConstants.BASIC_ERROR_AI_MODEL_MESSAGE)
        ));
    }

    @Test
    public void fetchAIModelResponseCall_whenNetworkConnectivityWorksAndApiReturnsGoodResponse_thenShouldTriggerSuccessHandler() {
        String responseFromAIModel = "Response from AI Model";

        mockNetworkUtil.when(() -> NetworkConnectivity.isAvailable(any())).thenReturn(true);

        ChatCompletion mockChatCompletion = mock(ChatCompletion.class);
        List<ChatCompletion.Choice> mockChoices = mock(List.class);
        ChatCompletion.Choice mockChoice = mock(ChatCompletion.Choice.class);
        ChatCompletionMessage mockMessage = mock(ChatCompletionMessage.class);

        ChatService mockChatService = mock(ChatService.class);
        ChatCompletionService mockCompletionService = mock(ChatCompletionService.class);

        when(mockClient.chat()).thenReturn(mockChatService);
        when(mockChatService.completions()).thenReturn(mockCompletionService);
        when(mockCompletionService.create(any(ChatCompletionCreateParams.class))).thenReturn(mockChatCompletion);

        when(mockChatCompletion.choices()).thenReturn(mockChoices);
        when(mockChoices.get(0)).thenReturn(mockChoice);
        when(mockChoice.message()).thenReturn(mockMessage);
        when(mockMessage.content()).thenReturn(Optional.of(responseFromAIModel));

        repository.fetchAIModelResponse(
                new AIModelRequestVO.Builder().setMessage("Right message to AI Model").build(),
                mockCallback);

        verify(mockHandler).post(any(Runnable.class));
        verify(mockClient, atLeastOnce()).chat();
        verify(mockCallback).onSuccess(argThat(response ->
                response != null && responseFromAIModel.equals(response.getMessage())
        ));
    }

    @Test
    public void fetchAIModelResponseCall_whenNetworkConnectivityWorksAndApiReturnsBadResponse_thenShouldTriggerErrorHandler() {
        String responseFromAIModel = null;

        mockNetworkUtil.when(() -> NetworkConnectivity.isAvailable(any())).thenReturn(true);

        ChatCompletion mockChatCompletion = mock(ChatCompletion.class);
        List<ChatCompletion.Choice> mockChoices = mock(List.class);
        ChatCompletion.Choice mockChoice = mock(ChatCompletion.Choice.class);
        ChatCompletionMessage mockMessage = mock(ChatCompletionMessage.class);

        ChatService mockChatService = mock(ChatService.class);
        ChatCompletionService mockCompletionService = mock(ChatCompletionService.class);

        when(mockClient.chat()).thenReturn(mockChatService);
        when(mockChatService.completions()).thenReturn(mockCompletionService);
        when(mockCompletionService.create(any(ChatCompletionCreateParams.class))).thenReturn(mockChatCompletion);

        when(mockChatCompletion.choices()).thenReturn(mockChoices);
        when(mockChoices.get(0)).thenReturn(mockChoice);
        when(mockChoice.message()).thenReturn(mockMessage);
        when(mockMessage.content()).thenReturn(Optional.ofNullable(responseFromAIModel));

        repository.fetchAIModelResponse(
                new AIModelRequestVO.Builder().setMessage("Right message to AI Model").build(),
                mockCallback);

        verify(mockHandler).post(any(Runnable.class));
        verify(mockClient, atLeastOnce()).chat();
        verify(mockCallback).onFailure(argThat(response ->
                response != null && response.equals(AIModelConstants.BASIC_ERROR_AI_MODEL_MESSAGE)
        ));
    }
}