package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.projection;

import java.time.LocalDateTime;

/**
 * Flat projection of material_aluno row with raw FK ids to ensure clients can correlate records
 */
public interface MaterialAlunoFlatRow {
    Integer getIdMaterialAluno();
    Integer getFkUsuario();
    Integer getFkCurso();
    Boolean getFinalizada();
    LocalDateTime getUltimoAcesso();
    Integer getIdVideo();     // maps fk_video
    Integer getIdApostila();  // maps fk_apostila
}
