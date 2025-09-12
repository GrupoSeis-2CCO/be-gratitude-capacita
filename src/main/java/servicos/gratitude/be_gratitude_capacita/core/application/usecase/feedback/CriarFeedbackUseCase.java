package servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback;

import servicos.gratitude.be_gratitude_capacita.core.application.command.feedback.CriarFeedbackCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.FeedbackGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

import java.util.Objects;

public class CriarFeedbackUseCase {
    private final FeedbackGateway feedbackGateway;
    private final UsuarioGateway usuarioGateway;

    public CriarFeedbackUseCase(FeedbackGateway feedbackGateway, UsuarioGateway usuarioGateway) {
        this.feedbackGateway = feedbackGateway;
        this.usuarioGateway = usuarioGateway;
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

        Usuario usuario = new Usuario();

        if (command.fkUsuario() != null) {
            usuario = usuarioGateway.findById(command.fkUsuario());
        }

        Feedback feedback = new Feedback();

        feedback.setCurso(curso);
        feedback.setFkCurso(curso.getIdCurso());
        feedback.setEstrelas(command.estrelas());
        feedback.setFkUsuario(usuario);
        feedback.setMotivo(command.motivo());

        return feedbackGateway.save(feedback);
    }
}
