package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.AtualizarSenhaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

public class AtualizarSenhaUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public AtualizarSenhaUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(AtualizarSenhaCommand command, Integer idUsuario){
        if (!usuarioGateway.existsById(idUsuario)){
            throw new NaoEncontradoException("Não foi encontrado um usuário com o id informado");
        }

        Usuario usuarioDoBanco = usuarioGateway.findById(idUsuario);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setIdUsuario(usuarioDoBanco.getIdUsuario());
        usuarioAtualizado.setNome(usuarioDoBanco.getNome());
        usuarioAtualizado.setCpf(usuarioDoBanco.getCpf());
        usuarioAtualizado.setFkCargo(usuarioDoBanco.getFkCargo());
        usuarioAtualizado.setEmail(usuarioDoBanco.getEmail());
        usuarioAtualizado.setDataEntrada(usuarioDoBanco.getDataEntrada());
        usuarioAtualizado.setUltimoAcesso(usuarioDoBanco.getUltimoAcesso() );

        usuarioAtualizado.setSenha(command.senha());

        return usuarioAtualizado;
    }
}
