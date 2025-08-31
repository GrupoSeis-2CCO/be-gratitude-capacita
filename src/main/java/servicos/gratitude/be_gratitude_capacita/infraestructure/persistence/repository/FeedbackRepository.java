package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, CursoEntity> {
    List<FeedbackEntity> findAllByFkCurso(CursoEntity fkCurso);
}
