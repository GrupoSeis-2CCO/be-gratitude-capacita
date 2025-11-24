package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import java.util.Optional;

public interface CursoRepository extends JpaRepository <CursoEntity, Integer>{
    Optional<CursoEntity> findByTituloCurso(String tituloCurso);
    Boolean existsByTituloCurso(String titulo);
    // Para listagem ordenada
    java.util.List<CursoEntity> findAllByOrderByOrdemCursoAsc();
}
