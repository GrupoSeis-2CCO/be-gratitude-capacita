package servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa;

import servicos.gratitude.be_gratitude_capacita.core.application.command.tentativa.CriarTentativaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class CriarTentativaUseCase {
    private final TentativaGateway tentativaGateway;

    public CriarTentativaUseCase(TentativaGateway tentativaGateway) {
        this.tentativaGateway = tentativaGateway;
    }

    public Tentativa execute(Matricula matricula, Avaliacao avaliacao, TentativaCompoundKey idTentativaComposto){
            if (
                    Objects.isNull(matricula) ||
                    Objects.isNull(avaliacao) ||
                    Objects.isNull(idTentativaComposto)
            ){
                throw new ValorInvalidoException("Valores inválidos para campos obrigátorios");
            }

            Tentativa tentativa = new Tentativa();

            tentativa.setDtTentativa(LocalDateTime.now());
            tentativa.setIdTentativaComposto(idTentativaComposto);
            tentativa.setAvaliacao(avaliacao);
            tentativa.setMatricula(matricula);

            return tentativaGateway.save(tentativa);
        }
}
