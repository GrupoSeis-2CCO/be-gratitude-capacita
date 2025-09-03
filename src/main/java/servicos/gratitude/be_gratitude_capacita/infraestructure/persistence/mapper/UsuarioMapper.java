package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;

public class UsuarioMapper {

    public static UsuarioEntity toEntity(Usuario usuario){
        UsuarioEntity entity = new UsuarioEntity();

        entity.setCpf(usuario.getCpf());
        entity.setIdUsuario(usuario.getIdUsuario());
        entity.setEmail(usuario.getEmail());
        entity.setNome(usuario.getNome());
        entity.setSenha(usuario.getSenha());
        entity.setDataEntrada(usuario.getDataEntrada());
        entity.setUltimoAcesso(usuario.getUltimoAcesso());
        entity.setFkCargo(CargoMapper.toEntity(usuario.getFkCargo()));

        return entity;
    }

    public static Usuario toDomain(UsuarioEntity entity){
        Usuario usuario = new Usuario();

        usuario.setCpf(entity.getCpf());
        usuario.setIdUsuario(entity.getIdUsuario());
        usuario.setEmail(entity.getEmail());
        usuario.setNome(entity.getNome());
        usuario.setSenha(entity.getSenha());
        usuario.setDataEntrada(entity.getDataEntrada());
        usuario.setUltimoAcesso(entity.getUltimoAcesso());
        usuario.setFkCargo(CargoMapper.toDomain(entity.getFkCargo()));

        return usuario;
    }

    public static List<Usuario> toDomains(List<UsuarioEntity> entity){
        List<Usuario> usuarios = new ArrayList<>();

        for (UsuarioEntity entityDaVez : entity) {
        Usuario usuario = new Usuario();

        usuario.setCpf(entityDaVez.getCpf());
        usuario.setIdUsuario(entityDaVez.getIdUsuario());
        usuario.setEmail(entityDaVez.getEmail());
        usuario.setNome(entityDaVez.getNome());
        usuario.setSenha(entityDaVez.getSenha());
        usuario.setDataEntrada(entityDaVez.getDataEntrada());
        usuario.setUltimoAcesso(entityDaVez.getUltimoAcesso());
        usuario.setFkCargo(CargoMapper.toDomain(entityDaVez.getFkCargo()));

        usuarios.add(usuario);
        }

        return usuarios;
    }
}
