package servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

import java.util.Objects;

public class EncontrarMatriculaPorIdUseCase {
    private final MatriculaGateway matriculaGateway;

    public EncontrarMatriculaPorIdUseCase(MatriculaGateway matriculaGateway) {
        this.matriculaGateway = matriculaGateway;
    }

    public Matricula execute(MatriculaCompoundKey idMatriculaComposto){
        if (Objects.isNull(idMatriculaComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigátorios");
        }

        Matricula matricula = matriculaGateway.findById(idMatriculaComposto);

        if (Objects.isNull(matricula)){
            throw new NaoEncontradoException("Não foi encontrado matrícula com o id informado");
        }

        return matricula;
    }
}
