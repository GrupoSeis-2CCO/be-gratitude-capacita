package servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;

public class DeletarApostilaUseCase {
    private final ApostilaGateway apostilaGateway;

    public DeletarApostilaUseCase(ApostilaGateway apostilaGateway) {
        this.apostilaGateway = apostilaGateway;
    }

    public void execute(Integer idApostila){
        if (!apostilaGateway.existsById(idApostila)){
            throw new NaoEncontradoException("Não foi encontrado apostila com id informado");
        }

        apostilaGateway.deleteById(idApostila);
    }
}
