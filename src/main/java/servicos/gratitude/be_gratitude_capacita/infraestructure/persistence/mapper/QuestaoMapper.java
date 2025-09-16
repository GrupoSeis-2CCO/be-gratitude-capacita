package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.QuestaoCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class QuestaoMapper {
    public static QuestaoEntity toEntity(Questao questao){
        QuestaoEntity entity = new QuestaoEntity();

        entity.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toEntity(questao.getIdQuestaoComposto()));
        entity.setEnunciado(questao.getEnunciado());
        entity.setFkAlternativaCorreta(AlternativaMapper.toEntity(questao.getFkAlternativaCorreta()));
        entity.setAvaliacaoEntity(AvaliacaoMapper.toEntity(questao.getAvaliacao()));

        return entity;
    }

    public static Questao toDomain(QuestaoEntity entity){
        Questao questao = new Questao();

        questao.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toDomain(entity.getIdQuestaoComposto()));
        questao.setEnunciado(entity.getEnunciado());
        questao.setFkAlternativaCorreta(AlternativaMapper.toDomain(entity.getFkAlternativaCorreta()));
        questao.setAvaliacao(AvaliacaoMapper.toDomain(entity.getAvaliacaoEntity()));

        return questao;
    }

    public static List<QuestaoEntity> toEntities(List<Questao> questoes){
        List<QuestaoEntity> entities = new ArrayList<>();

        for (Questao questaoDaVez : questoes) {
        QuestaoEntity entity = new QuestaoEntity();

        entity.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toEntity(questaoDaVez.getIdQuestaoComposto()));
        entity.setEnunciado(questaoDaVez.getEnunciado());
        entity.setFkAlternativaCorreta(AlternativaMapper.toEntity(questaoDaVez.getFkAlternativaCorreta()));
        entity.setAvaliacaoEntity(AvaliacaoMapper.toEntity(questaoDaVez.getAvaliacao()));

        entities.add(entity);
        }

        return entities;
    }

    public static List<Questao> toDomains(List<QuestaoEntity> entities){
        List<Questao> questoes = new ArrayList<>();

        for (QuestaoEntity entityDaVez : entities) {
        Questao questao = new Questao();

        questao.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toDomain(entityDaVez.getIdQuestaoComposto()));
        questao.setEnunciado(entityDaVez.getEnunciado());
        questao.setFkAlternativaCorreta(AlternativaMapper.toDomain(entityDaVez.getFkAlternativaCorreta()));
        questao.setAvaliacao(AvaliacaoMapper.toDomain(entityDaVez.getAvaliacaoEntity()));

        questoes.add(questao);
        }

        return questoes;
    }
}
