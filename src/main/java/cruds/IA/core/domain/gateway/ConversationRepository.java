package cruds.IA.core.domain.gateway;

import cruds.IA.core.domain.ConversationMessage;
import java.time.LocalDateTime;
import java.util.List;

public interface ConversationRepository {

    void save(ConversationMessage message);

    List<ConversationMessage> findByConversationId(String conversationId);

    void deleteOlderThan(LocalDateTime date);
}