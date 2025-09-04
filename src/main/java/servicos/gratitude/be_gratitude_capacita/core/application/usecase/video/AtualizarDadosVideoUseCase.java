package servicos.gratitude.be_gratitude_capacita.core.application.usecase.video;

import servicos.gratitude.be_gratitude_capacita.core.application.command.video.AtualizarVideoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;
import servicos.gratitude.be_gratitude_capacita.core.gateways.VideoGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class AtualizarDadosVideoUseCase {
    private final VideoGateway videoGateway;

    public AtualizarDadosVideoUseCase(VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    public Video execute(AtualizarVideoCommand command, Integer idVideo){
        if (!videoGateway.existsById(idVideo)){
            throw new NaoEncontradoException("Não foi encontrado um vídeo com o id informado");
        }

        Video videoComMesmaUrl = videoGateway.findByUrl(command.urlVideo());
        if (Objects.nonNull(videoComMesmaUrl) && videoComMesmaUrl.getIdVideo() != idVideo){
            throw new ConflitoException("Já existe um vídeo com essa URL");
        }

        Video videoComMesmoTitulo = videoGateway.findByNome(command.nomeVideo());
        if (Objects.nonNull(videoComMesmoTitulo) && videoComMesmoTitulo.getIdVideo() != idVideo){
            throw new ConflitoException("Já existe um vídeo com esse nome");
        }

        Video videoDoBanco = videoGateway.findById(idVideo);
        Video videoAtualizado = new Video();

        videoAtualizado.setUrlVideo(command.urlVideo());
        videoAtualizado.setNomeVideo(command.nomeVideo());
        videoAtualizado.setDescricaoVideo(command.descricaoVideo());
        videoAtualizado.setOrdemVideo(command.ordemVideo());

        videoAtualizado.setIdVideo(idVideo);
        videoAtualizado.setVideoOculto(videoDoBanco.getVideoOculto());
        videoAtualizado.setFkCurso(videoDoBanco.getFkCurso());
        videoAtualizado.setDataPostadoVideo(videoDoBanco.getDataPostadoVideo());
        videoAtualizado.setDataAtualizacaoVideo(LocalDateTime.now());

        return videoGateway.save(videoAtualizado);
    }
}
