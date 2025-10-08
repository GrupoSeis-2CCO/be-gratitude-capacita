package servicos.gratitude.be_gratitude_capacita.core.application.usecase.acessos;

import servicos.gratitude.be_gratitude_capacita.core.application.command.acessos.CriarAcessoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Acesso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AcessoGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class CriarAcessoUseCase {
    private final AcessoGateway acessoGateway;

    public CriarAcessoUseCase(AcessoGateway acessoGateway) {
        this.acessoGateway = acessoGateway;
    }

    public Acesso execute(Usuario usuario, CriarAcessoCommand command){
        if (Objects.isNull(usuario) || Objects.isNull(command)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Acesso acesso = new Acesso();
        acesso.setFkusuario(usuario);
        acesso.setDataAcesso(LocalDateTime.now());

        return acessoGateway.save(acesso);
    }
}
