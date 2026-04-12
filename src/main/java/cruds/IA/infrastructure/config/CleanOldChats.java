package cruds.IA.infrastructure.config;

import cruds.IA.core.domain.gateway.ConversationRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class CleanOldChats {

    private final ConversationRepository repository;

    public CleanOldChats(ConversationRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void clean() {

        repository.deleteOlderThan(
                LocalDateTime.now().minusDays(15)
        );
    }
}