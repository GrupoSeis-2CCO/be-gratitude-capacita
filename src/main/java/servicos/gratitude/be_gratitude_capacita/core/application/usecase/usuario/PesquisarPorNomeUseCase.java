package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

import java.util.List;

public class PesquisarPorNomeUseCase {
    private final UsuarioGateway usuarioGateway;

    public PesquisarPorNomeUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> execute(String pesquisa){
        return usuarioGateway.findAllBySearch(pesquisa);
    }
}
