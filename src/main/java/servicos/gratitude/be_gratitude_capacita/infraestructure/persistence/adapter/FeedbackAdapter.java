package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.gateways.FeedbackGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.FeedbackMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.FeedbackRepository;

import java.util.List;

@Service
public class FeedbackAdapter implements FeedbackGateway {
    private final FeedbackRepository feedbackRepository;

    public FeedbackAdapter(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }


    @Override
    public Feedback save(Feedback feedback) {
        FeedbackEntity entity = FeedbackMapper.toEntity(feedback);

        return FeedbackMapper.toDomain(feedbackRepository.save(entity));
    }

    @Override
    public List<Feedback> findAllByCurso(Curso fkCurso) {
        return FeedbackMapper.toDomains(feedbackRepository.findAllByFkCurso(CursoMapper.toEntity(fkCurso)));
    }
}
