package servicos.gratitude.be_gratitude_capacita.core.application.command.matricula;

public record CriarMatriculaCommand(
        Integer fkUsuario,
        Integer fkCurso
) {
}
