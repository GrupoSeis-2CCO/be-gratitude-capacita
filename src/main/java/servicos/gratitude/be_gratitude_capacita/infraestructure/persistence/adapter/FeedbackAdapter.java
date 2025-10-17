package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.gateways.FeedbackGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.FeedbackMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.FeedbackRepository;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;

import java.util.List;

@Service
public class FeedbackAdapter implements FeedbackGateway {
    private final FeedbackRepository feedbackRepository;
    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(FeedbackAdapter.class);

    public FeedbackAdapter(FeedbackRepository feedbackRepository, JdbcTemplate jdbcTemplate) {
        this.feedbackRepository = feedbackRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Feedback save(Feedback feedback) {
        // Alguns bancos ainda não possuem a coluna 'anonimo'.
        // Para evitar 500 no INSERT via JPA, fazemos um caminho resiliente via JDBC
        // checando a existência da coluna e montando o INSERT dinamicamente.
        boolean anonimoExists = false;
        try {
            Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'feedback' AND COLUMN_NAME = 'anonimo'",
                Integer.class
            );
            anonimoExists = (cnt != null && cnt > 0);
        } catch (Exception e) {
            log.debug("Could not check for 'anonimo' column on save: {}", e.getMessage());
        }

        Integer fkCurso = feedback.getFkCurso();
        Integer fkUsuario = (feedback.getFkUsuario() != null ? feedback.getFkUsuario().getIdUsuario() : null);
        Integer estrelas = feedback.getEstrelas();
        String motivo = feedback.getMotivo();
        Boolean anonimo = feedback.getAnonimo();

        // Tenta JDBC primeiro para tolerar esquemas antigos
        try {
            if (anonimoExists) {
                jdbcTemplate.update(
                    "INSERT INTO feedback (fk_curso, fk_usuario, estrelas, motivo, anonimo) VALUES (?, ?, ?, ?, ?)",
                    fkCurso, fkUsuario, estrelas, motivo, anonimo
                );
            } else {
                jdbcTemplate.update(
                    "INSERT INTO feedback (fk_curso, fk_usuario, estrelas, motivo) VALUES (?, ?, ?, ?)",
                    fkCurso, fkUsuario, estrelas, motivo
                );
            }
            // Retorna o próprio domínio (já preenchido) após inserção bem-sucedida
            return feedback;
        } catch (DataAccessException ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();
            // Se for chave duplicada (um feedback por usuario/curso), faz UPSERT (UPDATE)
            if (msg.toLowerCase().contains("duplicate") || msg.toLowerCase().contains("primary")) {
                try {
                    if (anonimoExists) {
                        jdbcTemplate.update(
                            "UPDATE feedback SET estrelas = ?, motivo = ?, anonimo = ? WHERE fk_curso = ? AND fk_usuario = ?",
                            estrelas, motivo, anonimo, fkCurso, fkUsuario
                        );
                    } else {
                        jdbcTemplate.update(
                            "UPDATE feedback SET estrelas = ?, motivo = ? WHERE fk_curso = ? AND fk_usuario = ?",
                            estrelas, motivo, fkCurso, fkUsuario
                        );
                    }
                    return feedback;
                } catch (DataAccessException upEx) {
                    log.warn("FeedbackAdapter.save UPSERT (UPDATE) failed: {}", upEx.getMessage());
                    // Usa a mensagem do UPDATE para o mapeamento de erro abaixo
                    msg = upEx.getMessage() == null ? "" : upEx.getMessage();
                }
            }
            // Regra: se falhar por constraint em 'estrelas', sinalizar 400 (valor inválido)
            if (msg.contains("feedback.estrelas") || msg.toLowerCase().contains("check constraint") || msg.toLowerCase().contains("constraint")) {
                throw new ValorInvalidoException("Estrelas fora do intervalo permitido pelo esquema atual. Atualize o banco para aceitar 1..10 ou envie um valor permitido.");
            }
            // Em esquemas antigos sem 'anonimo', evitar fallback via JPA (pois fará SELECT na coluna ausente)
            if (!anonimoExists) {
                log.warn("FeedbackAdapter.save JDBC insert failed and schema is legacy (sem coluna 'anonimo'): {}", msg);
                throw new ValorInvalidoException("Esquema do banco desatualizado (coluna 'anonimo' ausente). Aplique a migration antes de enviar feedback.");
            }
            // Caso geral: tentar fallback via JPA apenas quando o esquema está alinhado
            log.warn("FeedbackAdapter.save JDBC insert failed, falling back to JPA: {}", msg);
            try {
                FeedbackEntity entity = FeedbackMapper.toEntity(feedback);
                return FeedbackMapper.toDomain(feedbackRepository.save(entity));
            } catch (Exception jpaEx) {
                log.warn("FeedbackAdapter.save JPA save also failed: {}", jpaEx.getMessage());
                throw jpaEx;
            }
        }
    }

