package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

import java.util.List;

public class PesquisarPorNomeDeUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public PesquisarPorNomeDeUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> execute(String pesquisa){
        return usuarioGateway.findAllBySearch(pesquisa);
    }
}
