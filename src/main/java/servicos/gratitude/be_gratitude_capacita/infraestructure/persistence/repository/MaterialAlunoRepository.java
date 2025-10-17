package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MaterialAlunoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MaterialAlunoEntityCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.projection.MaterialAlunoFlatRow;

import java.util.List;
import java.util.Optional;

public interface MaterialAlunoRepository extends JpaRepository<MaterialAlunoEntity, MaterialAlunoEntityCompoundKey> {
    // Eagerly fetch fkVideo and fkApostila so their IDs are available without triggering LazyInitialization
    @EntityGraph(attributePaths = {"fkVideo", "fkApostila"})
    List<MaterialAlunoEntity> findAllByMatricula(MatriculaEntity matricula);

    @EntityGraph(attributePaths = {"fkVideo", "fkApostila"})
    List<MaterialAlunoEntity> findAllByMatriculaAndIsFinalizado(MatriculaEntity matricula, Boolean isFinalizado);

    // Override findById to eagerly fetch associations
    @Override
    @EntityGraph(attributePaths = {"fkVideo", "fkApostila"})
    Optional<MaterialAlunoEntity> findById(MaterialAlunoEntityCompoundKey id);
    // delete all material_aluno rows that reference a given apostila
    void deleteAllByFkApostila(ApostilaEntity fkApostila);
    // delete all material_aluno rows that reference a given video
    void deleteAllByFkVideo(VideoEntity fkVideo);

    // Fallback flat listing providing raw fk ids even if associations are null due to mapping
    @Query(value = "SELECT id_material_aluno AS idMaterialAluno, fk_usuario AS fkUsuario, fk_curso AS fkCurso, finalizada AS finalizada, ultimo_acesso AS ultimoAcesso, fk_video AS idVideo, fk_apostila AS idApostila FROM material_aluno WHERE fk_usuario = :fkUsuario AND fk_curso = :fkCurso", nativeQuery = true)
    List<MaterialAlunoFlatRow> listFlatByMatricula(@Param("fkUsuario") Integer fkUsuario, @Param("fkCurso") Integer fkCurso);
}
