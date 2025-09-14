package servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;

import java.util.Objects;

public class CriarMaterialAlunoUseCase {
    private final MaterialAlunoGateway materialAlunoGateway;

    public CriarMaterialAlunoUseCase(MaterialAlunoGateway materialAlunoGateway) {
        this.materialAlunoGateway = materialAlunoGateway;
    }

    public MaterialAluno execute(
            MaterialAlunoCompoundKey idMaterialAlunoComposto,
            Video video,
            Apostila apostila,
            Matricula matricula
    ){
        if(
                Objects.isNull(matricula) ||
                Objects.isNull(idMaterialAlunoComposto) ||
                (Objects.isNull(video) && Objects.isNull(apostila)) ||
                (Objects.nonNull(video) && Objects.nonNull(apostila))
        ){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (materialAlunoGateway.existsById(idMaterialAlunoComposto)){
            throw new ConflitoException("Já existe uma matrícula com o id informado");
        }

        MaterialAluno materialAluno = new MaterialAluno();

        materialAluno.setIdMaterialAlunoComposto(idMaterialAlunoComposto);
        materialAluno.setFinalizado(false);
        materialAluno.setFkApostila(apostila);
        materialAluno.setFkVideo(video);
        materialAluno.setMatricula(matricula);

        return materialAlunoGateway.save(materialAluno);
    }
}
