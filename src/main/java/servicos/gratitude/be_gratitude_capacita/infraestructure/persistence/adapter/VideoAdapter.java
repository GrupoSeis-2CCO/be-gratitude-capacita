package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;
import servicos.gratitude.be_gratitude_capacita.core.gateways.VideoGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.UsuarioMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.VideoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.VideoRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MaterialAlunoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VideoAdapter implements VideoGateway {
    private final VideoRepository videoRepository;
    private final MaterialAlunoRepository materialAlunoRepository;

    public VideoAdapter(VideoRepository videoRepository, MaterialAlunoRepository materialAlunoRepository) {
        this.videoRepository = videoRepository;
        this.materialAlunoRepository = materialAlunoRepository;
    }

    @Override
    public Video save(Video video) {
        VideoEntity entity = VideoMapper.toEntity(video);

        return VideoMapper.toDomain(videoRepository.save(entity));
    }

    @Override
    public Boolean existsByUrl(String url) {
        return videoRepository.existsByUrlVideo(url);
    }

    @Override
    public Boolean existsByNome(String nome) {
        return videoRepository.existsByNomeVideo(nome);
    }

    @Override
    public Video findByUrl(String url) {
        Optional<VideoEntity> entity = videoRepository.findByUrlVideo(url);

        return entity.map(VideoMapper::toDomain).orElse(null);
    }

    @Override
    public Video findByNome(String nome) {
        Optional<VideoEntity> entity = videoRepository.findByNomeVideo(nome);

        return entity.map(VideoMapper::toDomain).orElse(null);
    }

    @Override
    public List<Video> findAllByCurso(Curso curso) {
        return VideoMapper.toDomains(videoRepository.findAllByFkCurso(CursoMapper.toEntity(curso)));
    }

    @Override
    public Video findById(Integer id) {
        Optional<VideoEntity> entity = videoRepository.findById(id);

        return entity.map(VideoMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(Integer id) {
        return videoRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        // remove material_aluno rows that reference this video to avoid FK constraint
        servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity ve = new servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity();
        ve.setIdVideo(id);
        materialAlunoRepository.deleteAllByFkVideo(ve);

        videoRepository.deleteById(id);
    }
}
