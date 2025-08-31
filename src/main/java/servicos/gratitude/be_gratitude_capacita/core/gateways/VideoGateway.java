package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;

import java.util.List;

public interface VideoGateway {
    Video save(Video video);
    List<Video> findAllByCurso(Curso curso);
    Video findById(Integer id);
    Boolean existsById(Integer id);
    void deleteById(Integer id);
}
