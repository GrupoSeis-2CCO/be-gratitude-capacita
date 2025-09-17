package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioLoginDto;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioTokenDto;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TokenJwtGateway;

public class AutenticarUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    private final TokenJwtGateway tokenJwtGateway;

    public AutenticarUsuarioUseCase(UsuarioGateway usuarioGateway, TokenJwtGateway tokenJwtGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tokenJwtGateway = tokenJwtGateway;
    }

    public UsuarioTokenDto execute(UsuarioLoginDto loginDto) {
        Usuario usuario = usuarioGateway.autenticar(loginDto.getEmail(), loginDto.getSenha());
        if (usuario == null) {
            throw new RuntimeException("Credenciais inválidas");
        }
        String token = tokenJwtGateway.gerarToken(usuario);
        UsuarioTokenDto dto = new UsuarioTokenDto();
        dto.setToken(token);
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        if (usuario.getFkCargo() != null) {
            dto.setIdCargo(usuario.getFkCargo().getIdCargo());
        }
        return dto;
    }
}
