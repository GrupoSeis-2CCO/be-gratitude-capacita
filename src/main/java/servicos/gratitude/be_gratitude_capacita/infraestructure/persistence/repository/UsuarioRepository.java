package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    List<UsuarioEntity> findAllByNomeContainsIgnoreCase(String nome);

    Boolean existsByEmail(String email);

    Boolean existsByCpf(String cpf);

    Optional<UsuarioEntity> findByEmailAndSenha(String email, String senha);

    /**
     * Busca usuários por ID do cargo.
     * Ex: fkCargo.idCargo = 2 retorna colaboradores
     */
    List<UsuarioEntity> findByFkCargo_IdCargo(Integer idCargo);
}
