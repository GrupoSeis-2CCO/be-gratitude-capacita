package servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback;

import servicos.gratitude.be_gratitude_capacita.core.application.command.feedback.CriarFeedbackCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.FeedbackGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

import java.util.Objects;

public class CriarFeedbackUseCase {
    private final FeedbackGateway feedbackGateway;
    private final UsuarioGateway usuarioGateway;
    private final MatriculaGateway matriculaGateway;

    public CriarFeedbackUseCase(FeedbackGateway feedbackGateway, UsuarioGateway usuarioGateway, MatriculaGateway matriculaGateway) {
        this.feedbackGateway = feedbackGateway;
        this.usuarioGateway = usuarioGateway;
        this.matriculaGateway = matriculaGateway;
    }

    public Feedback execute(CriarFeedbackCommand command, Curso curso){
        if (
                Objects.isNull(command) ||
                Objects.isNull(curso) ||
                command.estrelas() == null ||
                command.idCurso() <= 0 ||
                (command.fkUsuario() != null && command.fkUsuario() <= 0)
        ) {
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (command.fkUsuario() != null && !usuarioGateway.existsById(command.fkUsuario())){
            throw new NaoEncontradoException("Não encontrado usuário para o id informado");
        }

        // Require fkUsuario to be present (only enrolled users can give feedback)
        if (command.fkUsuario() == null || command.fkUsuario() <= 0) {
            throw new ValorInvalidoException("Somente usuários autenticados podem enviar feedback");
        }

        if (!usuarioGateway.existsById(command.fkUsuario())){
            throw new NaoEncontradoException("Usuário não encontrado para o id informado");
        }

        Usuario usuario = usuarioGateway.findById(command.fkUsuario());

        // Verifica matrícula do usuário no curso e se está completa
        MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
        idMatriculaComposto.setFkCurso(command.idCurso());
        idMatriculaComposto.setFkUsuario(command.fkUsuario());

        if (!matriculaGateway.existsById(idMatriculaComposto)){
            throw new NaoEncontradoException("Usuário não está matriculado neste curso");
        }

        servicos.gratitude.be_gratitude_capacita.core.domain.Matricula matricula = matriculaGateway.findById(idMatriculaComposto);
        if (matricula == null || matricula.getCompleto() == null || !matricula.getCompleto()){
            throw new ConflitoException("Só é possível avaliar após concluir todos os materiais do curso");
        }

        Feedback feedback = new Feedback();

        feedback.setCurso(curso);
        feedback.setFkCurso(curso.getIdCurso());
        feedback.setEstrelas(command.estrelas());
        feedback.setFkUsuario(usuario);
        feedback.setMotivo(command.motivo());
        feedback.setAnonimo(Boolean.TRUE.equals(command.anonimo()));

        return feedbackGateway.save(feedback);
    }
}
