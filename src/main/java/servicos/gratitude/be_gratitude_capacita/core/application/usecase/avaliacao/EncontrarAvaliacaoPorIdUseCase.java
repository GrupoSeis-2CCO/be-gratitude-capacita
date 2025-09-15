package servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;

import java.util.Objects;

public class EncontrarAvaliacaoPorIdUseCase {
    private final AvaliacaoGateway avaliacaoGateway;

    public EncontrarAvaliacaoPorIdUseCase(AvaliacaoGateway avaliacaoGateway) {
        this.avaliacaoGateway = avaliacaoGateway;
    }

    public Avaliacao execute(Integer idAvaliacao){
        if (idAvaliacao == null || idAvaliacao <= 0){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Avaliacao avaliacao = avaliacaoGateway.findById(idAvaliacao);

        if (Objects.isNull(avaliacao)){
            throw new NaoEncontradoException("Não foi encontrado avaliação para o id informado");
        }

        return avaliacao;
    }
}
