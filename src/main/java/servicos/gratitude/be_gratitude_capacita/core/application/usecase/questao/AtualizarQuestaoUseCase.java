package servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao;

import servicos.gratitude.be_gratitude_capacita.core.application.command.questao.AtualizarQuestaoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;

import java.util.Objects;

public class AtualizarQuestaoUseCase {
    private final QuestaoGateway questaoGateway;
    private final AvaliacaoGateway avaliacaoGateway;

    public AtualizarQuestaoUseCase(QuestaoGateway questaoGateway, AvaliacaoGateway avaliacaoGateway) {
        this.questaoGateway = questaoGateway;
        this.avaliacaoGateway = avaliacaoGateway;
    }

    public Questao execute(
            AtualizarQuestaoCommand command,
            QuestaoCompoundKey questaoIdComposto
    ){
        if (Objects.isNull(command) ||
                Objects.isNull(questaoIdComposto) ||
                command.enunciado() == null ||
                command.enunciado().isBlank()
        ){
            throw new ValorInvalidoException("Valores obrigatórios inválidos");
        } else if (!questaoGateway.existsById(questaoIdComposto)){
            throw new NaoEncontradoException("Não foi encontrado questao para o id informado");
        }

        Avaliacao avaliacao = avaliacaoGateway.findById(questaoIdComposto.getFkAvaliacao());
        Questao questaoParaAtualizar = questaoGateway.findById(questaoIdComposto);
        Questao questaoAtualizada = new Questao();

        questaoAtualizada.setIdQuestaoComposto(questaoIdComposto);
        questaoAtualizada.setAvaliacao(avaliacao);
        questaoAtualizada.setEnunciado(command.enunciado());
        questaoAtualizada.setFkAlternativaCorreta(questaoParaAtualizar.getFkAlternativaCorreta());

        return questaoGateway.save(questaoAtualizada);
    }
}
