package servicos.gratitude.be_gratitude_capacita.core.application.command.respostaDoUsuario;

public record CriarRespostaDoUsuarioCommand(
        Integer fkUsuario,
        Integer fkCurso,
        Integer fkTentativa,
        Integer fkAvaliacao,
        Integer fkQuestao,
        Integer fkAlternativa
) {
}
