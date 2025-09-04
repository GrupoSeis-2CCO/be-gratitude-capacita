package servicos.gratitude.be_gratitude_capacita.core.application.usecase.video;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;
import servicos.gratitude.be_gratitude_capacita.core.gateways.VideoGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class AtualizarOcultoVideoUseCase {
    private final VideoGateway videoGateway;

    public AtualizarOcultoVideoUseCase(VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    public Video execute(Integer idVideo){
        Video videoParaAtualizar = videoGateway.findById(idVideo);

        if (Objects.isNull(videoParaAtualizar)){
            throw new NaoEncontradoException("Não foi encontrado um vídeo com o id informado");
        }
        videoParaAtualizar.setVideoOculto(!videoParaAtualizar.getVideoOculto());
        videoParaAtualizar.setDataAtualizacaoVideo(LocalDateTime.now());

        return videoGateway.save(videoParaAtualizar);
    }
}
