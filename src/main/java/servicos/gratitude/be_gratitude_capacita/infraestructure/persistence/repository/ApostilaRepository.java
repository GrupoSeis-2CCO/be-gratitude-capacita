package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;

import java.util.List;

public interface ApostilaRepository extends JpaRepository<ApostilaEntity, Integer> {
    List<ApostilaEntity> findAllByFkCurso(CursoRepository fkCurso);
}
