package servicos.gratitude.be_gratitude_capacita.core.application.command.curso;

public record CriarCursoCommand(
    String tituloCurso,
    String descricao,
    String imagem,
    Integer duracaoEstimada,
    Boolean ocultado
) {
    // Construtor de compatibilidade para código existente (sem ocultado)
    public CriarCursoCommand(String tituloCurso, String descricao, String imagem, Integer duracaoEstimada) {
        this(tituloCurso, descricao, imagem, duracaoEstimada, true); // default: oculto
    }
}
