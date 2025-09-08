package servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao;

import servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.DefinirAcertosMinimosCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;

public class AtualizarAcertosMinimosAvaliacaoUseCase {
    private final AvaliacaoGateway avaliacaoGateway;

    public AtualizarAcertosMinimosAvaliacaoUseCase(AvaliacaoGateway avaliacaoGateway) {
        this.avaliacaoGateway = avaliacaoGateway;
    }

    public Avaliacao execute(Integer idAvaliacao, DefinirAcertosMinimosCommand command){
        if (!avaliacaoGateway.existsById(idAvaliacao)){
            throw new NaoEncontradoException("Não foi encontrado avaliação com o id informado");
        } else if (command.acertosMinimos() == null || command.acertosMinimos() <= 0){
            throw new ValorInvalidoException("Valor inválido para acertos minimos");
        }

        Avaliacao avaliacaoParaAtualizar = avaliacaoGateway.findById(idAvaliacao);
        Avaliacao avaliacaoAtualizada = new Avaliacao();

        avaliacaoAtualizada.setIdAvaliacao(idAvaliacao);
        avaliacaoAtualizada.setAcertosMinimos(command.acertosMinimos());
        avaliacaoAtualizada.setFkCurso(avaliacaoParaAtualizar.getFkCurso());

        return avaliacaoGateway.save(avaliacaoAtualizada);
    }
}
