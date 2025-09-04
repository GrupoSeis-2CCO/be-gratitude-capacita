package servicos.gratitude.be_gratitude_capacita.core.application.usecase.video;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.gateways.VideoGateway;

import java.util.Objects;

public class DeletarVideoUseCase {
    private final VideoGateway videoGateway;

    public DeletarVideoUseCase(VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    public void execute(Integer idVideo){
        if (!videoGateway.existsById(idVideo)){
            throw new NaoEncontradoException("Não foi encontrado um vídeo com o id informado");
        }

        videoGateway.deleteById(idVideo);
    }
}
