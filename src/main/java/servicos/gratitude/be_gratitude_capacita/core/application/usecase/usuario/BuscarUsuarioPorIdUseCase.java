package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

public class BuscarUsuarioPorIdUseCase {
    private final UsuarioGateway usuarioGateway;

    public BuscarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(Integer idUsuario){
        if (!usuarioGateway.existsById(idUsuario)){
            throw new NaoEncontradoException("Não foi encontrado um usuário com o id informado");
        }

        return usuarioGateway.findById(idUsuario);
    }
}
