package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.QuestaoEntityCompoundKey;

public class QuestaoCompoundKeyMapper {
    public static QuestaoEntityCompoundKey toEntity(QuestaoCompoundKey questaoCompoundKey){
        QuestaoEntityCompoundKey entityCompoundKey = new QuestaoEntityCompoundKey();

        entityCompoundKey.setIdQuestao(questaoCompoundKey.getIdQuestao());
        entityCompoundKey.setFkAvaliacao(questaoCompoundKey.getFkAvaliacao());

        return entityCompoundKey;
    }

    public static QuestaoCompoundKey toDomain(QuestaoEntityCompoundKey entityCompoundKey){
        QuestaoCompoundKey questaoCompoundKey = new QuestaoCompoundKey();

        questaoCompoundKey.setIdQuestao(entityCompoundKey.getIdQuestao());
        questaoCompoundKey.setFkAvaliacao(entityCompoundKey.getFkAvaliacao());

        return questaoCompoundKey;
    }
}
