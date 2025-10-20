package cruds.notificacoes.consumidor;

import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.notificacoes.storage.NotificacaoMemoriaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerenciador de listeners de notificações por usuário
 * Cria e gerencia listeners individuais para cada fila de usuário
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoListener {

    private final NotificacaoMemoriaStorage notificacaoStorage;
    private final ConnectionFactory connectionFactory;
    private final Jackson2JsonMessageConverter messageConverter;

    /** Mapa de containers de listeners ativos por usuário */
    private final Map<UUID, SimpleMessageListenerContainer> listenersAtivos = new ConcurrentHashMap<>();

    /**
     * Cria um listener dedicado para receber notificações de um usuário específico
     * Se o listener já existir, não cria um novo
     *
     * @param userId ID do usuário para o qual criar o listener
     */
    public void criarListenerParaUsuario(UUID userId) {
        if (listenersAtivos.containsKey(userId)) {
            log.debug("Listener já existe para usuário {}", userId);
            return;
        }

        String nomeFila = gerarNomeFila(userId);

        try {
            SimpleMessageListenerContainer container = criarContainerMensagens(nomeFila, userId);

            container.afterPropertiesSet();
            container.start();

            listenersAtivos.put(userId, container);

            log.info("✅ Listener criado para usuário {} na fila {}", userId, nomeFila);

        } catch (Exception e) {
            log.error("❌ Erro ao criar listener para usuário {}: {}", userId, e.getMessage(), e);
        }
    }

    /**
     * Cria e configura o container de mensagens para um usuário
     */
    private SimpleMessageListenerContainer criarContainerMensagens(String nomeFila, UUID userId) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(nomeFila);
        container.setConcurrentConsumers(1);
        container.setPrefetchCount(10);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        container.setMessageListener((Message message) -> processarMensagem(message, userId));

        return container;
    }

    /**
     * Processa mensagem recebida e armazena a notificação
     */
    private void processarMensagem(Message message, UUID userId) {
        try {
            NotificacaoDTO notificacao = (NotificacaoDTO) messageConverter.fromMessage(message);

            // Define o userId da notificação baseado na fila
            notificacao.setUserId(userId);

            log.info("📥 Mensagem recebida para usuário {}: {}", userId, notificacao.getTitle());

            notificacaoStorage.adicionarNotificacao(notificacao);

        } catch (Exception e) {
            log.error("❌ Erro ao processar mensagem: {}", e.getMessage(), e);
        }
    }

    /**
     * Gera o nome da fila de um usuário específico
     */
    private String gerarNomeFila(UUID userId) {
        return "fila.usuario." + userId;
    }

    /**
     * Remove um listener de usuário (útil para desconectar usuário)
     */
    public void removerListenerUsuario(UUID userId) {
        SimpleMessageListenerContainer container = listenersAtivos.get(userId);

        if (container != null) {
            container.stop();
            listenersAtivos.remove(userId);
            log.info("🛑 Listener removido para usuário {}", userId);
        }
    }
}