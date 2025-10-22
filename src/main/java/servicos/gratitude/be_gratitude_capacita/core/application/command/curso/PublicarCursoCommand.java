package servicos.gratitude.be_gratitude_capacita.core.application.command.curso;

import java.util.List;

public record PublicarCursoCommand(
    Integer idCurso,
    Boolean notificarTodos,
    List<Integer> idsAlunosSelecionados
) {
}
