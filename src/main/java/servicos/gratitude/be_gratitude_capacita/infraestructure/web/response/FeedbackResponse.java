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
    String motivo,
    Boolean anonimo
) {

    public static FeedbackResponse fromDomain(Feedback feedback) {
        Usuario usuario = feedback.getFkUsuario();
        Curso curso = feedback.getCurso();

    boolean isAnon = Boolean.TRUE.equals(feedback.getAnonimo());
    String alunoNome = (Objects.nonNull(usuario) ? usuario.getNome() : null);
    if (isAnon) alunoNome = null; // ocultar nome no modo anônimo

    return new FeedbackResponse(
                feedback.getFkCurso(),
                Objects.nonNull(curso) ? curso.getTituloCurso() : null,
                Objects.nonNull(usuario) ? usuario.getIdUsuario() : null,
        alunoNome,
                feedback.getEstrelas(),
        feedback.getMotivo(),
        isAnon
        );
    }

    public static List<FeedbackResponse> fromDomains(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackResponse::fromDomain)
                .collect(Collectors.toList());
    }
}
