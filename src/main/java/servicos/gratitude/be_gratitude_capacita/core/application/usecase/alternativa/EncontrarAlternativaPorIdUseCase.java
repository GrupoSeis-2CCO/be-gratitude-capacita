package servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;

import java.util.Objects;

public class EncontrarAlternativaPorIdUseCase {
    private final AlternativaGateway alternativaGateway;

    public EncontrarAlternativaPorIdUseCase(AlternativaGateway alternativaGateway) {
        this.alternativaGateway = alternativaGateway;
    }

    public Alternativa execute(AlternativaCompoundKey idAlternativaComposto){
        if (Objects.isNull(idAlternativaComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Alternativa alternativa = alternativaGateway.findById(idAlternativaComposto);

        if (Objects.isNull(alternativa)){
            throw new NaoEncontradoException("Não encontrada alternatia para o id informado");
        }

        return alternativa;
    }
}
