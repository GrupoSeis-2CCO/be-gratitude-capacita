package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MaterialAlunoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MaterialAlunoCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class MaterialAlunoMapper {
    public static MaterialAlunoEntity toEntity(MaterialAluno materialAluno){
        MaterialAlunoEntity entity = new MaterialAlunoEntity();

        entity.setIdMaterialAlunoComposto(MaterialAlunoCompoundKeyMapper.toEntity(materialAluno.getIdMaterialAlunoComposto()));
        entity.setUltimoAcesso(materialAluno.getUltimoAcesso());
        entity.setFinalizado(materialAluno.getFinalizado());
        // Use key-only mapping to avoid touching lazy relations (usuario/curso) that may be null in domain
        entity.setMatricula(MatriculaMapper.toEntityKeyOnly(materialAluno.getMatricula()));
        entity.setFkVideo(VideoMapper.toEntity(materialAluno.getFkVideo()));
        entity.setFkApostila(ApostilaMapper.toEntity(materialAluno.getFkApostila()));

        return entity;
    }

    public static MaterialAluno toDomain(MaterialAlunoEntity entity){
        MaterialAluno materialAluno = new MaterialAluno();

        materialAluno.setIdMaterialAlunoComposto(MaterialAlunoCompoundKeyMapper.toDomain(entity.getIdMaterialAlunoComposto()));
        materialAluno.setUltimoAcesso(entity.getUltimoAcesso());
        materialAluno.setFinalizado(entity.getFinalizado());
    // Map only matricula key to avoid initializing lazy relations (curso/usuario)
    materialAluno.setMatricula(MatriculaMapper.toDomainKeyOnly(entity.getMatricula()));

        // Map only the identifiers of associations to avoid heavy lazy initialization.
        // This keeps listar-por-matricula usable by the frontend to match materials.
        try {
            if (entity.getFkVideo() != null) {
                Integer id = entity.getFkVideo().getIdVideo();
                materialAluno.setIdVideo(id);
                servicos.gratitude.be_gratitude_capacita.core.domain.Video v = new servicos.gratitude.be_gratitude_capacita.core.domain.Video();
                v.setIdVideo(id);
                materialAluno.setFkVideo(v);
            } else {
                materialAluno.setFkVideo(null);
                materialAluno.setIdVideo(null);
            }
        } catch (Exception ignored) {
            materialAluno.setFkVideo(null);
            materialAluno.setIdVideo(null);
        }

        try {
            if (entity.getFkApostila() != null) {
                Integer id = entity.getFkApostila().getIdApostila();
                materialAluno.setIdApostila(id);
                servicos.gratitude.be_gratitude_capacita.core.domain.Apostila a = new servicos.gratitude.be_gratitude_capacita.core.domain.Apostila();
                a.setIdApostila(id);
                materialAluno.setFkApostila(a);
            } else {
                materialAluno.setFkApostila(null);
                materialAluno.setIdApostila(null);
            }
        } catch (Exception ignored) {
            materialAluno.setFkApostila(null);
            materialAluno.setIdApostila(null);
        }

        return materialAluno;
    }

    /**
     * Map an entity to domain and include associated Video/Apostila (may trigger lazy loads).
     * Use when a full representation is required (for example: returning the freshly saved record).
     */
    public static MaterialAluno toDomainWithAssociations(MaterialAlunoEntity entity){
        MaterialAluno materialAluno = new MaterialAluno();

        materialAluno.setIdMaterialAlunoComposto(MaterialAlunoCompoundKeyMapper.toDomain(entity.getIdMaterialAlunoComposto()));
        materialAluno.setUltimoAcesso(entity.getUltimoAcesso());
        materialAluno.setFinalizado(entity.getFinalizado());
    // Avoid touching lazy relations inside Matricula; only map the key
    materialAluno.setMatricula(MatriculaMapper.toDomainKeyOnly(entity.getMatricula()));

        // Map associations fully
        try {
            materialAluno.setFkVideo(VideoMapper.toDomain(entity.getFkVideo()));
            materialAluno.setIdVideo(entity.getFkVideo() != null ? entity.getFkVideo().getIdVideo() : null);
        } catch (Exception ignored) {
            materialAluno.setFkVideo(null);
            materialAluno.setIdVideo(null);
        }

        try {
            materialAluno.setFkApostila(ApostilaMapper.toDomain(entity.getFkApostila()));
            materialAluno.setIdApostila(entity.getFkApostila() != null ? entity.getFkApostila().getIdApostila() : null);
        } catch (Exception ignored) {
            materialAluno.setFkApostila(null);
            materialAluno.setIdApostila(null);
        }

        return materialAluno;
    }

    public static List<MaterialAlunoEntity> toEntities(List<MaterialAluno> materiaisAluno){
        List<MaterialAlunoEntity> entities = new ArrayList<>();

        for (MaterialAluno materialAlunoDaVez : materiaisAluno) {
        MaterialAlunoEntity entity = new MaterialAlunoEntity();

        entity.setIdMaterialAlunoComposto(MaterialAlunoCompoundKeyMapper.toEntity(materialAlunoDaVez.getIdMaterialAlunoComposto()));
        entity.setUltimoAcesso(materialAlunoDaVez.getUltimoAcesso());
        entity.setFinalizado(materialAlunoDaVez.getFinalizado());
        entity.setMatricula(MatriculaMapper.toEntity(materialAlunoDaVez.getMatricula()));
        entity.setFkVideo(VideoMapper.toEntity(materialAlunoDaVez.getFkVideo()));
        entity.setFkApostila(ApostilaMapper.toEntity(materialAlunoDaVez.getFkApostila()));

        entities.add(entity);
        }

        return entities;
    }

    public static List<MaterialAluno> toDomains(List<MaterialAlunoEntity> entities){
        List<MaterialAluno> materiaisAluno = new ArrayList<>();

        for (MaterialAlunoEntity entityDaVez : entities) {
            MaterialAluno materialAluno = new MaterialAluno();

            materialAluno.setIdMaterialAlunoComposto(MaterialAlunoCompoundKeyMapper.toDomain(entityDaVez.getIdMaterialAlunoComposto()));
            materialAluno.setUltimoAcesso(entityDaVez.getUltimoAcesso());
            materialAluno.setFinalizado(entityDaVez.getFinalizado());
            // Only map the matricula key to prevent lazy init outside transactions
            materialAluno.setMatricula(MatriculaMapper.toDomainKeyOnly(entityDaVez.getMatricula()));

            // Map only IDs for associations to keep payload light yet informative
            try {
                if (entityDaVez.getFkVideo() != null) {
                    Integer id = entityDaVez.getFkVideo().getIdVideo();
                    materialAluno.setIdVideo(id);
                    servicos.gratitude.be_gratitude_capacita.core.domain.Video v = new servicos.gratitude.be_gratitude_capacita.core.domain.Video();
                    v.setIdVideo(id);
                    materialAluno.setFkVideo(v);
                } else {
                    materialAluno.setIdVideo(null);
                }
            } catch (Exception ignored) { materialAluno.setIdVideo(null); }

            try {
                if (entityDaVez.getFkApostila() != null) {
                    Integer id = entityDaVez.getFkApostila().getIdApostila();
                    materialAluno.setIdApostila(id);
                    servicos.gratitude.be_gratitude_capacita.core.domain.Apostila a = new servicos.gratitude.be_gratitude_capacita.core.domain.Apostila();
                    a.setIdApostila(id);
                    materialAluno.setFkApostila(a);
                } else {
                    materialAluno.setIdApostila(null);
                }
            } catch (Exception ignored) { materialAluno.setIdApostila(null); }

            materiaisAluno.add(materialAluno);
        }

        return materiaisAluno;
    }
}
