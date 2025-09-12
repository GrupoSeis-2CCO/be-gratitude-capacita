package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.List;
import java.util.Objects;

public class CriarChaveCompostaAlternativa {
    private final AlternativaGateway alternativaGateway;
    private final QuestaoGateway questaoGateway;

    public CriarChaveCompostaAlternativa(AlternativaGateway alternativaGateway, QuestaoGateway questaoGateway) {
        this.alternativaGateway = alternativaGateway;
        this.questaoGateway = questaoGateway;
    }

    public AlternativaCompoundKey execute(QuestaoCompoundKey questaoCompoundKey){
        if (Objects.isNull(questaoCompoundKey)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Questao questao = questaoGateway.findById(questaoCompoundKey);

        if (Objects.isNull(questao)){
            throw new NaoEncontradoException("Não foi encontrado questão para o id informado");
        }

        List<Alternativa> alternativas = alternativaGateway.findAllByQuestao(questao);
        Integer maiorId = 0;

        if (!alternativas.isEmpty()){
            for (Alternativa alternativaDaVez : alternativas) {
                if (alternativaDaVez.getAlternativaChaveComposta().getIdAlternativa() >= maiorId){
                    maiorId = alternativaDaVez.getAlternativaChaveComposta().getIdAlternativa();
                }
            }
        }

        AlternativaCompoundKey idComposto = new AlternativaCompoundKey();
        idComposto.setIdQuestaoComposto(questaoCompoundKey);
        idComposto.setIdAlternativa(maiorId + 1);

        return idComposto;
    }
}
