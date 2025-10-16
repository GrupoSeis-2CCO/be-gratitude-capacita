package servicos.gratitude.be_gratitude_capacita.infraestructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
public class DataSourceChecker implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSourceChecker.class);
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DataSourceChecker(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection c = dataSource.getConnection()) {
            DatabaseMetaData md = c.getMetaData();
            log.info("Datasource productName={} url={} user={}", md.getDatabaseProductName(), md.getURL(), md.getUserName());
        } catch (Exception e) {
            log.warn("Não foi possível obter info do DataSource: {}", e.getMessage());
        }

        try {
            Integer countFeedback = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM feedback", Integer.class);
            log.info("DB count feedback={}", countFeedback);
        } catch (Exception e) {
            log.warn("Erro ao consultar contagem de feedback: {}", e.getMessage());
        }

        try {
            // Removido: feedback_entity table check (causava erro caso a tabela não exista)
        } catch (Exception e) {
            // Removido: feedback_entity table check (causava erro caso a tabela não exista)
        }
    }
}
