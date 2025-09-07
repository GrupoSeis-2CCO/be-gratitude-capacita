package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;

import java.util.ArrayList;
import java.util.List;

public class ApostilaMapper {
    public static ApostilaEntity toEntity(Apostila apostila){
        ApostilaEntity entity = new ApostilaEntity();

        entity.setIdApostila(apostila.getIdApostila());
        entity.setNomeApostilaOriginal(apostila.getNomeApostilaOriginal());
        entity.setNomeApostilaArmazenamento(apostila.getNomeApostilaArmazenamento());
        entity.setDescricaoApostila(apostila.getDescricaoApostila());
        entity.setDataPostadoApostila(apostila.getDataPostadoApostila());
        entity.setDataAtualizacaoApostila(apostila.getDataAtualizacaoApostila());
        entity.setApostilaOculto(apostila.getApostilaOculto());
        entity.setTamanhoBytes(apostila.getTamanhoBytes());
        entity.setOrdemApostila(apostila.getOrdemApostila());
        entity.setFkCurso(CursoMapper.toEntity(apostila.getFkCurso()));

        return entity;
    }

    public static Apostila toDomain(ApostilaEntity entity){
        Apostila apostila = new Apostila();

        apostila.setIdApostila(entity.getIdApostila());
        apostila.setNomeApostilaOriginal(entity.getNomeApostilaOriginal());
        apostila.setNomeApostilaArmazenamento(entity.getNomeApostilaArmazenamento());
        apostila.setDescricaoApostila(entity.getDescricaoApostila());
        apostila.setDataPostadoApostila(entity.getDataPostadoApostila());
        apostila.setDataAtualizacaoApostila(entity.getDataAtualizacaoApostila());
        apostila.setApostilaOculto(entity.getApostilaOculto());
        apostila.setTamanhoBytes(entity.getTamanhoBytes());
        apostila.setOrdemApostila(entity.getOrdemApostila());
        apostila.setFkCurso(CursoMapper.toDomain(entity.getFkCurso()));

        return apostila;
    }

    public static List<ApostilaEntity> toEntities(List<Apostila> apostilas){
        List<ApostilaEntity> entities = new ArrayList<>();

        for (Apostila apostilaDaVez : apostilas) {
        ApostilaEntity entity = new ApostilaEntity();

        entity.setIdApostila(apostilaDaVez.getIdApostila());
        entity.setNomeApostilaOriginal(apostilaDaVez.getNomeApostilaOriginal());
        entity.setNomeApostilaArmazenamento(apostilaDaVez.getNomeApostilaArmazenamento());
        entity.setDescricaoApostila(apostilaDaVez.getDescricaoApostila());
        entity.setDataPostadoApostila(apostilaDaVez.getDataPostadoApostila());
        entity.setDataAtualizacaoApostila(apostilaDaVez.getDataAtualizacaoApostila());
        entity.setApostilaOculto(apostilaDaVez.getApostilaOculto());
        entity.setTamanhoBytes(apostilaDaVez.getTamanhoBytes());
        entity.setOrdemApostila(apostilaDaVez.getOrdemApostila());
        entity.setFkCurso(CursoMapper.toEntity(apostilaDaVez.getFkCurso()));

        entities.add(entity);
        }
        return entities;
    }

    public static List<Apostila> toDomains(List<ApostilaEntity> entities){
        List<Apostila> apostilas = new ArrayList<>();

        for (ApostilaEntity entityDaVez : entities) {
        Apostila apostila = new Apostila();

        apostila.setIdApostila(entityDaVez.getIdApostila());
        apostila.setNomeApostilaOriginal(entityDaVez.getNomeApostilaOriginal());
        apostila.setNomeApostilaArmazenamento(entityDaVez.getNomeApostilaArmazenamento());
        apostila.setDescricaoApostila(entityDaVez.getDescricaoApostila());
        apostila.setDataPostadoApostila(entityDaVez.getDataPostadoApostila());
        apostila.setDataAtualizacaoApostila(entityDaVez.getDataAtualizacaoApostila());
        apostila.setApostilaOculto(entityDaVez.getApostilaOculto());
        apostila.setTamanhoBytes(entityDaVez.getTamanhoBytes());
        apostila.setOrdemApostila(entityDaVez.getOrdemApostila());
        apostila.setFkCurso(CursoMapper.toDomain(entityDaVez.getFkCurso()));

        apostilas.add(apostila);
        }
        
        return apostilas;
    }
}
