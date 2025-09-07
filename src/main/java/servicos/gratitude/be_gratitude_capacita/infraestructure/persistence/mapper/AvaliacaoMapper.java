package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AvaliacaoEntity;

import java.util.ArrayList;
import java.util.List;

public class AvaliacaoMapper {
    public static AvaliacaoEntity toEntity(Avaliacao avaliacao){
        AvaliacaoEntity entity = new AvaliacaoEntity();

        entity.setIdAvaliacao(avaliacao.getIdAvaliacao());
        entity.setAcertosMinimos(avaliacao.getAcertosMinimos());
        entity.setFkCurso(CursoMapper.toEntity(avaliacao.getFkCurso()));

        return entity;
    }

    public static Avaliacao toDomain(AvaliacaoEntity entity){
        Avaliacao avaliacao = new Avaliacao();

        avaliacao.setIdAvaliacao(entity.getIdAvaliacao());
        avaliacao.setAcertosMinimos(entity.getAcertosMinimos());
        avaliacao.setFkCurso(CursoMapper.toDomain(entity.getFkCurso()));

        return avaliacao;
    }

    public static List<AvaliacaoEntity> toEntities(List<Avaliacao> avaliacoes){
        List<AvaliacaoEntity> entities = new ArrayList<>();

        for (Avaliacao avaliacaoDaVez : avaliacoes) {
        AvaliacaoEntity entity = new AvaliacaoEntity();

        entity.setIdAvaliacao(avaliacaoDaVez.getIdAvaliacao());
        entity.setAcertosMinimos(avaliacaoDaVez.getAcertosMinimos());
        entity.setFkCurso(CursoMapper.toEntity(avaliacaoDaVez.getFkCurso()));

        entities.add(entity);
        }

        return entities;
    }

    public static List<Avaliacao> toDomain(List<AvaliacaoEntity> entities){
        List<Avaliacao> avaliacoes = new ArrayList<>();

        for (AvaliacaoEntity entityDaVez : entities) {
        Avaliacao avaliacao = new Avaliacao();

        avaliacao.setIdAvaliacao(entityDaVez.getIdAvaliacao());
        avaliacao.setAcertosMinimos(entityDaVez.getAcertosMinimos());
        avaliacao.setFkCurso(CursoMapper.toDomain(entityDaVez.getFkCurso()));

        avaliacoes.add(avaliacao);
        }

        return avaliacoes;
    }
}
