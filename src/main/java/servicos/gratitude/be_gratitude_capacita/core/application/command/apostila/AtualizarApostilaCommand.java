package servicos.gratitude.be_gratitude_capacita.core.application.command.apostila;

public record AtualizarApostilaCommand(
        String nomeApostila,
        String descricaoApostila,
        Integer ordem
) {
}
