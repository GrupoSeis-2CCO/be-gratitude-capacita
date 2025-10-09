package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedbackMapper {
    public static FeedbackEntity toEntity(Feedback feedback) {
        if (Objects.isNull(feedback)) {
            return null;
        }

        FeedbackEntity entity = new FeedbackEntity();
        entity.setFkCurso(feedback.getFkCurso().intValue());
        entity.setEstrelas(feedback.getEstrelas());
        entity.setMotivo(feedback.getMotivo());
        entity.setFkUsuario(UsuarioMapper.toEntity(feedback.getFkUsuario()));

        return entity;
    }

    public static Feedback toDomain(FeedbackEntity entity){
        if (Objects.isNull(entity)) {
            return null;
        }

        Feedback feedback = new Feedback();

        // map curso id only
        Curso curso = new Curso();
        curso.setIdCurso(entity.getFkCurso().longValue());
        feedback.setCurso(curso);

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
            if (Objects.isNull(entityDaVez)) continue;
            Feedback feedback = new Feedback();

            Curso curso = new Curso();
            curso.setIdCurso(entityDaVez.getFkCurso().longValue());
            feedback.setCurso(curso);

            feedback.setFkCurso(entityDaVez.getFkCurso());
            feedback.setEstrelas(entityDaVez.getEstrelas());
            feedback.setMotivo(entityDaVez.getMotivo());
            feedback.setFkUsuario(UsuarioMapper.toDomain(entityDaVez.getFkUsuario()));

            feedbacks.add(feedback);
        }
        return feedbacks;
    }
}
