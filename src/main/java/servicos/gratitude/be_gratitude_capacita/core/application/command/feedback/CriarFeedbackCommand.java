package servicos.gratitude.be_gratitude_capacita.core.application.command.feedback;

public record CriarFeedbackCommand(
         Integer idCurso,
         Integer estrelas,
         String motivo,
         Integer fkUsuario,
         Boolean anonimo
) {
}
