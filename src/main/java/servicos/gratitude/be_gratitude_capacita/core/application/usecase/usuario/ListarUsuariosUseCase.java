package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

import java.util.List;

public class ListarUsuariosUseCase {
    private final UsuarioGateway usuarioGateway;

    public ListarUsuariosUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> execute(){
        return usuarioGateway.findAll();
    }
}
