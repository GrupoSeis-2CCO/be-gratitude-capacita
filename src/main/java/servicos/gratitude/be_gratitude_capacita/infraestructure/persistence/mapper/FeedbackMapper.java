package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;

import java.util.ArrayList;
import java.util.List;

public class FeedbackMapper {
    public static FeedbackEntity toEntity(Feedback feedback){
        FeedbackEntity entity = new FeedbackEntity();
        
        entity.setCurso(CursoMapper.toEntity(feedback.getCurso()));
        entity.setFkCurso(feedback.getFkCurso());
        entity.setEstrelas(feedback.getEstrelas());
        entity.setMotivo(feedback.getMotivo());
        entity.setFkUsuario(UsuarioMapper.toEntity(feedback.getFkUsuario()));
        
        return entity;
    }

    public static Feedback toDomain(FeedbackEntity entity){
        Feedback feedback = new Feedback();

        feedback.setCurso(CursoMapper.toDomain(entity.getCurso()));
        feedback.setFkCurso(entity.getFkCurso());
        feedback.setEstrelas(entity.getEstrelas());
        feedback.setMotivo(entity.getMotivo());
        feedback.setFkUsuario(UsuarioMapper.toDomain(entity.getFkUsuario()));

        return feedback;
    }

    public static List<FeedbackEntity> toEntities(List<Feedback> feedbacks){
        List<FeedbackEntity> entities = new ArrayList<>();

        for (Feedback feedbackDaVez : feedbacks) {
        FeedbackEntity entity = new FeedbackEntity();

        entity.setCurso(CursoMapper.toEntity(feedbackDaVez.getCurso()));
        entity.setFkCurso(feedbackDaVez.getFkCurso());
        entity.setEstrelas(feedbackDaVez.getEstrelas());
        entity.setMotivo(feedbackDaVez.getMotivo());
        entity.setFkUsuario(UsuarioMapper.toEntity(feedbackDaVez.getFkUsuario()));

        entities.add(entity);
        }
        return entities;
    }

    public static List<Feedback> toDomains(List<FeedbackEntity> entities){
        List<Feedback> feedbacks = new ArrayList<>();

        for (FeedbackEntity entityDaVez : entities) {
        Feedback feedback = new Feedback();

        feedback.setCurso(CursoMapper.toDomain(entityDaVez.getCurso()));
        feedback.setFkCurso(entityDaVez.getFkCurso());
        feedback.setEstrelas(entityDaVez.getEstrelas());
        feedback.setMotivo(entityDaVez.getMotivo());
        feedback.setFkUsuario(UsuarioMapper.toDomain(entityDaVez.getFkUsuario()));

        feedbacks.add(feedback);
        }
        return feedbacks;
    }
}
