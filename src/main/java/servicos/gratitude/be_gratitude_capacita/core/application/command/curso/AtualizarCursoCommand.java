package servicos.gratitude.be_gratitude_capacita.core.application.command.curso;

public record AtualizarCursoCommand(
    String tituloCurso,
    String descricao,
    String imagem,
    Integer duracaoEstimada
) {
}
