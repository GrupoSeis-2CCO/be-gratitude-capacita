package servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.List;

public class ListarQuestoesPorAvaliacaoUseCase {
    private final QuestaoGateway questaoGateway;
    private final AvaliacaoGateway avaliacaoGateway;

    public ListarQuestoesPorAvaliacaoUseCase(QuestaoGateway questaoGateway, AvaliacaoGateway avaliacaoGateway) {
        this.questaoGateway = questaoGateway;
        this.avaliacaoGateway = avaliacaoGateway;
    }

    public List<Questao> execute(Integer fkAvaliacao){
        if (fkAvaliacao == null || fkAvaliacao <= 0){
            throw new ValorInvalidoException("Valores inválidos na URI");
        } else if (!avaliacaoGateway.existsById(fkAvaliacao)){
            throw new NaoEncontradoException("Não foi encontrada avaiação para o id informado");
        }

        Avaliacao avaliacao = avaliacaoGateway.findById(fkAvaliacao);
        return questaoGateway.findAllByAvaliacao(avaliacao);
    }
}
