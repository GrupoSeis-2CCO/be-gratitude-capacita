package servicos.gratitude.be_gratitude_capacita.infraestructure.config;

// Usamos a classe com.rabbitmq.client.ConnectionFactory diretamente no código
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConnectionConfig {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQConnectionConfig.class);

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.username:guest}")
    private String username;

    @Value("${spring.rabbitmq.password:guest}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host:/}")
    private String virtualHost;

    @Value("${spring.rabbitmq.ssl.enabled:false}")
    private boolean sslEnabled;

    @Bean
    @Primary
    public ConnectionFactory rabbitConnectionFactory() {
        try {
            com.rabbitmq.client.ConnectionFactory client = new com.rabbitmq.client.ConnectionFactory();
            client.setHost(host);
            client.setPort(port);
            client.setUsername(username);
            client.setPassword(password);
            client.setVirtualHost(virtualHost);

            if (sslEnabled) {
                try {
                    client.useSslProtocol();
                    LOG.info("RabbitMQ SSL/TLS habilitado para host={} port={}", host, port);
                } catch (Exception e) {
                    LOG.warn("Falha ao habilitar SSL para RabbitMQ: {}", e.getMessage());
                }
            }

            CachingConnectionFactory factory = new CachingConnectionFactory(client);
            // configurações adicionais podem ser colocadas aqui (ex.: requested heartbeat)
            return factory;
        } catch (Exception e) {
            LOG.error("Erro ao criar ConnectionFactory do RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
