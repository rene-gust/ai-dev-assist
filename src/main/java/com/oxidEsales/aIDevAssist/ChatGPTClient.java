package com.oxidEsales.aIDevAssist;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ChatGPTClient {
    public String query(String query) {
        String apiKey = Objects.requireNonNull(AppSettingsState.getInstance().getState()).apiKey;
        OpenAiService service = new OpenAiService(apiKey);
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful oxid eshop developer assistant.");
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), query);
        messages.add(systemMessage);
        messages.add(userMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-4o")
                .messages(messages)
                .n(1)
                .maxTokens(4000)
                .logitBias(new HashMap<>())
                .build();

        StringBuilder outputStringBuilder = new StringBuilder();
        service.createChatCompletion(chatCompletionRequest).getChoices().forEach(
                choice -> outputStringBuilder.append(choice.getMessage().getContent()).append("\n")
        );

        return outputStringBuilder.toString();
    }
}
