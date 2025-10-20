package cruds.notificacoes.service;

import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.notificacoes.storage.NotificacaoMemoriaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Serviço para interação direta com filas do RabbitMQ
 * Permite consumir mensagens diretamente das filas (modo pull)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoFilaService {

    private final RabbitTemplate rabbitTemplate;
    private final NotificacaoMemoriaStorage notificacaoStorage;

    /**
     * Consome a próxima notificação disponível na fila do usuário
     *
     * @param userId ID do usuário
     * @return NotificacaoDTO ou null se não houver notificações
     */
    public NotificacaoDTO buscarProximaNotificacao(UUID userId) {
        String nomeFila = gerarNomeFila(userId);

        try {
            Message message = rabbitTemplate.receive(nomeFila, 1000);

            if (message != null) {
                NotificacaoDTO notificacao = (NotificacaoDTO) rabbitTemplate
                        .getMessageConverter()
                        .fromMessage(message);

                log.info("📥 Notificação consumida da fila: {}", notificacao.getTitle());
                return notificacao;
            }

        } catch (Exception e) {
            log.error("❌ Erro ao consumir notificação: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * Deleta uma notificação específica do storage
     *
     * @param userId ID do usuário
     * @param notifyId ID da notificação
     * @return true se deletada com sucesso
     */
    public boolean deletarNotificacao(UUID userId, UUID notifyId) {
        try {
            boolean removida = notificacaoStorage.removerNotificacao(userId, notifyId);

            if (removida) {
                log.info("🗑️ Notificação {} deletada para usuário {}", notifyId, userId);
                return true;
            }

            log.warn("⚠️ Notificação {} não encontrada para usuário {}", notifyId, userId);
            return false;

        } catch (Exception e) {
            log.error("❌ Erro ao deletar notificação: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Marca uma notificação como visualizada
     *
     * @param userId ID do usuário
     * @param notifyId ID da notificação
     * @return true se marcada com sucesso
     */
    public boolean marcarComoLida(UUID userId, UUID notifyId) {
        try {
            boolean marcada = notificacaoStorage.marcarComoLida(userId, notifyId);

            if (marcada) {
                log.info("👁️ Notificação {} marcada como lida para usuário {}", notifyId, userId);
                return true;
            }

            log.warn("⚠️ Notificação {} não encontrada para usuário {}", notifyId, userId);
            return false;

        } catch (Exception e) {
            log.error("❌ Erro ao marcar notificação como lida: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Limpa todas as mensagens da fila de um usuário
     *
     * @param userId ID do usuário
     */
    public void limparFilaUsuario(UUID userId) {
        String nomeFila = gerarNomeFila(userId);

        try {
            rabbitTemplate.execute(channel -> {
                channel.queuePurge(nomeFila);
                return null;
            });

            log.info("🗑️ Fila {} limpa com sucesso", nomeFila);

        } catch (Exception e) {
            log.error("❌ Erro ao limpar fila: {}", e.getMessage(), e);
        }
    }

    /**
     * Conta quantas mensagens existem na fila (sem consumir)
     *
     * @param userId ID do usuário
     * @return Quantidade de mensagens na fila
     */
    public long contarMensagensNaFila(UUID userId) {
        String nomeFila = gerarNomeFila(userId);

        try {
            return rabbitTemplate.execute(channel -> {
                var declareOk = channel.queueDeclarePassive(nomeFila);
                return (long) declareOk.getMessageCount();
            });

        } catch (Exception e) {
            log.error("❌ Erro ao contar mensagens na fila: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Gera o nome da fila de um usuário
     */
    private String gerarNomeFila(UUID userId) {
        return "fila.usuario." + userId;
    }
}