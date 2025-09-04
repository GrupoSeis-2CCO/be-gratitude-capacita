package servicos.gratitude.be_gratitude_capacita.core.application.usecase.video;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.VideoGateway;

import java.util.List;

public class ListarVideoPorCursoUseCase {
    private final VideoGateway videoGateway;
    private final CursoGateway cursoGateway;

    public ListarVideoPorCursoUseCase(VideoGateway videoGateway, CursoGateway cursoGateway) {
        this.videoGateway = videoGateway;
        this.cursoGateway = cursoGateway;
    }

    public List<Video> execute(Integer fkCurso){
        if (!cursoGateway.existsById(fkCurso)){
            throw new NaoEncontradoException("Não foi encontrado um curso com o id informado");
        }

        Curso curso = cursoGateway.findById(fkCurso);
        return videoGateway.findAllByCurso(curso);
    }
}
