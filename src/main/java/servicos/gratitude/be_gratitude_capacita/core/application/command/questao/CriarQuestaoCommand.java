package servicos.gratitude.be_gratitude_capacita.core.application.command.questao;

public record CriarQuestaoCommand(
        Integer fkAvaliacao,
        String enunciado,
        Integer numeroQuestao
) {
}
