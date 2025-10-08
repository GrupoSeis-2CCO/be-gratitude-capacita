package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Acesso;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AcessoEntity;

import java.util.ArrayList;
import java.util.List;

public class AcessoMapper {
    public static AcessoEntity toEntity(Acesso acesso){
        AcessoEntity entity = new AcessoEntity();

        entity.setIdAcesso(acesso.getIdAcesso());
        entity.setDataAcesso(acesso.getDataAcesso());
        entity.setFkUsuario(UsuarioMapper.toEntity(acesso.getFkusuario()));

        return entity;
    }

    public static Acesso toDomain(AcessoEntity entity){
        Acesso acesso = new Acesso();

        acesso.setIdAcesso(entity.getIdAcesso());
        acesso.setDataAcesso(entity.getDataAcesso());
        acesso.setFkusuario(UsuarioMapper.toDomain(entity.getFkUsuario()));

        return acesso;
    }

    public static List<AcessoEntity> toEntities(List<Acesso> acessos){
        List<AcessoEntity> entities = new ArrayList<>();

        for (Acesso acesso : acessos) {
        AcessoEntity entity = new AcessoEntity();

        entity.setIdAcesso(acesso.getIdAcesso());
        entity.setDataAcesso(acesso.getDataAcesso());
        entity.setFkUsuario(UsuarioMapper.toEntity(acesso.getFkusuario()));

        entities.add(entity);
        }

        return entities;
    }

    public static List<Acesso> toDomains(List<AcessoEntity> entities){
        List<Acesso> acessos = new ArrayList<>();

        for (AcessoEntity entity : entities) {
        Acesso acesso = new Acesso();

        acesso.setIdAcesso(entity.getIdAcesso());
        acesso.setDataAcesso(entity.getDataAcesso());
        acesso.setFkusuario(UsuarioMapper.toDomain(entity.getFkUsuario()));

        acessos.add(acesso);
        }

        return acessos;
    }
}
