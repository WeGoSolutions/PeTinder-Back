package cruds.IA.core.application.usecase;

import cruds.IA.core.application.service.ConversationMemoryService;
import cruds.IA.core.domain.ChatContent;
import cruds.IA.core.domain.ChatMessage;
import cruds.IA.core.domain.gateway.AiChatGateway;

import java.util.ArrayList;
import java.util.List;

public class ChatWithAiUseCase {

    private final AiChatGateway aiChatGateway;
    private final ConversationMemoryService memoryService;

    public ChatWithAiUseCase(
            ConversationMemoryService memoryService,
            AiChatGateway aiChatGateway) {

        this.memoryService = memoryService;
        this.aiChatGateway = aiChatGateway;
    }

    public String chat(String conversationId, String message) {

        List<ChatMessage> history = new ArrayList<>(
                memoryService.getHistory(conversationId)
        );

        ChatMessage userMessage = new ChatMessage(
                "user",
                List.of(new ChatContent("text", message))
        );

        history.add(userMessage);

        String response = aiChatGateway.chat(history);
        memoryService.addMessage(conversationId, userMessage);
        ChatMessage assistantMessage = new ChatMessage(
                "assistant",
                List.of(new ChatContent("text", response))
        );
        memoryService.addMessage(conversationId, assistantMessage);

        return response;
    }
}