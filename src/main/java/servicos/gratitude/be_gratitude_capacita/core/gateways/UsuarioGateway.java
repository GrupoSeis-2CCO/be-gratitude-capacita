package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

import java.util.List;

public interface UsuarioGateway {
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
    List<Usuario> findAllBySearch();
    Usuario findById(Integer id);
    Boolean existsById(Integer id);
    void deleteById(Integer id);
}
