package servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula;

import servicos.gratitude.be_gratitude_capacita.core.application.command.matricula.CriarMatriculaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class CriarMatriculaUseCase {
    private final MatriculaGateway matriculaGateway;

    public CriarMatriculaUseCase(MatriculaGateway matriculaGateway) {
        this.matriculaGateway = matriculaGateway;
    }

    public Matricula execute(CriarMatriculaCommand command, MatriculaCompoundKey idMatriculaComposto, Usuario usuario, Curso curso){
        if(Objects.isNull(command) || Objects.isNull(idMatriculaComposto) || Objects.isNull(usuario) || Objects.isNull(curso)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (matriculaGateway.existsById(idMatriculaComposto)){
            throw new ConflitoException("Já existe uma matrícula com o id informado");
        }

        Matricula matricula = new Matricula();

        matricula.setIdMatriculaComposto(idMatriculaComposto);
        matricula.setCompleto(false);
        matricula.setDtInscricao(LocalDateTime.now());
        matricula.setCurso(curso);
        matricula.setUsuario(usuario);

        return matriculaGateway.save(matricula);
    }
}
