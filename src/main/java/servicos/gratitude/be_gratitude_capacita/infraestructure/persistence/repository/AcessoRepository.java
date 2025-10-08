package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AcessoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;

import java.util.List;

public interface AcessoRepository extends JpaRepository<AcessoEntity, Integer> {
    List<AcessoEntity> findAllByFkUsuario(UsuarioEntity usuarioEntity);
}
