package servicos.gratitude.be_gratitude_capacita.infraestructure.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.notificacao}")
    private String filaNotificacao;

    @Value("${rabbitmq.exchange.notificacao}")
    private String exchangeNotificacao;

    @Value("${rabbitmq.routing.key.notificacao}")
    private String routingKeyNotificacao;

    // Declarar fila
    @Bean
    @ConditionalOnProperty(name = "rabbitmq.declare-resources", havingValue = "true", matchIfMissing = true)
    public Queue filaNotificacao() {
        return new Queue(filaNotificacao, true, false, false);
    }

    // Declarar exchange
    @Bean
    @ConditionalOnProperty(name = "rabbitmq.declare-resources", havingValue = "true", matchIfMissing = true)
    public DirectExchange exchangeNotificacao() {
        return new DirectExchange(exchangeNotificacao, true, false);
    }

    // Binding entre fila e exchange
    @Bean
    @ConditionalOnProperty(name = "rabbitmq.declare-resources", havingValue = "true", matchIfMissing = true)
    public Binding bindingNotificacao(Queue filaNotificacao, DirectExchange exchangeNotificacao) {
        return BindingBuilder.bind(filaNotificacao)
                .to(exchangeNotificacao)
                .with(routingKeyNotificacao);
    }

    // Converter para JSON
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Template do RabbitMQ
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
