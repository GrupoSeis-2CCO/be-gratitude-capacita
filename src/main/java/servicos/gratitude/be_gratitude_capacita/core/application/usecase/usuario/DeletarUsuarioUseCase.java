package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

public class DeletarUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public DeletarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public void execute (Integer idUsuario){
        if (!usuarioGateway.existsById(idUsuario)){
            throw new NaoEncontradoException("Não foi encontrado um usuário com o id informado");
        }

        usuarioGateway.deleteById(idUsuario);
    }
}
