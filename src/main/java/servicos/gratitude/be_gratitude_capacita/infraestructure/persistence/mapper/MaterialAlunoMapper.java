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
        entity.setMatricula(MatriculaMapper.toEntity(materialAluno.getMatricula()));
        entity.setFkVideo(VideoMapper.toEntity(materialAluno.getFkVideo()));
        entity.setFkApostila(ApostilaMapper.toEntity(materialAluno.getFkApostila()));

        return entity;
    }

    public static MaterialAluno toDomain(MaterialAlunoEntity entity){
        MaterialAluno materialAluno = new MaterialAluno();

        materialAluno.setIdMaterialAlunoComposto(MaterialAlunoCompoundKeyMapper.toDomain(entity.getIdMaterialAlunoComposto()));
        materialAluno.setUltimoAcesso(entity.getUltimoAcesso());
        materialAluno.setFinalizado(entity.getFinalizado());
        materialAluno.setMatricula(MatriculaMapper.toDomain(entity.getMatricula()));
    // Avoid initializing lazy proxies for Video/Apostila here —
    // the participants endpoint only needs finalizado/ultimoAcesso.
    // Mapping the full Video/Apostila would trigger a DB load and
    // may cause SQL errors if the schema differs. Set to null for now.
    materialAluno.setFkVideo(null);
    materialAluno.setFkApostila(null);

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
        materialAluno.setMatricula(MatriculaMapper.toDomain(entityDaVez.getMatricula()));
    // Avoid initializing lazy proxies when converting lists — keep lightweight.
    materialAluno.setFkVideo(null);
    materialAluno.setFkApostila(null);

        materiaisAluno.add(materialAluno);
        }

        return materiaisAluno;
    }
}
