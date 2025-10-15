package servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa;

import servicos.gratitude.be_gratitude_capacita.core.application.command.alternativa.CriarAlternativaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.List;
import java.util.Objects;

public class CriarAlternativaUseCase {
    private final AlternativaGateway alternativaGateway;
    private final QuestaoGateway questaoGateway;

    public CriarAlternativaUseCase(AlternativaGateway alternativaGateway, QuestaoGateway questaoGateway) {
        this.alternativaGateway = alternativaGateway;
        this.questaoGateway = questaoGateway;
    }

    public Alternativa execute(CriarAlternativaCommand command, AlternativaCompoundKey idAlternativaComposto){
        if (Objects.isNull(command) ||
                Objects.isNull(idAlternativaComposto) ||
                command.texto() == null ||
                command.texto().isBlank()
        ){
            throw new ValorInvalidoException("Valores inválidos em campos obrigatórios");
        } else if (alternativaGateway.existsById(idAlternativaComposto)) {
            throw new ConflitoException("Já existe uma questao com o id informado");
        }

    servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey questaoCompoundKey = new servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey();
    questaoCompoundKey.setIdQuestao(idAlternativaComposto.getIdQuestao());
    questaoCompoundKey.setFkAvaliacao(idAlternativaComposto.getIdAvaliacao());
    Questao questao = questaoGateway.findById(questaoCompoundKey);

        List<Alternativa> alternativasDaQuestao = alternativaGateway.findAllByQuestao(questao);
        Integer maiorOrdem = 0;

        for (Alternativa alternativaDaVez : alternativasDaQuestao) {
            if (alternativaDaVez.getOrdem() >= maiorOrdem){
                maiorOrdem = alternativaDaVez.getOrdem();
            }
        }

        Alternativa alternativa = new Alternativa();
        alternativa.setQuestao(questao);
        alternativa.setTexto(command.texto());
        alternativa.setAlternativaChaveComposta(idAlternativaComposto);
        alternativa.setOrdem(maiorOrdem + 1);

        return alternativaGateway.save(alternativa);
    }
}
