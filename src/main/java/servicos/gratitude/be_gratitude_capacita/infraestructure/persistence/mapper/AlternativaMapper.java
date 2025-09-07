package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.AlternativaCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class AlternativaMapper {
    public static AlternativaEntity toEntity(Alternativa alternativa){
        AlternativaEntity entity = new AlternativaEntity();

        entity.setAlternativaChaveComposta(AlternativaCompoundKeyMapper.toEntity(alternativa.getAlternativaChaveComposta()));
        entity.setTexto(alternativa.getTexto());
        entity.setOrdem(alternativa.getOrdem());
        entity.setQuestao(QuestaoMapper.toEntity(alternativa.getQuestao()));

        return entity;
    }

    public static Alternativa toDomain(AlternativaEntity entity){
        Alternativa alternativa = new Alternativa();

        alternativa.setAlternativaChaveComposta(AlternativaCompoundKeyMapper.toDomain(entity.getAlternativaChaveComposta()));
        alternativa.setTexto(entity.getTexto());
        alternativa.setOrdem(entity.getOrdem());
        alternativa.setQuestao(QuestaoMapper.toDomain(entity.getQuestao()));

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
        List<Alternativa> alternativas = new ArrayList<>();

        for (AlternativaEntity entityDaVez : entities) {
        Alternativa alternativa = new Alternativa();

        alternativa.setAlternativaChaveComposta(AlternativaCompoundKeyMapper.toDomain(entityDaVez.getAlternativaChaveComposta()));
        alternativa.setTexto(entityDaVez.getTexto());
        alternativa.setOrdem(entityDaVez.getOrdem());
        alternativa.setQuestao(QuestaoMapper.toDomain(entityDaVez.getQuestao()));

        alternativas.add(alternativa);
        }

        return alternativas;
    }
}
