package cruds.IA.core.domain;

import java.time.LocalDateTime;

public class ConversationMessage {

    private String conversationId;
    private String role;
    private String message;
    private LocalDateTime createdAt;

    public ConversationMessage(String conversationId, String role, String message) {
        this.conversationId = conversationId;
        this.role = role;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public String getConversationId() { return conversationId; }
    public String getRole() { return role; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
