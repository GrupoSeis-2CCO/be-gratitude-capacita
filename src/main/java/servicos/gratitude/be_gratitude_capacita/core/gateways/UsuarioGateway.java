package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

import java.util.List;

public interface UsuarioGateway {
    Usuario save(Usuario usuario);

    List<Usuario> findAll();

    List<Usuario> findAllBySearch(String nome);

    Usuario findById(Integer id);

    Boolean existsById(Integer id);

    Boolean existsByEmail(String email);

    Boolean existsByCpf(String cpf);

    void deleteById(Integer id);

    // Para autenticação JWT
    Usuario autenticar(String email, String senha);
}
