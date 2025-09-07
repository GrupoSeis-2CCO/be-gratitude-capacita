package servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class AtualizarOcultoApostilaUseCase {
    private final ApostilaGateway apostilaGateway;

    public AtualizarOcultoApostilaUseCase(ApostilaGateway apostilaGateway) {
        this.apostilaGateway = apostilaGateway;
    }

    public Apostila execute(Integer idApostila){
        Apostila apostilaParaAtualizar = apostilaGateway.findById(idApostila);

        if (Objects.isNull(apostilaParaAtualizar)){
        throw new NaoEncontradoException("Não foi encontrado apostila com o id informado");
        }

        apostilaParaAtualizar.setApostilaOculto(!apostilaParaAtualizar.getApostilaOculto());
        apostilaParaAtualizar.setDataAtualizacaoApostila(LocalDateTime.now());

        return apostilaGateway.save(apostilaParaAtualizar);
    }
}
