package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity;

import java.util.List;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    List<VideoEntity> findAllByFkCurso(CursoEntity fkCurso);
}
