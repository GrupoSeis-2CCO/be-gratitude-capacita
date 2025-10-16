package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.TentativaEntityCompoundKey;

import java.util.List;

public interface TentativaRepository extends JpaRepository<TentativaEntity, TentativaEntityCompoundKey> {
    @Query("select t from TentativaEntity t join fetch t.matricula m join fetch m.usuario join fetch m.curso where t.matricula = :matricula")
    List<TentativaEntity> findAllByMatricula(@Param("matricula") MatriculaEntity matricula);
    /**
     * Return tentativas for a given usuario by joining with matricula. Using an explicit join
     * guarantees we only return tentativa rows that reference an existing matricula entity
     * (avoids EntityNotFoundException when there are dangling foreign keys).
     */
    // Use the embedded key path to filter by usuario without joining the Matricula entity.
    @Query("select t from TentativaEntity t join fetch t.matricula m join fetch m.usuario join fetch m.curso where t.idTentativaComposto.idMatriculaComposto.fkUsuario = :fkUsuario")
    java.util.List<TentativaEntity> findAllByUsuario(@Param("fkUsuario") Integer fkUsuario);
}
