package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.RespostaDoUsuario;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.RespostaDoUsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.RespostaDoUsuarioCompoundKeyMapper;

import java.util.ArrayList;
import java.util.List;

public class RespostaDoUsuarioMapper {
    public static RespostaDoUsuarioEntity toEntity(RespostaDoUsuario respostaDoUsuario){
        RespostaDoUsuarioEntity entity = new RespostaDoUsuarioEntity();

        entity.setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioCompoundKeyMapper.toEntity(respostaDoUsuario.getRespostaDoUsuarioCompoundKey()));
        entity.setAlternativa(AlternativaMapper.toEntity(respostaDoUsuario.getAlternativa()));
        entity.setTentativa(TentativaMapper.toEntity(respostaDoUsuario.getTentativa()));

        return entity;
    }

    public static RespostaDoUsuario toDomain(RespostaDoUsuarioEntity entity){
        RespostaDoUsuario respostaDoUsuario = new RespostaDoUsuario();

        respostaDoUsuario.setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioCompoundKeyMapper.toDomain(entity.getRespostaDoUsuarioCompoundKey()));
        respostaDoUsuario.setAlternativa(AlternativaMapper.toDomain(entity.getAlternativa()));
        // Don't map tentativa to avoid LazyInitializationException - it's not needed for nota calculation
        // respostaDoUsuario.setTentativa(TentativaMapper.toDomain(entity.getTentativa()));

        return respostaDoUsuario;
    }

    public static List<RespostaDoUsuarioEntity> toEntities(List<RespostaDoUsuario> respostasDoUsuario){
        List<RespostaDoUsuarioEntity> entities = new ArrayList<>();

        for (RespostaDoUsuario respostaDoUsuarioDaVez : respostasDoUsuario) {
        RespostaDoUsuarioEntity entity = new RespostaDoUsuarioEntity();

        entity.setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioCompoundKeyMapper.toEntity(respostaDoUsuarioDaVez.getRespostaDoUsuarioCompoundKey()));
        entity.setAlternativa(AlternativaMapper.toEntity(respostaDoUsuarioDaVez.getAlternativa()));
        entity.setTentativa(TentativaMapper.toEntity(respostaDoUsuarioDaVez.getTentativa()));

        entities.add(entity);
        }

        return entities;
    }

    public static List<RespostaDoUsuario> toDomains(List<RespostaDoUsuarioEntity> entities){
        List<RespostaDoUsuario> respostasDoUsuario = new ArrayList<>();

        for (RespostaDoUsuarioEntity entityDaVez : entities) {
        RespostaDoUsuario respostaDoUsuario = new RespostaDoUsuario();

        respostaDoUsuario.setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioCompoundKeyMapper.toDomain(entityDaVez.getRespostaDoUsuarioCompoundKey()));
        respostaDoUsuario.setAlternativa(AlternativaMapper.toDomain(entityDaVez.getAlternativa()));
        // Don't map tentativa to avoid LazyInitializationException - it's not needed for nota calculation
        // respostaDoUsuario.setTentativa(TentativaMapper.toDomain(entityDaVez.getTentativa()));

        respostasDoUsuario.add(respostaDoUsuario);
        }

        return respostasDoUsuario;
    }
}
