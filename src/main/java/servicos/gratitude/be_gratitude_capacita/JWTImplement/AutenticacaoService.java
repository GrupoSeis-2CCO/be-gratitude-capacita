package servicos.gratitude.be_gratitude_capacita.JWTImplement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha o campo de e-mail");
        }
        final String emailFinal = email.trim();

        if (!emailFinal.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Formato de e-mail inválido");
        }

        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        UsuarioEntity usuario = usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(emailFinal))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                List.of());

    }

    public void validarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Preencha a senha");
        }

        if (senha.length() < 4) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 4 caracteres");
        }
    }
}