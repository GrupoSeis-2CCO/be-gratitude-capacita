package servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;

import java.util.Objects;

public class EncontrarTentativaPorId {
    private final TentativaGateway tentativaGateway;

    public EncontrarTentativaPorId(TentativaGateway tentativaGateway) {
        this.tentativaGateway = tentativaGateway;
    }

    public Tentativa execute(TentativaCompoundKey tentativaCompoundKey){
        Tentativa tentativa = tentativaGateway.findById(tentativaCompoundKey);

        if (Objects.isNull(tentativa)){
            throw new NaoEncontradoException("Não foi encontrado tentativa com o id informado");
        }

        return tentativa;
    }
}
