package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;

import java.util.List;
import java.util.Objects;

public class CriarNovaChaveCompostaTentativaUseCase {
    private final MatriculaGateway matriculaGateway;
    private final TentativaGateway tentativaGateway;

    public CriarNovaChaveCompostaTentativaUseCase(MatriculaGateway matriculaGateway, TentativaGateway tentativaGateway) {
        this.matriculaGateway = matriculaGateway;
        this.tentativaGateway = tentativaGateway;
    }

    public TentativaCompoundKey execute(MatriculaCompoundKey matriculaCompoundKey){
        if (Objects.isNull(matriculaCompoundKey)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        Matricula matricula = matriculaGateway.findById(matriculaCompoundKey);

        if (Objects.isNull(matricula)){
            throw new NaoEncontradoException("Não foi encontrado matricula para o id informado");
        }

        List<Tentativa> tentativas = tentativaGateway.findAllByMatricula(matricula);
        Integer maiorId = 0;

        if (!tentativas.isEmpty()){
            for (Tentativa tentativaDaVez : tentativas) {
                if (tentativaDaVez.getIdTentativaComposto().getIdTentativa() >= maiorId){
                    maiorId = tentativaDaVez.getIdTentativaComposto().getIdTentativa();
                }
            }
        }

        TentativaCompoundKey idComposto = new TentativaCompoundKey();
        idComposto.setIdMatriculaComposto(matriculaCompoundKey);
        idComposto.setIdTentativa(maiorId + 1);

        return idComposto;
    }
}
