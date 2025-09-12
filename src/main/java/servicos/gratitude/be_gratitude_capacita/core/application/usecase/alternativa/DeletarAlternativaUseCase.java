package servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;

import java.util.Objects;

public class DeletarAlternativaUseCase {
    private final AlternativaGateway alternativaGateway;

    public DeletarAlternativaUseCase(AlternativaGateway alternativaGateway) {
        this.alternativaGateway = alternativaGateway;
    }

    public void execute(AlternativaCompoundKey idAlternativaComposto){
        if (Objects.isNull(idAlternativaComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!alternativaGateway.existsById(idAlternativaComposto)){
            throw new NaoEncontradoException("Não foi encontrado alternativa para o id informado");
        }

        alternativaGateway.deleteById(idAlternativaComposto);
    }
}
