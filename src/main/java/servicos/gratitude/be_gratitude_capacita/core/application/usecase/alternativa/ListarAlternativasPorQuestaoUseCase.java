package servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa;

import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;

import java.util.List;

public class ListarAlternativasPorQuestaoUseCase {
    private final AlternativaGateway alternativaGateway;

    public ListarAlternativasPorQuestaoUseCase(AlternativaGateway alternativaGateway) {
        this.alternativaGateway = alternativaGateway;
    }

    public List<Alternativa> execute(Questao questao){
        return alternativaGateway.findAllByQuestao(questao);
    }
}
