package servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;

import java.util.List;
import java.util.Objects;

public class ListarTentativaPorMatriculaUseCase {
    private final TentativaGateway tentativaGateway;

    public ListarTentativaPorMatriculaUseCase(TentativaGateway tentativaGateway) {
        this.tentativaGateway = tentativaGateway;
    }

    public List<Tentativa> execute(Matricula matricula){
        if (Objects.isNull(matricula)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigátorios");
        }

        return tentativaGateway.findAllByMatricula(matricula);
    }
}
