package servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa;

import servicos.gratitude.be_gratitude_capacita.core.application.command.alternativa.AtualizarTextoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;

import java.util.Objects;

public class AtualizarAlternativaUseCase {
    private final AlternativaGateway alternativaGateway;

    public AtualizarAlternativaUseCase(AlternativaGateway alternativaGateway) {
        this.alternativaGateway = alternativaGateway;
    }

    public Alternativa execute(AlternativaCompoundKey idAlternativaComposto, AtualizarTextoCommand command){
        if (
                Objects.isNull(idAlternativaComposto) ||
                Objects.isNull(command) ||
                command.texto() == null ||
                command.texto().isBlank()
        ){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!alternativaGateway.existsById(idAlternativaComposto)){
            throw new NaoEncontradoException("Não foi encontrado alternativa para o id informado");
        }

        Alternativa alternativaParaAtualizar = alternativaGateway.findById(idAlternativaComposto);

        alternativaParaAtualizar.setTexto(command.texto());

        return alternativaGateway.save(alternativaParaAtualizar);
    }
}
