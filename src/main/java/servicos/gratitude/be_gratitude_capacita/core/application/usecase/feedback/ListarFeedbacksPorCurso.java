package servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.gateways.FeedbackGateway;

import java.util.List;
import java.util.Objects;

public class ListarFeedbacksPorCurso {
    private final FeedbackGateway feedbackGateway;

    public ListarFeedbacksPorCurso(FeedbackGateway feedbackGateway) {
        this.feedbackGateway = feedbackGateway;
    }

    public List<Feedback> execute(Curso curso){
        if (Objects.isNull(curso)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigátorios");
        }

        return feedbackGateway.findAllByCurso(curso);
    }
}
