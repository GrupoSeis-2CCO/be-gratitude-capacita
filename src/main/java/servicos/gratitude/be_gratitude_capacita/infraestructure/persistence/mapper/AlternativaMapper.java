package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.AlternativaCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;

import java.util.ArrayList;
import java.util.List;

public class AlternativaMapper {
    public static AlternativaEntity toEntity(Alternativa alternativa){
        if (alternativa == null) return null;
        AlternativaEntity entity = new AlternativaEntity();
        // Map composite key if present
        if (alternativa.getAlternativaChaveComposta() != null) {
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey k = AlternativaCompoundKeyMapper.toEntity(alternativa.getAlternativaChaveComposta());
            entity.setAlternativaChaveComposta(k);
        }
        entity.setTexto(alternativa.getTexto());
        entity.setOrdem(alternativa.getOrdem());

        // Map questao and ensure foreign keys are set
        QuestaoEntity questaoEntity = QuestaoMapper.toEntity(alternativa.getQuestao());
        entity.setQuestao(questaoEntity);
        if (questaoEntity != null && questaoEntity.getIdQuestaoComposto() != null) {
            Integer fkQuestao = questaoEntity.getIdQuestaoComposto().getIdQuestao();
            Integer fkAvaliacao = questaoEntity.getIdQuestaoComposto().getFkAvaliacao();
            entity.setFkQuestao(fkQuestao);
            entity.setFkAvaliacao(fkAvaliacao);
            // If composite key is missing or incomplete, set it
            if (entity.getAlternativaChaveComposta() == null) {
                var k = new servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey();
                k.setIdQuestao(fkQuestao);
                k.setIdAvaliacao(fkAvaliacao);
                k.setIdAlternativa(entity.getIdAlternativa());
                entity.setAlternativaChaveComposta(k);
            } else {
                // Ensure composite key is in sync
                var k = entity.getAlternativaChaveComposta();
                if (k.getIdQuestao() == null) k.setIdQuestao(fkQuestao);
                if (k.getIdAvaliacao() == null) k.setIdAvaliacao(fkAvaliacao);
            }
        }
        return entity;
    }

    public static Alternativa toDomain(AlternativaEntity entity){
        if (entity == null) return null;
        return toDomain(entity, true);
    }

    public static Alternativa toDomain(AlternativaEntity entity, boolean includeQuestao){
        if (entity == null) return null;
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
