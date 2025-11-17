
package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CargoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.RespostaDoUsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.FeedbackEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.UsuarioMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.UsuarioRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MatriculaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MaterialAlunoRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.TentativaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.RespostaDoUsuarioRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.FeedbackRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioAdapter implements UsuarioGateway {
    private static final Logger log = LoggerFactory.getLogger(UsuarioAdapter.class);
    private final UsuarioRepository usuarioRepository;
    private final MatriculaRepository matriculaRepository;
    private final MaterialAlunoRepository materialAlunoRepository;
    private final TentativaRepository tentativaRepository;
    private final RespostaDoUsuarioRepository respostaDoUsuarioRepository;
    private final FeedbackRepository feedbackRepository;

    public UsuarioAdapter(
            UsuarioRepository usuarioRepository,
            MatriculaRepository matriculaRepository,
            MaterialAlunoRepository materialAlunoRepository,
            TentativaRepository tentativaRepository,
            RespostaDoUsuarioRepository respostaDoUsuarioRepository,
            FeedbackRepository feedbackRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.matriculaRepository = matriculaRepository;
        this.materialAlunoRepository = materialAlunoRepository;
        this.tentativaRepository = tentativaRepository;
        this.respostaDoUsuarioRepository = respostaDoUsuarioRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Garante que a senha será salva como hash BCrypt
        String senhaOriginal = usuario.getSenha();
        if (senhaOriginal != null && !senhaOriginal.startsWith("$2a$")) { // evita re-hash
            usuario.setSenha(org.springframework.security.crypto.bcrypt.BCrypt.hashpw(senhaOriginal,
                    org.springframework.security.crypto.bcrypt.BCrypt.gensalt()));
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
            UsuarioEntity userEntity = entityOpt.get();
            String hash = userEntity.getSenha();
            log.debug("Hash salvo no banco: {}", hash);

            // Se o valor no banco parece ser um hash BCrypt válido, use checkpw
            if (hash != null && hash.startsWith("$2")) {
                try {
                    if (BCrypt.checkpw(senha, hash)) {
                        log.info("Usuário autenticado com sucesso: {}", email);
                        return UsuarioMapper.toDomain(userEntity);
                    } else {
                        log.warn("Senha inválida para {}", email);
                    }
                } catch (IllegalArgumentException iae) {
                    // Protege contra 'Invalid salt' e outros formatos inesperados
                    log.warn("Formato de hash inválido no banco para {}: {}", email, iae.getMessage());
                }
            } else {
                // Possível senha em texto simples (legacy) — comparar diretamente
                if (hash != null && hash.equals(senha)) {
                    log.info("Usuário autenticado (senha em texto simples). Atualizando para bcrypt: {}", email);
                    try {
                        String newHash = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(senha,
                                org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
                        userEntity.setSenha(newHash);
                        usuarioRepository.save(userEntity);
                        // retornar domínio com a entidade já atualizada
                        return UsuarioMapper.toDomain(userEntity);
                    } catch (Exception e) {
                        log.error("Falha ao re-hashar senha do usuário {}: {}", email, e.getMessage());
                        // mesmo se falhar ao re-hash, já autenticou com texto simples: retornar usuário
                        return UsuarioMapper.toDomain(userEntity);
                    }
                } else {
                    log.warn("Senha inválida (legacy) para {}", email);
                }
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
    @Transactional
    public void deleteById(Integer id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElse(null);
        if (usuarioEntity == null) {
            return;
        }
        // 1) Remover dependências
        // Matrículas do usuário
        java.util.List<MatriculaEntity> matriculas = matriculaRepository.findAllByUsuario(usuarioEntity);

        for (MatriculaEntity m : matriculas) {
            // material_aluno
            var materiais = materialAlunoRepository.findAllByMatricula(m);
            if (!materiais.isEmpty()) materialAlunoRepository.deleteAll(materiais);

            // tentativas e respostas
            java.util.List<TentativaEntity> tentativas = tentativaRepository.findAllByMatricula(m);
            for (TentativaEntity t : tentativas) {
                java.util.List<RespostaDoUsuarioEntity> respostas = respostaDoUsuarioRepository.findAllByTentativa(t);
                if (!respostas.isEmpty()) respostaDoUsuarioRepository.deleteAll(respostas);
            }
            if (!tentativas.isEmpty()) tentativaRepository.deleteAll(tentativas);
        }

        // feedbacks do usuário
        java.util.List<FeedbackEntity> feedbacksDoUsuario = feedbackRepository.findAllByFkUsuario(usuarioEntity);
        if (!feedbacksDoUsuario.isEmpty()) feedbackRepository.deleteAll(feedbacksDoUsuario);

        // remove matrículas
        if (!matriculas.isEmpty()) matriculaRepository.deleteAll(matriculas);

        // 2) Remover usuário
        usuarioRepository.deleteById(id);
    }
}
