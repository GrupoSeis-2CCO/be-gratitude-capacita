package servicos.gratitude.be_gratitude_capacita.core.application.command.materialAluno;

public record CriarMaterialAlunoCommand(
        Integer fkUsuario,
        Integer fkCurso,
        Integer fkVideo,
        Integer fkApostila
) {
}
