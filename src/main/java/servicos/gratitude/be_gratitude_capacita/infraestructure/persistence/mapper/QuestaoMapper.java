package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.QuestaoCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class QuestaoMapper {
    public static QuestaoEntity toEntity(Questao questao){
        if (questao == null) return null;
        QuestaoEntity entity = new QuestaoEntity();

        // Se a chave composta for nula (questão nova), instancie e preencha fkAvaliacao
        if (questao.getIdQuestaoComposto() != null) {
            entity.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toEntity(questao.getIdQuestaoComposto()));
        } else if (questao.getAvaliacao() != null && questao.getAvaliacao().getIdAvaliacao() != null) {
            var key = new servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.QuestaoEntityCompoundKey();
            key.setFkAvaliacao(questao.getAvaliacao().getIdAvaliacao());
            // idQuestao fica nulo para ser gerado
            entity.setIdQuestaoComposto(key);
        }
    entity.setEnunciado(questao.getEnunciado());
    entity.setNumeroQuestao(questao.getNumeroQuestao());
    entity.setFkAlternativaCorreta(AlternativaMapper.toEntity(questao.getFkAlternativaCorreta()));
    entity.setAvaliacaoEntity(AvaliacaoMapper.toEntity(questao.getAvaliacao()));

        return entity;
    }

    /**
     * Maps only the composite key to avoid deep recursion or null pointer issues
     * when only an identifier is required (e.g., repository lookups).
     */
    public static QuestaoEntity toEntityKeyOnly(Questao questao){
        if (questao == null) return null;
        QuestaoEntity entity = new QuestaoEntity();
        entity.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toEntity(questao.getIdQuestaoComposto()));
        return entity;
    }

    public static Questao toDomain(QuestaoEntity entity){
        Questao questao = new Questao();

        if (entity.getIdQuestaoComposto() != null) {
            questao.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toDomain(entity.getIdQuestaoComposto()));
        }
        questao.setEnunciado(entity.getEnunciado());
        questao.setNumeroQuestao(entity.getNumeroQuestao());
        questao.setFkAlternativaCorreta(AlternativaMapper.toDomain(entity.getFkAlternativaCorreta(), false)); // Don't include questao to avoid circular ref
        questao.setAvaliacao(AvaliacaoMapper.toDomain(entity.getAvaliacaoEntity()));

        return questao;
    }

    /**
     * Maps a QuestaoEntity to domain without creating circular references.
     * Maps fkAlternativaCorreta without its questao to break the cycle.
     */
    public static Questao toDomainWithoutCircular(QuestaoEntity entity){
        if (entity == null) return null;
        
        Questao questao = new Questao();
        if (entity.getIdQuestaoComposto() != null) {
            questao.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toDomain(entity.getIdQuestaoComposto()));
        }
        questao.setEnunciado(entity.getEnunciado());
        questao.setNumeroQuestao(entity.getNumeroQuestao());
        questao.setAvaliacao(AvaliacaoMapper.toDomain(entity.getAvaliacaoEntity()));
        
        // Map fkAlternativaCorreta but tell the AlternativaMapper NOT to include the questao back
        if (entity.getFkAlternativaCorreta() != null) {
            questao.setFkAlternativaCorreta(AlternativaMapper.toDomain(entity.getFkAlternativaCorreta(), false));
        }

        return questao;
    }

    public static List<QuestaoEntity> toEntities(List<Questao> questoes){
        List<QuestaoEntity> entities = new ArrayList<>();

        for (Questao questaoDaVez : questoes) {
        if (questaoDaVez == null) continue;
        QuestaoEntity entity = new QuestaoEntity();

    entity.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toEntity(questaoDaVez.getIdQuestaoComposto()));
    entity.setEnunciado(questaoDaVez.getEnunciado());
    entity.setNumeroQuestao(questaoDaVez.getNumeroQuestao());
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

        if (entityDaVez.getIdQuestaoComposto() != null) {
            questao.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toDomain(entityDaVez.getIdQuestaoComposto()));
        }
        questao.setEnunciado(entityDaVez.getEnunciado());
        questao.setNumeroQuestao(entityDaVez.getNumeroQuestao());
        questao.setFkAlternativaCorreta(AlternativaMapper.toDomain(entityDaVez.getFkAlternativaCorreta()));
        questao.setAvaliacao(AvaliacaoMapper.toDomain(entityDaVez.getAvaliacaoEntity()));

        questoes.add(questao);
        }

        return questoes;
    }
}
