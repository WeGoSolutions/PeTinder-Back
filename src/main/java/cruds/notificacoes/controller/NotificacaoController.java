package cruds.notificacoes.controller;

import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.notificacoes.service.NotificacaoFilaService;
import cruds.notificacoes.storage.NotificacaoMemoriaStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller REST para gerenciamento de notificações de usuários
 * Endpoints para listar, buscar e deletar notificações
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notificações", description = "Endpoints para gerenciamento de notificações")
public class NotificacaoController {

    private final NotificacaoFilaService notificacaoFilaService;
    private final NotificacaoMemoriaStorage notificacaoStorage;

    /**
     * Lista todas as notificações não visualizadas de um usuário
     *
     * GET /api/notifications/{userId}
     *
     * @param userId ID do usuário
     * @return Lista de notificações não visualizadas
     */
    @Operation(summary = "Lista todas as notificações não visualizadas de um usuário")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificacaoDTO>> buscarNotificacoes(@PathVariable UUID userId) {
        log.info("📋 Buscando notificações para usuário {}", userId);

        List<NotificacaoDTO> notificacoes = notificacaoStorage.listarNotificacoesNaoLidas(userId);

        return ResponseEntity.ok(notificacoes);
    }

    /**
     * Busca a próxima notificação disponível na fila (modo pull)
     *
     * GET /api/notifications/{userId}/next
     *
     * @param userId ID do usuário
     * @return Próxima notificação ou 204 No Content se não houver
     */
    @Operation(summary = "Busca a próxima notificação da fila")
    @GetMapping("/{userId}/next")
    public ResponseEntity<NotificacaoDTO> buscarProximaNotificacao(@PathVariable UUID userId) {
        log.info("📬 Buscando próxima notificação para usuário {}", userId);

        NotificacaoDTO notificacao = notificacaoFilaService.buscarProximaNotificacao(userId);

        if (notificacao == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(notificacao);
    }

    /**
     * Conta quantas notificações não visualizadas um usuário possui
     *
     * GET /api/notifications/{userId}/count
     *
     * @param userId ID do usuário
     * @return Objeto com a contagem de notificações
     */
    @Operation(summary = "Conta notificações não visualizadas de um usuário")
    @GetMapping("/{userId}/count")
    public ResponseEntity<Map<String, Long>> contarNotificacoes(@PathVariable UUID userId) {
        log.info("🔢 Contando notificações para usuário {}", userId);

        long count = notificacaoStorage.contarNotificacoesNaoLidas(userId);

        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Deleta uma notificação específica
     *
     * DELETE /api/notifications/{userId}/{notifyId}
     *
     * @param userId ID do usuário
     * @param notifyId ID da notificação
     * @return 204 No Content se deletada, 404 Not Found se não encontrada
     */
    @Operation(summary = "Deleta uma notificação específica")
    @DeleteMapping("/{userId}/{notifyId}")
    public ResponseEntity<Void> deletarNotificacao(
            @PathVariable UUID userId,
            @PathVariable UUID notifyId) {

        log.info("🗑️ Deletando notificação {} do usuário {}", notifyId, userId);

        boolean sucesso = notificacaoFilaService.deletarNotificacao(userId, notifyId);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Marca uma notificação como lida
     *
     * PATCH /api/notifications/{userId}/{notifyId}/read
     *
     * @param userId ID do usuário
     * @param notifyId ID da notificação
     * @return 200 OK se marcada, 404 Not Found se não encontrada
     */
    @Operation(summary = "Marca uma notificação como lida")
    @PatchMapping("/{userId}/{notifyId}/read")
    public ResponseEntity<Void> marcarComoLida(
            @PathVariable UUID userId,
            @PathVariable UUID notifyId) {

        log.info("👁️ Marcando notificação {} como lida para usuário {}", notifyId, userId);

        boolean sucesso = notificacaoFilaService.marcarComoLida(userId, notifyId);

        if (sucesso) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Limpa todas as mensagens da fila de um usuário
     *
     * DELETE /api/notifications/{userId}/queue
     *
     * @param userId ID do usuário
     * @return 204 No Content
     */
    @Operation(summary = "Limpa toda a fila de notificações de um usuário")
    @DeleteMapping("/{userId}/queue")
    public ResponseEntity<Void> limparFilaUsuario(@PathVariable UUID userId) {
        log.info("🧹 Limpando fila de notificações do usuário {}", userId);

        notificacaoFilaService.limparFilaUsuario(userId);

        return ResponseEntity.noContent().build();
    }
}