package servicos.gratitude.be_gratitude_capacita.core.application.command.tentativa;

public record CriarTentativaCommand(
        Integer fkUsuario,
        Integer fkCurso,
        Integer fkAvaliacao
) {
}
