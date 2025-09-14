package servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;

public class DeletarMaterialUseCase {
    private final MaterialAlunoGateway materialAlunoGateway;

    public DeletarMaterialUseCase(MaterialAlunoGateway materialAlunoGateway) {
        this.materialAlunoGateway = materialAlunoGateway;
    }

    public void execute(MaterialAlunoCompoundKey idMaterialAluno){
        if (!materialAlunoGateway.existsById(idMaterialAluno)){
            throw new NaoEncontradoException("Não foi encontrado material com o id informado");
        }

        materialAlunoGateway.deleteById(idMaterialAluno);
    }
}
