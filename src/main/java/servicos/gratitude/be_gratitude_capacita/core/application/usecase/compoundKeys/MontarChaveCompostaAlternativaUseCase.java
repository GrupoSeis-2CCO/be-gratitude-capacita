package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.List;
import java.util.Objects;

public class MontarChaveCompostaAlternativaUseCase {
    private final QuestaoGateway questaoGateway;

    public MontarChaveCompostaAlternativaUseCase(QuestaoGateway questaoGateway) {
        this.questaoGateway = questaoGateway;
    }

    public AlternativaCompoundKey execute(QuestaoCompoundKey questaoCompoundKey, Integer idAlternativa){
        if (Objects.isNull(questaoCompoundKey) || idAlternativa == null || idAlternativa <= 0){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Questao questao = questaoGateway.findById(questaoCompoundKey);

        if (Objects.isNull(questao)){
            throw new NaoEncontradoException("Não foi encontrado questão para o id informado");
        }

        AlternativaCompoundKey idComposto = new AlternativaCompoundKey();
        idComposto.setIdQuestaoComposto(questaoCompoundKey);
        idComposto.setIdAlternativa(idAlternativa);

        return idComposto;
    }
}
