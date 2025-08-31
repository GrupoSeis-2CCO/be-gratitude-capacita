package servicos.gratitude.be_gratitude_capacita.core.application.command.video;

public record AtualizarVideoCommand(
         String nomeVideo,
         String descricaoVideo,
         String urlVideo,
         Integer ordemVideo
) {
}
