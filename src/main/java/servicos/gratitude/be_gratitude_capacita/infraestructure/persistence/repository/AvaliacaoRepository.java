package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AvaliacaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;

import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Integer> {
    Boolean existsByFkCurso(CursoEntity cursoEntity);
    java.util.List<AvaliacaoEntity> findAllByFkCurso(CursoEntity cursoEntity);
    Optional<AvaliacaoEntity> findFirstByFkCurso_IdCurso(Long idCurso);
    // findAll já existe por herdar de JpaRepository
}
