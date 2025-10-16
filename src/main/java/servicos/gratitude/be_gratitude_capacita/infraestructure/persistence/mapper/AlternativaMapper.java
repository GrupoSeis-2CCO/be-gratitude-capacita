package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.AlternativaCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class AlternativaMapper {
    public static AlternativaEntity toEntity(Alternativa alternativa){
        AlternativaEntity entity = new AlternativaEntity();
        if (alternativa.getAlternativaChaveComposta() != null) {
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey k = AlternativaCompoundKeyMapper.toEntity(alternativa.getAlternativaChaveComposta());
            entity.setAlternativaChaveComposta(k);
        }
        entity.setTexto(alternativa.getTexto());
        entity.setOrdem(alternativa.getOrdem());
        entity.setQuestao(QuestaoMapper.toEntity(alternativa.getQuestao()));

        return entity;
    }

    public static Alternativa toDomain(AlternativaEntity entity){
        return toDomain(entity, true);
    }

    public static Alternativa toDomain(AlternativaEntity entity, boolean includeQuestao){
        Alternativa alternativa = new Alternativa();
        if (entity.getAlternativaChaveComposta() != null) {
            alternativa.setAlternativaChaveComposta(AlternativaCompoundKeyMapper.toDomain(entity.getAlternativaChaveComposta()));
        }
        alternativa.setTexto(entity.getTexto());
        alternativa.setOrdem(entity.getOrdem());
        
        // Only map questao if requested AND if entity has it loaded to avoid LazyInitializationException
        if (includeQuestao && entity.getQuestao() != null) {
            alternativa.setQuestao(QuestaoMapper.toDomainWithoutCircular(entity.getQuestao()));
        }

        return alternativa;
    }

    public static List<AlternativaEntity> toEntities(List<Alternativa> alternativas){
        List<AlternativaEntity> entities = new ArrayList<>();

        for (Alternativa alternativaDaVez : alternativas) {
        AlternativaEntity entity = new AlternativaEntity();

        entity.setAlternativaChaveComposta(AlternativaCompoundKeyMapper.toEntity(alternativaDaVez.getAlternativaChaveComposta()));
        entity.setTexto(alternativaDaVez.getTexto());
        entity.setOrdem(alternativaDaVez.getOrdem());
        entity.setQuestao(QuestaoMapper.toEntity(alternativaDaVez.getQuestao()));

        entities.add(entity);
        }

        return entities;
    }

    public static List<Alternativa> toDomains(List<AlternativaEntity> entities){
        return toDomains(entities, true);
    }

    public static List<Alternativa> toDomains(List<AlternativaEntity> entities, boolean includeQuestao){
        List<Alternativa> alternativas = new ArrayList<>();

        for (AlternativaEntity entityDaVez : entities) {
            alternativas.add(toDomain(entityDaVez, includeQuestao));
        }

        return alternativas;
    }
}
