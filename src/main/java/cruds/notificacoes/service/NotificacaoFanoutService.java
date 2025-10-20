package cruds.notificacoes.service;

import cruds.notificacoes.consumidor.NotificacaoListener;
import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.Pets.service.PetStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço responsável por gerenciar notificações via Fanout Exchange
 * Permite enviar notificações em broadcast para múltiplos usuários interessados em um pet
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoFanoutService {

    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final PetStatusService petStatusService;
    private final NotificacaoListener notificacaoListener;

    /** Cache de exchanges já criados */
    private final Set<String> exchangesCriados = ConcurrentHashMap.newKeySet();

    /** Cache de bindings já criados (formato: "nomeFila:nomeExchange") */
    private final Set<String> bindingsCriados = ConcurrentHashMap.newKeySet();

    /**
     * Inscreve um usuário para receber notificações sobre um pet específico
     * Cria fila, exchange e binding necessários
     *
     * @param petId ID do pet
     * @param userId ID do usuário interessado
     */
    public void inscreverUsuarioNoPet(UUID petId, UUID userId) {
        validarParametros(petId, userId);

        String nomeFila = gerarNomeFila(userId);
        String nomeExchange = gerarNomeExchange(petId);
        String chaveBinding = gerarChaveBinding(nomeFila, nomeExchange);

        try {
            criarExchangeSeNaoExistir(nomeExchange);
            criarFilaSeNaoExistir(nomeFila);
            criarBindingSeNaoExistir(nomeFila, nomeExchange, chaveBinding);

            notificacaoListener.criarListenerParaUsuario(userId);

            log.info("✅ Usuário {} inscrito nas notificações do pet {}", userId, petId);

        } catch (Exception e) {
            log.error("❌ Erro ao inscrever usuário {} no pet {}: {}",
                    userId, petId, e.getMessage(), e);
            throw new RuntimeException("Erro ao configurar notificações do usuário", e);
        }
    }

    /**
     * Notifica APENAS o usuário que foi selecionado para adotar o pet
     * Remove o binding para evitar que ele receba a notificação de "pet adotado"
     *
     * @param petId ID do pet adotado
     * @param userId ID do usuário que adotou
     */
    public void notificarUsuarioSelecionado(UUID petId, UUID userId) {
        String nomePet = petStatusService.getPetNomeById(petId);
        String nomeFila = gerarNomeFila(userId);

        NotificacaoDTO notificacao = new NotificacaoDTO(
                userId,
                "ADOPTED",
                "Parabéns!",
                "Adoção confirmada! " + nomePet + " ganhou um novo lar."
        );

        rabbitTemplate.convertAndSend(nomeFila, notificacao);
        log.info("📨 Notificação de ADOÇÃO enviada para usuário {}", userId);

        // Remove o binding para evitar receber notificação dos demais interessados
        desconectarUsuarioDoPet(petId, userId);
    }

    /**
     * Notifica TODOS os demais interessados via FANOUT que o pet já foi adotado
     *
     * @param petId ID do pet que foi adotado
     */
    public void notificarDemaisInteressados(UUID petId) {
        String nomePet = petStatusService.getPetNomeById(petId);
        String nomeExchange = gerarNomeExchange(petId);

        NotificacaoDTO notificacao = new NotificacaoDTO(
                UUID.randomUUID(), // ID temporário, será substituído pelo listener
                "NOTADOPTED",
                "Ah, que pena!",
                "O pet " + nomePet + " já foi adotado por outra pessoa. Continue procurando, o seu amigo perfeito está te esperando!"
        );

        // Envia para o exchange fanout - vai para TODAS as filas conectadas
        rabbitTemplate.convertAndSend(nomeExchange, "", notificacao);

        log.info("📢 Notificação de PET ADOTADO enviada via fanout para interessados no pet {}", petId);
    }

    /**
     * Desinscreve um usuário das notificações de um pet específico
     * Remove o binding entre a fila do usuário e o exchange do pet
     *
     * @param petId ID do pet
     * @param userId ID do usuário
     */
    public void desconectarUsuarioDoPet(UUID petId, UUID userId) {
        String nomeFila = gerarNomeFila(userId);
        String nomeExchange = gerarNomeExchange(petId);
        String chaveBinding = gerarChaveBinding(nomeFila, nomeExchange);

        try {
            Queue fila = new Queue(nomeFila);
            FanoutExchange exchange = new FanoutExchange(nomeExchange);
            Binding binding = BindingBuilder.bind(fila).to(exchange);

            amqpAdmin.removeBinding(binding);
            bindingsCriados.remove(chaveBinding);

            log.info("🔓 Usuário {} desconectado do exchange do pet {}", userId, petId);

        } catch (Exception e) {
            log.warn("⚠️ Erro ao remover binding para usuário {} e pet {}: {}",
                    userId, petId, e.getMessage());
        }
    }

    // ==================== Métodos Privados ====================

    private void criarExchangeSeNaoExistir(String nomeExchange) {
        if (!exchangesCriados.contains(nomeExchange)) {
            FanoutExchange exchange = new FanoutExchange(nomeExchange, true, false);
            amqpAdmin.declareExchange(exchange);
            exchangesCriados.add(nomeExchange);
            log.debug("📡 Exchange criado: {}", nomeExchange);
        }
    }

    private void criarFilaSeNaoExistir(String nomeFila) {
        Queue fila = new Queue(nomeFila, true, false, false);
        amqpAdmin.declareQueue(fila);
        log.debug("📬 Fila criada/verificada: {}", nomeFila);
    }

    private void criarBindingSeNaoExistir(String nomeFila, String nomeExchange, String chaveBinding) {
        if (!bindingsCriados.contains(chaveBinding)) {
            Queue fila = new Queue(nomeFila);
            FanoutExchange exchange = new FanoutExchange(nomeExchange);
            Binding binding = BindingBuilder.bind(fila).to(exchange);
            amqpAdmin.declareBinding(binding);
            bindingsCriados.add(chaveBinding);
            log.debug("🔗 Binding criado: {} -> {}", nomeFila, nomeExchange);
        }
    }

    private void validarParametros(UUID petId, UUID userId) {
        if (petId == null || userId == null) {
            throw new IllegalArgumentException("PetId e UserId não podem ser nulos");
        }
    }

    private String gerarNomeFila(UUID userId) {
        return "fila.usuario." + userId;
    }

    private String gerarNomeExchange(UUID petId) {
        return "fanoutExchange.pet." + petId;
    }

    private String gerarChaveBinding(String nomeFila, String nomeExchange) {
        return nomeFila + ":" + nomeExchange;
    }
}