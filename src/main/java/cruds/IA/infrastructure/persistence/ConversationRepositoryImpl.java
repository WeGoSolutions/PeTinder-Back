package cruds.IA.infrastructure.persistence;

import cruds.IA.core.domain.ConversationMessage;
import cruds.IA.core.domain.gateway.ConversationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConversationRepositoryImpl implements ConversationRepository {

    private final ConversationJpaRepository repository;

    public ConversationRepositoryImpl(ConversationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ConversationMessage message) {

        ConversationJpaEntity entity = new ConversationJpaEntity();

        entity.setConversationId(message.getConversationId());
        entity.setRole(message.getRole());
        entity.setMessage(message.getMessage());
        entity.setCreatedAt(message.getCreatedAt());

        repository.save(entity);
    }

    @Override
    public List<ConversationMessage> findByConversationId(String conversationId) {

        return repository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(e -> new ConversationMessage(
                        e.getConversationId(),
                        e.getRole(),
                        e.getMessage()
                ))
                .toList();
    }

    @Override
    public void deleteOlderThan(LocalDateTime date) {
        repository.deleteByCreatedAtBefore(date);
    }
}