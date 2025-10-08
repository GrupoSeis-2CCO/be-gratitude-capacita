package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record FeedbackResponse(
        Integer cursoId,
        String cursoTitulo,
        Integer usuarioId,
        String aluno,
        Integer estrelas,
        String motivo
) {

    public static FeedbackResponse fromDomain(Feedback feedback) {
        Usuario usuario = feedback.getFkUsuario();
        Curso curso = feedback.getCurso();

        return new FeedbackResponse(
                feedback.getFkCurso(),
                Objects.nonNull(curso) ? curso.getTituloCurso() : null,
                Objects.nonNull(usuario) ? usuario.getIdUsuario() : null,
                Objects.nonNull(usuario) ? usuario.getNome() : null,
                feedback.getEstrelas(),
                feedback.getMotivo()
        );
    }

    public static List<FeedbackResponse> fromDomains(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackResponse::fromDomain)
                .collect(Collectors.toList());
    }
}
