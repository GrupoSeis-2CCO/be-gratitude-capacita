package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey;

public class AlternativaCompoundKeyMapper {
    public static AlternativaEntityCompoundKey toEntity(AlternativaCompoundKey alternativaCompoundKey){
        AlternativaEntityCompoundKey entityCompoundKey = new AlternativaEntityCompoundKey();

        entityCompoundKey.setIdAlternativa(alternativaCompoundKey.getIdAlternativa());
        entityCompoundKey.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toEntity(alternativaCompoundKey.getIdQuestaoComposto()));

        return entityCompoundKey;
    }

    public static AlternativaCompoundKey toDomain(AlternativaEntityCompoundKey entityCompoundKey){
        AlternativaCompoundKey alternativaCompoundKey = new AlternativaCompoundKey();

        alternativaCompoundKey.setIdAlternativa(entityCompoundKey.getIdAlternativa());
        alternativaCompoundKey.setIdQuestaoComposto(QuestaoCompoundKeyMapper.toDomain(entityCompoundKey.getIdQuestaoComposto()));

        return alternativaCompoundKey;
    }
}
