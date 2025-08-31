package servicos.gratitude.be_gratitude_capacita.core.application.command.usuario;

public record CriarUsuarioCommand(
        String nome,
        String cpf,
        String email,
        Integer idCargo
) {
}
