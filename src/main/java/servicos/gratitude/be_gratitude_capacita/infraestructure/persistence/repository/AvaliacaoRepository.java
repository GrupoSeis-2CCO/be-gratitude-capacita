package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AvaliacaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Integer> {
    Boolean existsByFkCurso(CursoEntity cursoEntity);
}
