package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

import java.util.List;
import java.util.Objects;

public class CriarChaveCompostaMaterialAluno {
    private final MaterialAlunoGateway materialAlunoGateway;
    private final MatriculaGateway matriculaGateway;

    public CriarChaveCompostaMaterialAluno(MaterialAlunoGateway materialAlunoGateway, MatriculaGateway matriculaGateway) {
        this.materialAlunoGateway = materialAlunoGateway;
        this.matriculaGateway = matriculaGateway;
    }

    public MaterialAlunoCompoundKey execute(MatriculaCompoundKey matriculaCompoundKey){
        if (Objects.isNull(matriculaCompoundKey)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Matricula matricula = matriculaGateway.findById(matriculaCompoundKey);

        if (Objects.isNull(matricula)){
            throw new NaoEncontradoException("Não foi encontrado matricula para o id informado");
        }

        List<MaterialAluno> materiais = materialAlunoGateway.findAllByMatricula(matricula);
        Integer maiorId = 0;

        if (!materiais.isEmpty()){
            for (MaterialAluno materialDaVez : materiais) {
                if (materialDaVez.getIdMaterialAlunoComposto().getIdMaterialAluno() >= maiorId){
                    maiorId = materialDaVez.getIdMaterialAlunoComposto().getIdMaterialAluno();
                }
            }
        }

        MaterialAlunoCompoundKey idComposto = new MaterialAlunoCompoundKey();
        idComposto.setIdMatriculaComposto(matriculaCompoundKey);
        idComposto.setIdMaterialAluno(maiorId + 1);

        return idComposto;
    }
}
