package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.TentativaEntityCompoundKey;

import java.util.List;

public interface TentativaRepository extends JpaRepository<TentativaEntity, TentativaEntityCompoundKey> {
    List<TentativaEntity> findAllByMatricula(MatriculaEntity matricula);
}
