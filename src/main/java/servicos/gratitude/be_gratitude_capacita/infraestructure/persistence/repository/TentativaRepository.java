package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.TentativaCompoundKey;

import java.util.List;

public interface TentativaRepository extends JpaRepository<TentativaEntity, TentativaCompoundKey> {
    List<TentativaEntity> findAllByMatricula(MatriculaEntity matricula);
}
