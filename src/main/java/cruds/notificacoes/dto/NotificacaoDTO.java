package cruds.notificacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO representando uma notificação do sistema
 * Utilizado para comunicação entre RabbitMQ e a aplicação
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificacaoDTO {

    /** ID único da notificação */
    private UUID notifyId;

    /** ID do usuário destinatário */
    private UUID userId;

    /** Tipo da notificação (ADOPTED, NOTADOPTED, etc.) */
    private String notifyType;

    /** Título da notificação */
    private String title;

    /** Descrição detalhada da notificação */
    private String description;

    /** Indica se a notificação foi visualizada */
    private boolean viewed;

    /** Data e hora de criação da notificação */
    private LocalDateTime createdAt;

    /**
     * Construtor auxiliar para criar notificações de forma simplificada
     *
     * @param userId ID do usuário destinatário
     * @param notifyType Tipo da notificação
     * @param title Título da notificação
     * @param description Descrição da notificação
     */
    public NotificacaoDTO(UUID userId, String notifyType, String title, String description) {
        this.notifyId = UUID.randomUUID();
        this.userId = userId;
        this.notifyType = notifyType;
        this.title = title;
        this.description = description;
        this.viewed = false;
        this.createdAt = LocalDateTime.now();
    }
}