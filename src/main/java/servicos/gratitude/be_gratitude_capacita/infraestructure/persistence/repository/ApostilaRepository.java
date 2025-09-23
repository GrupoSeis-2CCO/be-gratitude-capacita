package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;

import java.util.List;
import java.util.Optional;

public interface ApostilaRepository extends JpaRepository<ApostilaEntity, Integer> {
    List<ApostilaEntity> findAllByFkCurso(CursoEntity fkCurso);

    Page<ApostilaEntity> findAllByFkCurso(CursoEntity fkCurso, Pageable pageable);

    Boolean existsByNomeApostilaOriginal(String nome);

    Optional<ApostilaEntity> findByNomeApostilaOriginal(String nome);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE ApostilaEntity a SET a.urlArquivo = :url WHERE a.idApostila = :id")
    int atualizarUrlArquivo(@org.springframework.data.repository.query.Param("id") Integer id,
            @org.springframework.data.repository.query.Param("url") String url);
}
