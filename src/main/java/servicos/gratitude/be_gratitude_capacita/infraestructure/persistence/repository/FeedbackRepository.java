package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.FeedbackId;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, FeedbackId> {
    List<FeedbackEntity> findAllByFkCurso(Integer fkCurso);

    @Query(value = "SELECT FK_curso, FK_usuario, estrelas, motivo FROM feedback WHERE FK_curso = :fkCurso", nativeQuery = true)
    List<Object[]> findAllByFkCursoNative(@Param("fkCurso") Integer fkCurso);
}
