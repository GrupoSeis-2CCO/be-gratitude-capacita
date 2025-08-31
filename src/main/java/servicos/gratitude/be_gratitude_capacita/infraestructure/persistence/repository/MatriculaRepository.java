package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.MatriculaCompoundKey;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<MatriculaEntity, MatriculaCompoundKey> {
    List<MatriculaEntity> findAllByUsuario(UsuarioEntity usuario);
    List<MatriculaEntity> findAllByCurso(CursoEntity curso);
    List<MatriculaEntity> findAllByIsCompleto(Boolean isFinalizada);
}
