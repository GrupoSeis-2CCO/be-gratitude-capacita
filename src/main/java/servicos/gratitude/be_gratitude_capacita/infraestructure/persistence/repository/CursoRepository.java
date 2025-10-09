package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.responses.ParticipanteCursoResponse;

import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<CursoEntity, Integer> {
    Optional<CursoEntity> findByTituloCurso(String tituloCurso);
    Boolean existsByTituloCurso(String titulo);

    @Query("SELECT new servicos.gratitude.be_gratitude_capacita.infraestructure.web.responses.ParticipanteCursoResponse(u.id, u.nome, '0/0' , 'N/A', m.ultimoAcesso) " +
           "FROM Matricula m JOIN m.usuario u " +
           "WHERE m.curso.id = :idCurso")
    List<ParticipanteCursoResponse> findParticipantesByCurso(@Param("idCurso") Long idCurso);
}
