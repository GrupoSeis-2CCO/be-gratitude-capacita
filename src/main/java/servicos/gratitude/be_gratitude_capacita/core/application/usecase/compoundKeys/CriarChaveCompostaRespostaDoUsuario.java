package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.*;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;

import java.util.List;
import java.util.Objects;

public class CriarChaveCompostaRespostaDoUsuario {
    private final TentativaGateway tentativaGateway;
    private final AlternativaGateway alternativaGateway;

    public CriarChaveCompostaRespostaDoUsuario(TentativaGateway tentativaGateway, AlternativaGateway alternativaGateway) {
        this.tentativaGateway = tentativaGateway;
        this.alternativaGateway = alternativaGateway;
    }

    public RespostaDoUsuarioCompoundKey execute(AlternativaCompoundKey alternativaIdComposto, TentativaCompoundKey tentativaIdComposto){
        if (Objects.isNull(alternativaIdComposto) || Objects.isNull(tentativaIdComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!alternativaGateway.existsById(alternativaIdComposto)){
            throw new NaoEncontradoException("Não foi encontrado alternativa para o id informado");
        } else if (!tentativaGateway.existsById(tentativaIdComposto)){
            throw new NaoEncontradoException("Não foi encontrado tentativa para o id informado");
        }

        RespostaDoUsuarioCompoundKey idComposto = new RespostaDoUsuarioCompoundKey();
        idComposto.setIdAlternativaComposto(alternativaIdComposto);
        idComposto.setIdTentativaComposto(tentativaIdComposto);

        return idComposto;
    }
}
