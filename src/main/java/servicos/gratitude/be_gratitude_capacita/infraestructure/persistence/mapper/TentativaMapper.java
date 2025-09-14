package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.TentativaCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class TentativaMapper {
    public static TentativaEntity toEntity(Tentativa tentativa){
        TentativaEntity entity = new TentativaEntity();

        entity.setIdTentativaComposto(TentativaCompoundKeyMapper.toEntity(tentativa.getIdTentativaComposto()));
        entity.setDtTentativa(tentativa.getDtTentativa());
        entity.setMatricula(MatriculaMapper.toEntity(tentativa.getMatricula()));
        entity.setAvaliacao(AvaliacaoMapper.toEntity(tentativa.getAvaliacao()));

        return entity;
    }

    public static Tentativa toDomain(TentativaEntity entity){
        Tentativa tentativa = new Tentativa();

        tentativa.setIdTentativaComposto(TentativaCompoundKeyMapper.toDomain(entity.getIdTentativaComposto()));
        tentativa.setDtTentativa(entity.getDtTentativa());
        tentativa.setMatricula(MatriculaMapper.toDomains(entity.getMatricula()));
        tentativa.setAvaliacao(AvaliacaoMapper.toDomain(entity.getAvaliacao()));

        return tentativa;
    }

    public static List<TentativaEntity> toEntities(List<Tentativa> tentativas){
        List<TentativaEntity> entities = new ArrayList<>();

        for (Tentativa tentativaDaVez : tentativas) {
        TentativaEntity entity = new TentativaEntity();

        entity.setIdTentativaComposto(TentativaCompoundKeyMapper.toEntity(tentativaDaVez.getIdTentativaComposto()));
        entity.setDtTentativa(tentativaDaVez.getDtTentativa());
        entity.setMatricula(MatriculaMapper.toEntity(tentativaDaVez.getMatricula()));
        entity.setAvaliacao(AvaliacaoMapper.toEntity(tentativaDaVez.getAvaliacao()));

        entities.add(entity);
        }

        return entities;
    }

    public static List<Tentativa> toDomains(List<TentativaEntity> entities){
        List<Tentativa> tentativas = new ArrayList<>();

        for (TentativaEntity entityDaVez : entities) {
        Tentativa tentativa = new Tentativa();

        tentativa.setIdTentativaComposto(TentativaCompoundKeyMapper.toDomain(entityDaVez.getIdTentativaComposto()));
        tentativa.setDtTentativa(entityDaVez.getDtTentativa());
        tentativa.setMatricula(MatriculaMapper.toDomains(entityDaVez.getMatricula()));
        tentativa.setAvaliacao(AvaliacaoMapper.toDomain(entityDaVez.getAvaliacao()));

        tentativas.add(tentativa);
        }

        return tentativas;
    }
}
