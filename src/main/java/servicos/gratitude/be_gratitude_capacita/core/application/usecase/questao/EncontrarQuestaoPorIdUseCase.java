package servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.Objects;

public class EncontrarQuestaoPorIdUseCase {
    private final QuestaoGateway questaoGateway;

    public EncontrarQuestaoPorIdUseCase(QuestaoGateway questaoGateway) {
        this.questaoGateway = questaoGateway;
    }

    public Questao execute(QuestaoCompoundKey idQuestaoComposto){
        if (Objects.isNull(idQuestaoComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!questaoGateway.existsById(idQuestaoComposto)){
            throw new NaoEncontradoException("Não foi encontrado questão para o id informado");
        }

        return questaoGateway.findById(idQuestaoComposto);
    }
}
