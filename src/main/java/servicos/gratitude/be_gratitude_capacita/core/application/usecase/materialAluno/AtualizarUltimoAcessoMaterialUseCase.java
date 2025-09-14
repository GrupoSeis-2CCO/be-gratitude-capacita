package servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class AtualizarUltimoAcessoMaterialUseCase {
    private final MaterialAlunoGateway materialAlunoGateway;

    public AtualizarUltimoAcessoMaterialUseCase(MaterialAlunoGateway materialAlunoGateway) {
        this.materialAlunoGateway = materialAlunoGateway;
    }

    public MaterialAluno execute(MaterialAlunoCompoundKey idMaterialAlunoComposto){
        if (Objects.isNull(idMaterialAlunoComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!materialAlunoGateway.existsById(idMaterialAlunoComposto)){
            throw new NaoEncontradoException("Não foi encontrado material com o id informado");
        }

        MaterialAluno materialAlunoParaAtualizar = materialAlunoGateway.findById(idMaterialAlunoComposto);

        materialAlunoParaAtualizar.setUltimoAcesso(LocalDateTime.now());

        return materialAlunoGateway.save(materialAlunoParaAtualizar);
    }
}
