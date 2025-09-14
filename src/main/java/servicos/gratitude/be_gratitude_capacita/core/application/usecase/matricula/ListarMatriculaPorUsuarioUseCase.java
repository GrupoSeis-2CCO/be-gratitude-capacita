package servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

import java.util.List;
import java.util.Objects;

public class ListarMatriculaPorUsuarioUseCase {
    private final MatriculaGateway matriculaGateway;

    public ListarMatriculaPorUsuarioUseCase(MatriculaGateway matriculaGateway) {
        this.matriculaGateway = matriculaGateway;
    }

    public List<Matricula> execute(Usuario usuario){
        if (Objects.isNull(usuario)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        return matriculaGateway.findAllByUsuario(usuario);
    }
}
