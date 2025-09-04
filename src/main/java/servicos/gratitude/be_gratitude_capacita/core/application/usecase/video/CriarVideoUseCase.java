package servicos.gratitude.be_gratitude_capacita.core.application.usecase.video;

import servicos.gratitude.be_gratitude_capacita.core.application.command.video.CriarVideoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.VideoGateway;

import java.time.LocalDateTime;
import java.util.List;

public class CriarVideoUseCase {
    private final VideoGateway videoGateway;
    private final CursoGateway cursoGateway;

    public CriarVideoUseCase(VideoGateway videoGateway, CursoGateway cursoGateway) {
        this.videoGateway = videoGateway;
        this.cursoGateway = cursoGateway;
    }

    public Video execute(CriarVideoCommand command){
        if (videoGateway.existsByNome(command.nomeVideo())){
            throw new ConflitoException("Já existe um vídeo com o nome informado");
        } else if (videoGateway.existsByUrl(command.urlVideo())){
            throw new ConflitoException("Já existe um vídeo com a url informada");
        } else if (!cursoGateway.existsById(command.fkCurso())) {
            throw new NaoEncontradoException("Não foi encontrado um vídeo com o id informado");
        }

        Curso curso = cursoGateway.findById(command.fkCurso());

        List<Video> videosDoCurso = videoGateway.findAllByCurso(curso);
        Integer maiorOrdem = 0;

        for (Video videoDaVez : videosDoCurso) {
            if (videoDaVez.getOrdemVideo() > maiorOrdem){
                maiorOrdem = videoDaVez.getOrdemVideo();
            }
        }

        Video video = new Video();
        video.setDescricaoVideo(command.descricaoVideo());
        video.setNomeVideo(command.nomeVideo());
        video.setUrlVideo(command.urlVideo());
        video.setDataPostadoVideo(LocalDateTime.now());
        video.setDataAtualizacaoVideo(LocalDateTime.now());
        video.setFkCurso(curso);
        video.setVideoOculto(true);
        video.setOrdemVideo(maiorOrdem);

        return videoGateway.save(video);
    }
}
