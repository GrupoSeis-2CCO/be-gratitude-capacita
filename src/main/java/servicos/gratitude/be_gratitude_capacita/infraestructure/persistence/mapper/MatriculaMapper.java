package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MatriculaCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class MatriculaMapper {
    public static MatriculaEntity toEntity(Matricula matricula){
        MatriculaEntity entity = new MatriculaEntity();

    entity.setId(MatriculaCompoundKeyMapper.toEntity(matricula.getIdMatriculaComposto()));
        entity.setCurso(CursoMapper.toEntity(matricula.getCurso()));
        entity.setCompleto(matricula.getCompleto());
        entity.setUsuario(UsuarioMapper.toEntity(matricula.getUsuario()));
        entity.setDtInscricao(matricula.getDtInscricao());
        entity.setDataFinalizacao(matricula.getDataFinalizacao());
        entity.setUltimoAcesso(matricula.getUltimoAcesso());

        return entity;
    }

    public static Matricula toDomain(MatriculaEntity entity){
        Matricula matricula = new Matricula();

    matricula.setIdMatriculaComposto(entity.getId() != null ? MatriculaCompoundKeyMapper.toDoamin(entity.getId()) : null);
        matricula.setCurso(entity.getCurso() != null ? CursoMapper.toDomain(entity.getCurso()) : null);
        matricula.setCompleto(entity.getCompleto());
        matricula.setUsuario(entity.getUsuario() != null ? UsuarioMapper.toDomain(entity.getUsuario()) : null);
        matricula.setDtInscricao(entity.getDtInscricao());
        matricula.setDataFinalizacao(entity.getDataFinalizacao());
        matricula.setUltimoAcesso(entity.getUltimoAcesso());

        return matricula;
    }

    /**
     * Shallow mapping that avoids touching lazy relations (curso/usuario).
     * Use this when the JPA session may be closed to prevent LazyInitializationException.
     */
    public static Matricula toDomainKeyOnly(MatriculaEntity entity){
        Matricula matricula = new Matricula();
        // Only copy the ID (compound key) and DO NOT touch any other property that could be lazy
        matricula.setIdMatriculaComposto(entity.getId() != null ? MatriculaCompoundKeyMapper.toDoamin(entity.getId()) : null);
        // Optionally, you can copy primitive fields that are always loaded (not relations)
        // But avoid entity.getCompleto(), getUsuario(), getCurso(), etc.
        return matricula;
    }

    /**
     * Build a MatriculaEntity with only the embedded id filled.
     * Use this when you only have the composite key and don't want to touch relations (usuario/curso).
     * Typically, the caller should later replace the association with an EntityManager.getReference.
     */
    public static MatriculaEntity toEntityKeyOnly(Matricula matricula){
        if (matricula == null) return null;
        MatriculaEntity entity = new MatriculaEntity();
        entity.setId(MatriculaCompoundKeyMapper.toEntity(matricula.getIdMatriculaComposto()));
        // Do NOT set usuario/curso here to avoid NPEs when domain only carries keys
        return entity;
    }

    public static List<MatriculaEntity> toEntities(List<Matricula> matriculas){
        List<MatriculaEntity> entities = new ArrayList<>();

        for (Matricula matriculaDaVez : matriculas) {
        MatriculaEntity entity = new MatriculaEntity();

    entity.setId(MatriculaCompoundKeyMapper.toEntity(matriculaDaVez.getIdMatriculaComposto()));
        entity.setCurso(CursoMapper.toEntity(matriculaDaVez.getCurso()));
        entity.setCompleto(matriculaDaVez.getCompleto());
        entity.setUsuario(UsuarioMapper.toEntity(matriculaDaVez.getUsuario()));
        entity.setDtInscricao(matriculaDaVez.getDtInscricao());
        entity.setDataFinalizacao(matriculaDaVez.getDataFinalizacao());
        entity.setUltimoAcesso(matriculaDaVez.getUltimoAcesso());

        entities.add(entity);
        }

        return entities;
    }

    public static List<Matricula> toDomains(List<MatriculaEntity> entities){
        List<Matricula> matriculas = new ArrayList<>();

        for (MatriculaEntity entityDaVez : entities) {
        Matricula matricula = new Matricula();

    matricula.setIdMatriculaComposto(entityDaVez.getId() != null ? MatriculaCompoundKeyMapper.toDoamin(entityDaVez.getId()) : null);
        matricula.setCurso(entityDaVez.getCurso() != null ? CursoMapper.toDomain(entityDaVez.getCurso()) : null);
        matricula.setCompleto(entityDaVez.getCompleto());
        matricula.setUsuario(entityDaVez.getUsuario() != null ? UsuarioMapper.toDomain(entityDaVez.getUsuario()) : null);
        matricula.setDtInscricao(entityDaVez.getDtInscricao());
        matricula.setDataFinalizacao(entityDaVez.getDataFinalizacao());
        matricula.setUltimoAcesso(entityDaVez.getUltimoAcesso());

        matriculas.add(matricula);
        }

        return matriculas;
    }
}
