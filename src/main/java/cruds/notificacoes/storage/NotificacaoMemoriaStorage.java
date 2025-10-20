package cruds.notificacoes.storage;

import cruds.notificacoes.dto.NotificacaoDTO;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * Armazenamento em memória para notificações
 * Mantém as notificações recebidas temporariamente até serem consumidas
 *
 * NOTA: Este storage é volátil e será perdido ao reiniciar a aplicação
 * Para persistência, considere implementar storage com banco de dados
 */
@Component
public class NotificacaoMemoriaStorage {

    /** Deque thread-safe para armazenar notificações */
    private final Deque<NotificacaoDTO> notificacoes = new ConcurrentLinkedDeque<>();

    /**
     * Adiciona uma nova notificação ao storage
     *
     * @param notificacao Notificação a ser armazenada
     */
    public void adicionarNotificacao(NotificacaoDTO notificacao) {
        notificacoes.push(notificacao);
    }

    /**
     * Lista todas as notificações NÃO VISUALIZADAS de um usuário específico
     *
     * @param userId ID do usuário
     * @return Lista de notificações não visualizadas
     */
    public List<NotificacaoDTO> listarNotificacoesNaoLidas(UUID userId) {
        return notificacoes.stream()
                .filter(n -> n.getUserId() != null && n.getUserId().equals(userId))
                .filter(n -> !n.isViewed())
                .collect(Collectors.toList());
    }

    /**
     * Remove permanentemente uma notificação específica
     *
     * @param userId ID do usuário dono da notificação
     * @param notifyId ID da notificação a ser removida
     * @return true se a notificação foi removida, false caso contrário
     */
    public boolean removerNotificacao(UUID userId, UUID notifyId) {
        return notificacoes.removeIf(n ->
                n.getUserId() != null &&
                        n.getUserId().equals(userId) &&
                        n.getNotifyId().equals(notifyId)
        );
    }

    /**
     * Marca uma notificação como visualizada
     *
     * @param userId ID do usuário
     * @param notifyId ID da notificação
     * @return true se marcada com sucesso
     */
    public boolean marcarComoLida(UUID userId, UUID notifyId) {
        return notificacoes.stream()
                .filter(n -> n.getUserId() != null && n.getUserId().equals(userId))
                .filter(n -> n.getNotifyId().equals(notifyId))
                .peek(n -> n.setViewed(true))
                .findFirst()
                .isPresent();
    }

    /**
     * Conta quantas notificações não visualizadas um usuário possui
     *
     * @param userId ID do usuário
     * @return Quantidade de notificações não visualizadas
     */
    public long contarNotificacoesNaoLidas(UUID userId) {
        return notificacoes.stream()
                .filter(n -> n.getUserId() != null && n.getUserId().equals(userId))
                .filter(n -> !n.isViewed())
                .count();
    }

    /**
     * Lista TODAS as notificações de um usuário (incluindo visualizadas)
     *
     * @param userId ID do usuário
     * @return Lista completa de notificações
     */
    public List<NotificacaoDTO> listarTodasNotificacoes(UUID userId) {
        return notificacoes.stream()
                .filter(n -> n.getUserId() != null && n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}