package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AtualizarAcessoUseCase {
    private final UsuarioGateway usuarioGateway;

    public AtualizarAcessoUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(Integer idUsuario){
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
        usuarioAtualizado.setSenha(usuarioDoBanco.getSenha());

        usuarioAtualizado.setUltimoAcesso(LocalDateTime.now());

        return usuarioGateway.save(usuarioAtualizado);
    }
}
