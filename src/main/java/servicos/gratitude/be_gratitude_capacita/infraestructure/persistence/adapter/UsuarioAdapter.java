package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CargoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.UsuarioMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CargoRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioAdapter implements UsuarioGateway {
    private final UsuarioRepository usuarioRepository;

    public UsuarioAdapter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        usuarioRepository.save(entity);

        return UsuarioMapper.toDomain(entity);
    }

    @Override
    public List<Usuario> findAll() {
        return UsuarioMapper.toDomains(usuarioRepository.findAll());
    }

    @Override
    public List<Usuario> findAllBySearch(String nome) {
        return UsuarioMapper.toDomains(usuarioRepository.findAllByNomeContainsIgnoreCase(nome));
    }

    @Override
    public Usuario findById(Integer id) {
        Optional<UsuarioEntity> entity = usuarioRepository.findById(id);

        return entity.map(UsuarioMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(Integer id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByCpf(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    @Override
    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
