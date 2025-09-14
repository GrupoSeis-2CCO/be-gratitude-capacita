package servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

import java.util.List;
import java.util.Objects;

public class ListarMatriculaPorCursoUseCase {
    private final MatriculaGateway matriculaGateway;

    public ListarMatriculaPorCursoUseCase(MatriculaGateway matriculaGateway) {
        this.matriculaGateway = matriculaGateway;
    }

    public List<Matricula> execute(Curso curso){
        if (Objects.isNull(curso)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        return matriculaGateway.findAllByCurso(curso);
    }
}
