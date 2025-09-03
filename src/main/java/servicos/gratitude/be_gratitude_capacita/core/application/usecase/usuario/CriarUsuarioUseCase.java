package servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario;

import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.CriarUsuarioCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CargoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

import java.time.LocalDateTime;

public class CriarUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    private final CargoGateway cargoGateway;

    public CriarUsuarioUseCase(UsuarioGateway usuarioGateway, CargoGateway cargoGateway) {
        this.usuarioGateway = usuarioGateway;
        this.cargoGateway = cargoGateway;
    }

    public Usuario execute (CriarUsuarioCommand command){
        if (usuarioGateway.existsByEmail(command.email())){
            throw new ConflitoException("Já existe um usuário com o email informado");

        } else if (usuarioGateway.existsByCpf(command.cpf())) {
            throw new ConflitoException("Já existe um usuário com o email informado");

        } else if (!cargoGateway.existsById(command.idCargo())) {
            throw new NaoEncontradoException("Não foi encontrado um usuário com o id informado");
        }

        Usuario usuario = new Usuario();
        Cargo cargo = cargoGateway.findById(command.idCargo());
        String senha = "#Gc" + command.cpf();

        usuario.setCpf(command.cpf());
        usuario.setEmail(command.email());
        usuario.setNome(command.nome());

        usuario.setSenha(senha);
        usuario.setDataEntrada(LocalDateTime.now());
        usuario.setFkCargo(cargo);

        return usuarioGateway.save(usuario);
    }
}
