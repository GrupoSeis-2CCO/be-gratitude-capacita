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
        Long idCurso = curso.getIdCurso();
        List<FeedbackEntity> entities = feedbackRepository.findAllByFkCurso(idCurso.intValue());
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
        return FeedbackMapper.toDomains(entities);
    }
}
