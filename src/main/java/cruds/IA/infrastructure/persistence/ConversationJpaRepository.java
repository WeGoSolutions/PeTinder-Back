package cruds.IA.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConversationJpaRepository
        extends JpaRepository<ConversationJpaEntity, Long> {

    List<ConversationJpaEntity> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    void deleteByCreatedAtBefore(LocalDateTime date);
}
