package servicos.gratitude.be_gratitude_capacita.core.application.command.apostila;

public record CriarApostilaCommand(
         Integer fkCurso,
         String nomeApostila,
         String descricaoApostila,
         Integer tamanhoBytes
) {
}
