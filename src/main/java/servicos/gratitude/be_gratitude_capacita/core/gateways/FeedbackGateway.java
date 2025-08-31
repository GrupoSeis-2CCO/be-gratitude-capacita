package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;

import java.util.List;

public interface FeedbackGateway {
    Feedback save(Feedback feedback);
    List<Feedback> findAllByCurso(Curso fkCurso);
}
