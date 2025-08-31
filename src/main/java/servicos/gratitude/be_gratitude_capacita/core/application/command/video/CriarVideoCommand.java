package servicos.gratitude.be_gratitude_capacita.core.application.command.video;

public record CriarVideoCommand(
        Integer fkCurso,
        String nomeVideo,
        String descricaoVideo,
        String urlVideo
) {
}
