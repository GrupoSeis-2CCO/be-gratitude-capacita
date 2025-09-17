
package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UsuarioAdapter.class);
    private final UsuarioRepository usuarioRepository;

    public UsuarioAdapter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Garante que a senha será salva como hash BCrypt
        String senhaOriginal = usuario.getSenha();
        if (senhaOriginal != null && !senhaOriginal.startsWith("$2a$")) { // evita re-hash
            usuario.setSenha(org.springframework.security.crypto.bcrypt.BCrypt.hashpw(senhaOriginal, org.springframework.security.crypto.bcrypt.BCrypt.gensalt()));
        }
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        usuarioRepository.save(entity);
        return UsuarioMapper.toDomain(entity);
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        log.info("Tentando autenticar usuário: {}", email);
        Optional<UsuarioEntity> entityOpt = usuarioRepository.findAll().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().trim().equalsIgnoreCase(email.trim()))
                .findFirst();
        if (entityOpt.isPresent()) {
            String hash = entityOpt.get().getSenha();
            log.debug("Hash salvo no banco: {}", hash);
            if (BCrypt.checkpw(senha, hash)) {
                log.info("Usuário autenticado com sucesso: {}", email);
                return UsuarioMapper.toDomain(entityOpt.get());
            } else {
                log.warn("Senha inválida para {}", email);
            }
        } else {
            log.warn("Usuário com email {} não encontrado no banco.", email);
        }
        return null;
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
