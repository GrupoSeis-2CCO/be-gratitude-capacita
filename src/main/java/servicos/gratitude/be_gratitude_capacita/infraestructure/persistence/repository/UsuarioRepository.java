package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    List<UsuarioEntity> findAllByNomeContainsIgnoreCase(String nome);

    Boolean existsByEmail(String email);

    Boolean existsByCpf(String cpf);

    // Para autenticação JWT
    Optional<UsuarioEntity> findByEmailAndSenha(String email, String senha);
}
