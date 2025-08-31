package servicos.gratitude.be_gratitude_capacita.core.application.command.alternativa;

public record CriarAlternativaCommand(
        Integer fkQuestao,
        Integer fkAvaliacao,
        String texto
) {
}
