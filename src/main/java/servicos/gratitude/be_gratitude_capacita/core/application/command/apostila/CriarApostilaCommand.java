package servicos.gratitude.be_gratitude_capacita.core.application.command.apostila;

public record CriarApostilaCommand(
         Integer fkCurso,
         String nomeApostila,
         String descricaoApostila,
         Integer tamanhoBytes,
         String urlArquivo
) {
    // Construtor de compatibilidade para código existente (sem urlArquivo)
    public CriarApostilaCommand(Integer fkCurso, String nomeApostila, String descricaoApostila, Integer tamanhoBytes) {
        this(fkCurso, nomeApostila, descricaoApostila, tamanhoBytes, null);
    }
}
