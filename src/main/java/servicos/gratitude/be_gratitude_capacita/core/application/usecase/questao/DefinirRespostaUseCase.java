package servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.Objects;

public class DefinirRespostaUseCase {
    private final QuestaoGateway questaoGateway;
    private final AlternativaGateway alternativaGateway;

    public DefinirRespostaUseCase(QuestaoGateway questaoGateway, AlternativaGateway alternativaGateway) {
        this.questaoGateway = questaoGateway;
        this.alternativaGateway = alternativaGateway;
    }

    public Questao execute (AlternativaCompoundKey idAlternativaComposto, QuestaoCompoundKey idQuestaoComposto){
        if (Objects.isNull(idAlternativaComposto) || Objects.isNull(idQuestaoComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!alternativaGateway.existsById(idAlternativaComposto)){
            throw new NaoEncontradoException("Não foi encontrada alternativa para o id informado");
        } else if (!questaoGateway.existsById(idQuestaoComposto)){
            throw new NaoEncontradoException("Não foi encontrada questao para o id informado");
        }

        Questao questao = questaoGateway.findById(idQuestaoComposto);
        Alternativa alternativaCorreta = alternativaGateway.findById(idAlternativaComposto);

        questao.setFkAlternativaCorreta(alternativaCorreta);

        return questaoGateway.save(questao);
    }
}
