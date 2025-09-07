package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;

import java.util.List;
import java.util.Optional;

public interface ApostilaRepository extends JpaRepository<ApostilaEntity, Integer> {
    List<ApostilaEntity> findAllByFkCurso(CursoEntity fkCurso);
    Boolean existsByNomeApostilaOriginal(String nome);
    Optional<ApostilaEntity> findByNomeApostilaOriginal(String nome);
}
