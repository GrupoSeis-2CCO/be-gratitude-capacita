package servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;

import java.util.List;
import java.util.Objects;

public class ListarMaterialPorMatriculaUseCase {
    private final MaterialAlunoGateway materialAlunoGateway;

    public ListarMaterialPorMatriculaUseCase(MaterialAlunoGateway materialAlunoGateway) {
        this.materialAlunoGateway = materialAlunoGateway;
    }

    public List<MaterialAluno> execute(Matricula matricula){
        if (Objects.isNull(matricula)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        return materialAlunoGateway.findAllByMatricula(matricula);
    }
}
