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
        FeedbackEntity entity = FeedbackMapper.toEntity(feedback);

        return FeedbackMapper.toDomain(feedbackRepository.save(entity));
    }

    @Override
    public List<Feedback> findAllByCurso(Curso curso) {
        Integer idCurso = curso.getIdCurso();
        List<FeedbackEntity> entities = feedbackRepository.findAllByFkCurso(idCurso);
        if (entities == null) {
            log.info("FeedbackAdapter: repository returned null for curso {}", idCurso);
        } else {
            log.info("FeedbackAdapter: repository returned {} entities for curso {}", entities.size(), idCurso);
            for (FeedbackEntity e : entities) {
                try {
                    log.debug("RAW feedback row -> fkCurso={}, fkUsuario={}, estrelas={}, motivo={}", e.getFkCurso(), e.getFkUsuario() == null ? null : e.getFkUsuario().getIdUsuario(), e.getEstrelas(), e.getMotivo());
                } catch (Exception ex) {
                    log.debug("RAW feedback row logging failed: {}", ex.getMessage());
                }
            }
        }

        List<Feedback> domains = FeedbackMapper.toDomains(entities == null ? List.of() : entities);
        // if JPA returned empty but DB has rows, try native fallback
        if ((entities == null || entities.isEmpty())) {
            try {
                List<Object[]> rows = feedbackRepository.findAllByFkCursoNative(idCurso);
                if (rows != null && !rows.isEmpty()) {
                    log.info("FeedbackAdapter: native query returned {} rows for curso {}", rows.size(), idCurso);
                    // map rows -> Feedback
                    List<Feedback> nativeMapped = new java.util.ArrayList<>();
                    for (Object[] r : rows) {
                        Feedback f = new Feedback();
                        // r: FK_curso, FK_usuario, estrelas, motivo
                        Integer fkCurso = r[0] == null ? null : ((Number) r[0]).intValue();
                        Integer fkUsuario = r[1] == null ? null : ((Number) r[1]).intValue();
                        Integer estrelas = r[2] == null ? null : ((Number) r[2]).intValue();
                        String motivo = r[3] == null ? null : r[3].toString();

                        f.setFkCurso(fkCurso);
                        f.setEstrelas(estrelas);
                        f.setMotivo(motivo);
                        // minimal usuario domain
                        if (fkUsuario != null) {
                            servicos.gratitude.be_gratitude_capacita.core.domain.Usuario u = new servicos.gratitude.be_gratitude_capacita.core.domain.Usuario();
                            u.setIdUsuario(fkUsuario);
                            f.setFkUsuario(u);
                        }
                        // set curso domain
                        servicos.gratitude.be_gratitude_capacita.core.domain.Curso c = new servicos.gratitude.be_gratitude_capacita.core.domain.Curso();
                        c.setIdCurso(fkCurso);
                        f.setCurso(c);

                        nativeMapped.add(f);
                    }
                    return nativeMapped;
                }
            } catch (Exception ex) {
                log.warn("FeedbackAdapter: native fallback failed: {}", ex.getMessage());
            }

            // If native fallback via JPA didn't return rows, try JdbcTemplate directly
            try {
                List<Feedback> jdbcRows = jdbcTemplate.query("SELECT FK_curso, FK_usuario, estrelas, motivo FROM feedback WHERE FK_curso = ?",
                        new Object[]{idCurso}, (rs, rowNum) -> {
                            Feedback f = new Feedback();
                            Integer fkCurso = rs.getInt("FK_curso");
                            int fkUsuario = rs.getInt("FK_usuario");
                            int estrelas = rs.getInt("estrelas");
                            String motivo = rs.getString("motivo");
                            f.setFkCurso(fkCurso);
                            f.setEstrelas(estrelas);
                            f.setMotivo(motivo);
                            servicos.gratitude.be_gratitude_capacita.core.domain.Usuario u = new servicos.gratitude.be_gratitude_capacita.core.domain.Usuario();
                            u.setIdUsuario(fkUsuario);
                            f.setFkUsuario(u);
                            servicos.gratitude.be_gratitude_capacita.core.domain.Curso c = new servicos.gratitude.be_gratitude_capacita.core.domain.Curso();
                            c.setIdCurso(fkCurso);
                            f.setCurso(c);
                            return f;
                        });
                log.info("FeedbackAdapter: JdbcTemplate fallback returned {} rows for curso {}", jdbcRows.size(), idCurso);
                jdbcRows.forEach(f -> log.debug("Jdbc fallback mapped curso={}, usuarioId={}, estrelas={}, motivo={}", f.getFkCurso(), f.getFkUsuario() == null ? null : f.getFkUsuario().getIdUsuario(), f.getEstrelas(), f.getMotivo()));
                if (jdbcRows != null && !jdbcRows.isEmpty()) return jdbcRows;
            } catch (DataAccessException dae) {
                log.warn("FeedbackAdapter: JdbcTemplate fallback failed: {}", dae.getMessage());
            }
        }
        // inject curso object into domain feedbacks
        if (domains != null) {
            domains.forEach(d -> {
                if (d.getCurso() == null) {
                    Curso c = new Curso();
                    c.setIdCurso(idCurso);
                    d.setCurso(c);
                }
            });
        }

        return domains;
    }
}
