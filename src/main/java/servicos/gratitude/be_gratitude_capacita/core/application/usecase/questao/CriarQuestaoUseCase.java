package servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao;

import servicos.gratitude.be_gratitude_capacita.core.application.command.questao.CriarQuestaoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.List;
import java.util.Objects;

public class CriarQuestaoUseCase {
        private final QuestaoGateway questaoGateway;
        private final AvaliacaoGateway avaliacaoGateway;

    public CriarQuestaoUseCase(QuestaoGateway questaoGateway, AvaliacaoGateway avaliacaoGateway) {
        this.questaoGateway = questaoGateway;
        this.avaliacaoGateway = avaliacaoGateway;
    }

    public Questao execute(CriarQuestaoCommand command, QuestaoCompoundKey idQuestaoComposto){
        if (Objects.isNull(command) ||
                Objects.isNull(idQuestaoComposto) ||
                command.fkAvaliacao() == null ||
                command.numeroQuestao() == null ||
                command.enunciado() == null ||
                command.enunciado().isBlank() ||
                command.numeroQuestao() <= 0 ||
                command.fkAvaliacao() <= 0
        ){
            throw new ValorInvalidoException("Valores inválidos em campos obrigatórios");
        } else if (questaoGateway.existsById(idQuestaoComposto)) {
            throw new ConflitoException("Já existe uma questao com o id informado");
        } else if (!avaliacaoGateway.existsById(idQuestaoComposto.getFkAvaliacao())) {
            throw new NaoEncontradoException("Não foi encontrada avaliação com o id informado");
        }
        Avaliacao avaliacao = avaliacaoGateway.findById(idQuestaoComposto.getFkAvaliacao());

        Questao questao = new Questao();
        questao.setAvaliacao(avaliacao);
        questao.setEnunciado(command.enunciado());
        questao.setIdQuestaoComposto(idQuestaoComposto);
        
        return questaoGateway.save(questao);
    }
}
