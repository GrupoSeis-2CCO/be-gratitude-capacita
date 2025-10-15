package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    List<VideoEntity> findAllByFkCurso(CursoEntity fkCurso);
    Boolean existsByUrlVideo(String url);
    Boolean existsByNomeVideo(String nome);
    Optional<VideoEntity> findByUrlVideo(String url);
    // allow multiple results in DB; adapter will pick the first if duplicates exist
    List<VideoEntity> findAllByNomeVideo(String nome);
}
