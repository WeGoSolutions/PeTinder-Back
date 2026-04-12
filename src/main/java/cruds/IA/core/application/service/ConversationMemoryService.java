package cruds.IA.core.application.service;

import cruds.IA.core.domain.ChatContent;
import cruds.IA.core.domain.ChatMessage;
import cruds.IA.core.domain.gateway.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversationMemoryService {

    private final Map<String, List<ChatMessage>> conversations = new HashMap<>();
    private final ConversationRepository repository;

    public ConversationMemoryService(ConversationRepository repository) {
        this.repository = repository;
    }

    public List<ChatMessage> getHistory(String conversationId) {

        return repository.findByConversationId(conversationId)
                .stream()
                .map(msg -> new ChatMessage(
                        msg.getRole(),
                        List.of(new ChatContent("text", msg.getMessage()))
                ))
                .toList();
    }

    public void addMessage(String conversationId, ChatMessage message) {

        List<ChatMessage> history =
                conversations.getOrDefault(conversationId, new ArrayList<>());

        history.add(message);

        conversations.put(conversationId, history);
    }
}
