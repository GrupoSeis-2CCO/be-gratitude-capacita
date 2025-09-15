package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

import java.util.List;
import java.util.Objects;

public class MontarChaveCompostaTentativaUseCase {
    private final MatriculaGateway matriculaGateway;

    public MontarChaveCompostaTentativaUseCase(MatriculaGateway matriculaGateway) {
        this.matriculaGateway = matriculaGateway;
    }

    public TentativaCompoundKey execute(MatriculaCompoundKey matriculaCompoundKey, Integer idTentativa){
        if (Objects.isNull(matriculaCompoundKey) || idTentativa == null || idTentativa <= 0){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Matricula matricula = matriculaGateway.findById(matriculaCompoundKey);

        if (Objects.isNull(matricula)){
            throw new NaoEncontradoException("Não foi encontrado matricula para o id informado");
        }

        TentativaCompoundKey idComposto = new TentativaCompoundKey();
        idComposto.setIdMatriculaComposto(matriculaCompoundKey);
        idComposto.setIdTentativa(idTentativa);

        return idComposto;
    }
}
