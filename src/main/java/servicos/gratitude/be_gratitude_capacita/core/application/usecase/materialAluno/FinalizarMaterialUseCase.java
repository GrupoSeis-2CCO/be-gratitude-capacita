package servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;

import java.util.Objects;

public class FinalizarMaterialUseCase {
    private final MaterialAlunoGateway materialAlunoGateway;

    public FinalizarMaterialUseCase(MaterialAlunoGateway materialAlunoGateway) {
        this.materialAlunoGateway = materialAlunoGateway;
    }

    public MaterialAluno execute(MaterialAlunoCompoundKey idMaterialAlunoComposto){
        if (Objects.isNull(idMaterialAlunoComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!materialAlunoGateway.existsById(idMaterialAlunoComposto)){
            throw new NaoEncontradoException("Não foi encontrado material com o id informado");
        }

        MaterialAluno materialAlunoParaAtualizar = materialAlunoGateway.findById(idMaterialAlunoComposto);

        materialAlunoParaAtualizar.setFinalizado(true);

        return materialAlunoGateway.save(materialAlunoParaAtualizar);
    }
}
