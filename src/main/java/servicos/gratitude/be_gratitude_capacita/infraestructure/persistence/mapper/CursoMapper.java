package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CursoMapper {

    public static CursoEntity toEntity(Curso curso){

        if (Objects.isNull(curso)){
            return null;
        }

        CursoEntity entity = new CursoEntity();
        entity.setTituloCurso(curso.getTituloCurso());
        entity.setDescricao(curso.getDescricao());
        entity.setImagem(curso.getImagem());
        entity.setDuracaoEstimada(curso.getDuracaoEstimada());
        entity.setOcultado(curso.getOcultado());
        entity.setIdCurso(curso.getIdCurso());

        return entity;
    }

    public static Curso toDomain(CursoEntity entity){

        if (Objects.isNull(entity)){
            return null;
        }

        Curso curso = new Curso();
        curso.setTituloCurso(entity.getTituloCurso());
        curso.setDescricao(entity.getDescricao());
        curso.setImagem(entity.getImagem());
        curso.setDuracaoEstimada(entity.getDuracaoEstimada());
        curso.setOcultado(entity.getOcultado());
        curso.setIdCurso(entity.getIdCurso());

        return curso;
    }

    public static List<CursoEntity> toEntities(List<Curso> cursos){

        if (Objects.isNull(cursos)){
            return null;
        }

        List<CursoEntity> entities = new ArrayList<>();

        for (Curso cursoDaVez : cursos) {
            CursoEntity entity = new CursoEntity();

            entity.setTituloCurso(cursoDaVez.getTituloCurso());
            entity.setDescricao(cursoDaVez.getDescricao());
            entity.setImagem(cursoDaVez.getImagem());
            entity.setDuracaoEstimada(cursoDaVez.getDuracaoEstimada());
            entity.setOcultado(cursoDaVez.getOcultado());
            entity.setIdCurso(cursoDaVez.getIdCurso());

            entities.add(entity);
        }

        return entities;
    }

    public static List<Curso> toDomains(List<CursoEntity> entities){

        if (Objects.isNull(entities)){
            return null;
        }

        List<Curso> cursos = new ArrayList<>();

        for (CursoEntity entityDaVez : entities) {
            Curso curso = new Curso();

            curso.setTituloCurso(entityDaVez.getTituloCurso());
            curso.setDescricao(entityDaVez.getDescricao());
            curso.setImagem(entityDaVez.getImagem());
            curso.setDuracaoEstimada(entityDaVez.getDuracaoEstimada());
            curso.setOcultado(entityDaVez.getOcultado());
            curso.setIdCurso(entityDaVez.getIdCurso());

            cursos.add(curso);
        }

        return cursos;
    }
}
