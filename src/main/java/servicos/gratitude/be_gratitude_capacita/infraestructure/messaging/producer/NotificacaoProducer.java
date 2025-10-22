package servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.dto.NotificacaoEmailDTO;

@Service
public class NotificacaoProducer {
    private static final Logger logger = LoggerFactory.getLogger(NotificacaoProducer.class);

    @Value("${rabbitmq.exchange.notificacao}")
    private String exchange;

    @Value("${rabbitmq.routing.key.notificacao}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public NotificacaoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarNotificacao(NotificacaoEmailDTO notificacao) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, notificacao);
            logger.info("Notificação enviada para fila: {} | Aluno: {} | Curso: {}", 
                routingKey, notificacao.getNomeAluno(), notificacao.getTituloCurso());
        } catch (Exception e) {
            logger.error("Erro ao enviar notificação para fila: {}", e.getMessage(), e);
        }
    }
}
