package cruds.notificacoes.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para o sistema de notificações
 * Define os beans necessários para envio e recebimento de mensagens
 */
@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitMQConfig {

    /**
     * Conversor de mensagens JSON para objetos Java e vice-versa
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template para envio de mensagens ao RabbitMQ
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    /**
     * Fila geral de notificações (pode ser usada para broadcast geral)
     */
    @Bean
    public Queue filaNotificacoesGeral() {
        return new Queue("fila.notificacoes.geral", true);
    }
}