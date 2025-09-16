package servicos.gratitude.be_gratitude_capacita.core.application.command.questao;

public record CriarQuestaoCommand(
        Integer fkAvaliacao,
        Integer numeroQuestao,
        String enunciado,
        Boolean oculto
) {}