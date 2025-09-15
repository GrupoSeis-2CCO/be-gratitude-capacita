package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.List;
import java.util.Objects;

public class CriarNovaChaveCompostaQuestaoUseCase {
    private final QuestaoGateway questaoGateway;
    private final AvaliacaoGateway avaliacaoGateway;

    public CriarNovaChaveCompostaQuestaoUseCase(QuestaoGateway questaoGateway, AvaliacaoGateway avaliacaoGateway) {
        this.questaoGateway = questaoGateway;
        this.avaliacaoGateway = avaliacaoGateway;
    }

    public QuestaoCompoundKey execute(Integer fkAvaliacao){
        if (fkAvaliacao == null || fkAvaliacao <= 0){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Avaliacao avaliacao = avaliacaoGateway.findById(fkAvaliacao);

        if (Objects.isNull(avaliacao)){
            throw new NaoEncontradoException("Não foi encontrado avaliação com o id informado");
        }

        List<Questao> questoes = questaoGateway.findAllByAvaliacao(avaliacao);
        Integer maiorId = 0;

        if (!questoes.isEmpty()){
            for (Questao questaoDaVez : questoes) {
                if (questaoDaVez.getIdQuestaoComposto().getIdQuestao() >= maiorId){
                    maiorId = questaoDaVez.getIdQuestaoComposto().getIdQuestao();
                }
            }
        }
        QuestaoCompoundKey idComposto = new QuestaoCompoundKey();
        idComposto.setFkAvaliacao(fkAvaliacao);
        idComposto.setIdQuestao(maiorId + 1);

        return idComposto;
    }
}
