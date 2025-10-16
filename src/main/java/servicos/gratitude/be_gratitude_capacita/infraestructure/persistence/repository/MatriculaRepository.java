package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<MatriculaEntity, MatriculaEntityCompoundKey> {
    @Query("SELECT m FROM MatriculaEntity m JOIN FETCH m.curso WHERE m.usuario = :usuario")
    List<MatriculaEntity> findAllByUsuario(@Param("usuario") UsuarioEntity usuario);
    
    List<MatriculaEntity> findAllByCurso(CursoEntity curso);
    List<MatriculaEntity> findAllByIsCompleto(Boolean isFinalizada);
}
