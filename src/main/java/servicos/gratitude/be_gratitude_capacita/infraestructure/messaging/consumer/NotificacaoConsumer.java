package servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.dto.NotificacaoEmailDTO;

@Service
public class NotificacaoConsumer {
    private static final Logger logger = LoggerFactory.getLogger(NotificacaoConsumer.class);

    @Value("${spring.mail.username}")
    private String mailFrom;

    private final JavaMailSender mailSender;

    public NotificacaoConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notificacao}")
    public void receberNotificacao(NotificacaoEmailDTO notificacao) {
        try {
            logger.info("Processando notificação para: {} | Curso: {}", 
                notificacao.getEmailAluno(), notificacao.getTituloCurso());

            enviarEmail(notificacao);

            logger.info("Email enviado com sucesso para: {}", notificacao.getEmailAluno());
        } catch (Exception e) {
            logger.error("Erro ao processar notificação: {}", e.getMessage(), e);
            // Aqui você pode implementar retry ou dead-letter queue
        }
    }

    private void enviarEmail(NotificacaoEmailDTO notificacao) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(notificacao.getEmailAluno());
            message.setSubject("🎓 Novo Curso Lançado: " + notificacao.getTituloCurso());
            message.setText(construirCorpoEmail(notificacao));

            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", 
                notificacao.getEmailAluno(), e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar email", e);
        }
    }

    private String construirCorpoEmail(NotificacaoEmailDTO notificacao) {
        return String.format(
            "Olá %s,\n\n" +
            "Temos o prazer de informar que um novo curso foi lançado!\n\n" +
            "Título: %s\n" +
            "Descrição: %s\n\n" +
            "Acesse nossa plataforma para começar o curso agora mesmo!\n\n" +
            "Atenciosamente,\n" +
            "Equipe Gratitude Capacita",
            notificacao.getNomeAluno(),
            notificacao.getTituloCurso(),
            notificacao.getDescricaoCurso()
        );
    }
}
