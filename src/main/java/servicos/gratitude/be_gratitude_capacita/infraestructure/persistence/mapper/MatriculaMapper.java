package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MatriculaCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class MatriculaMapper {
    public static MatriculaEntity toEntity(Matricula matricula){
        MatriculaEntity entity = new MatriculaEntity();

        entity.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toEntity(matricula.getIdMatriculaComposto()));
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

        matricula.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toDoamin(entity.getIdMatriculaComposto()));
        matricula.setCurso(CursoMapper.toDomain(entity.getCurso()));
        matricula.setCompleto(entity.getCompleto());
        matricula.setUsuario(UsuarioMapper.toDomain(entity.getUsuario()));
        matricula.setDtInscricao(entity.getDtInscricao());
        matricula.setDataFinalizacao(entity.getDataFinalizacao());
        matricula.setUltimoAcesso(entity.getUltimoAcesso());

        return matricula;
    }

    public static List<MatriculaEntity> toEntities(List<Matricula> matriculas){
        List<MatriculaEntity> entities = new ArrayList<>();

        for (Matricula matriculaDaVez : matriculas) {
        MatriculaEntity entity = new MatriculaEntity();

        entity.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toEntity(matriculaDaVez.getIdMatriculaComposto()));
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

        matricula.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toDoamin(entityDaVez.getIdMatriculaComposto()));
        matricula.setCurso(CursoMapper.toDomain(entityDaVez.getCurso()));
        matricula.setCompleto(entityDaVez.getCompleto());
        matricula.setUsuario(UsuarioMapper.toDomain(entityDaVez.getUsuario()));
        matricula.setDtInscricao(entityDaVez.getDtInscricao());
        matricula.setDataFinalizacao(entityDaVez.getDataFinalizacao());
        matricula.setUltimoAcesso(entityDaVez.getUltimoAcesso());

        matriculas.add(matricula);
        }

        return matriculas;
    }
}