    @Override
    public List<Feedback> findAllByCurso(Curso curso) {
        Integer idCurso = curso.getIdCurso();
        // Robust path: avoid JPA entity query to tolerate schema missing 'anonimo'
        boolean anonimoExists = false;
        try {
            Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'feedback' AND COLUMN_NAME = 'anonimo'",
                Integer.class
            );
            anonimoExists = (cnt != null && cnt > 0);
        } catch (Exception e) {
            log.debug("Could not check for 'anonimo' column: {}", e.getMessage());
        }

        final String baseCols = "fk_curso, fk_usuario, estrelas, motivo";
        final String cols = anonimoExists ? baseCols + ", anonimo" : baseCols;
        String sql = "SELECT " + cols + " FROM feedback WHERE fk_curso = ?";

        List<Feedback> rows;
        try {
            boolean readAnon = anonimoExists;
            rows = jdbcTemplate.query(sql, new Object[]{idCurso}, (rs, rowNum) -> {
                Feedback f = new Feedback();
                Integer fkCursoRow = rs.getObject("fk_curso") == null ? null : rs.getInt("fk_curso");
                Integer fkUsuarioRow = rs.getObject("fk_usuario") == null ? null : rs.getInt("fk_usuario");
                Integer estrelasRow = rs.getObject("estrelas") == null ? null : rs.getInt("estrelas");
                String motivoRow = rs.getString("motivo");
                Boolean anonimoRow = null;
                if (readAnon) {
                    try { anonimoRow = rs.getObject("anonimo") != null ? rs.getBoolean("anonimo") : null; } catch (Exception ignore) { /* tolerate */ }
                }

                f.setFkCurso(fkCursoRow);
                f.setEstrelas(estrelasRow);
                f.setMotivo(motivoRow);
                f.setAnonimo(anonimoRow);
                if (fkUsuarioRow != null) {
                    servicos.gratitude.be_gratitude_capacita.core.domain.Usuario u = new servicos.gratitude.be_gratitude_capacita.core.domain.Usuario();
                    u.setIdUsuario(fkUsuarioRow);
                    f.setFkUsuario(u);
                }
                servicos.gratitude.be_gratitude_capacita.core.domain.Curso c = new servicos.gratitude.be_gratitude_capacita.core.domain.Curso();
                c.setIdCurso(fkCursoRow);
                f.setCurso(c);
                return f;
            });
            log.info("FeedbackAdapter: query returned {} rows for curso {} (anonimo column present? {})", rows.size(), idCurso, anonimoExists);
        } catch (DataAccessException e) {
            log.warn("FeedbackAdapter: failed to query feedbacks: {}", e.getMessage());
            rows = List.of();
        }

        // inject curso object if missing (defensive)
        rows.forEach(d -> {
            if (d.getCurso() == null) {
                Curso c = new Curso();
                c.setIdCurso(idCurso);
                d.setCurso(c);
            }
        });
        return rows;
    }
}
