package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MaterialAlunoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.MaterialAlunoCompoundKey;

import java.util.List;

public interface MaterialAlunoRepository extends JpaRepository<MaterialAlunoRepository, MaterialAlunoCompoundKey> {
    List<MaterialAlunoEntity> findAllByMatricula(MatriculaEntity matricula);
    List<MaterialAlunoEntity> findAllByMatriculaAndIsFinalizado(MatriculaEntity matricula, Boolean isFinalizado);
}
