package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;

import java.util.ArrayList;
import java.util.List;

public class FeedbackMapper {
    public static FeedbackEntity toEntity(Feedback feedback){
        FeedbackEntity entity = new FeedbackEntity();

        entity.setFkCurso(feedback.getFkCurso());
        entity.setEstrelas(feedback.getEstrelas());
        entity.setMotivo(feedback.getMotivo());
        entity.setFkUsuario(UsuarioMapper.toEntity(feedback.getFkUsuario()));
    entity.setAnonimo(feedback.getAnonimo());

        return entity;
    }

    public static Feedback toDomain(FeedbackEntity entity){
        Feedback feedback = new Feedback();

        // set minimal curso domain with id only (title can be loaded elsewhere if needed)
        Curso curso = new Curso();
        curso.setIdCurso(entity.getFkCurso());
        feedback.setCurso(curso);

        feedback.setFkCurso(entity.getFkCurso());
        feedback.setEstrelas(entity.getEstrelas());
        feedback.setMotivo(entity.getMotivo());
        feedback.setFkUsuario(UsuarioMapper.toDomain(entity.getFkUsuario()));
    feedback.setAnonimo(entity.getAnonimo());

        return feedback;
    }

    public static List<FeedbackEntity> toEntities(List<Feedback> feedbacks){
        List<FeedbackEntity> entities = new ArrayList<>();

        for (Feedback feedbackDaVez : feedbacks) {
            FeedbackEntity entity = new FeedbackEntity();

            entity.setFkCurso(feedbackDaVez.getFkCurso());
            entity.setEstrelas(feedbackDaVez.getEstrelas());
            entity.setMotivo(feedbackDaVez.getMotivo());
            entity.setFkUsuario(UsuarioMapper.toEntity(feedbackDaVez.getFkUsuario()));
            entity.setAnonimo(feedbackDaVez.getAnonimo());

            entities.add(entity);
        }
        return entities;
    }

    public static List<Feedback> toDomains(List<FeedbackEntity> entities){
        List<Feedback> feedbacks = new ArrayList<>();

        for (FeedbackEntity entityDaVez : entities) {
            Feedback feedback = new Feedback();

            Curso curso = new Curso();
            curso.setIdCurso(entityDaVez.getFkCurso());
            feedback.setCurso(curso);

            feedback.setFkCurso(entityDaVez.getFkCurso());
            feedback.setEstrelas(entityDaVez.getEstrelas());
            feedback.setMotivo(entityDaVez.getMotivo());
            feedback.setFkUsuario(UsuarioMapper.toDomain(entityDaVez.getFkUsuario()));
            feedback.setAnonimo(entityDaVez.getAnonimo());

            feedbacks.add(feedback);
        }
        return feedbacks;
    }
}
