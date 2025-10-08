package servicos.gratitude.be_gratitude_capacita.core.application.usecase.acessos;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Acesso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AcessoGateway;

import java.util.List;
import java.util.Objects;

public class ListarAcessosPorAlunoUseCase {
    private final AcessoGateway acessoGateway;

    public ListarAcessosPorAlunoUseCase(AcessoGateway acessoGateway) {
        this.acessoGateway = acessoGateway;
    }

    public List<Acesso> execute(Usuario usuario){
        if (Objects.isNull(usuario)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        return acessoGateway.findAllByUsuario(usuario);
    }
}
