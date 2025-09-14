package servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.RespostaDoUsuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.RespostaDoUsuarioCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RespostaDoUsuarioGateway;

import java.util.Objects;

public class CriarRespostaDoUsuarioUseCase {
    private final RespostaDoUsuarioGateway respostaDoUsuarioGateway;

    public CriarRespostaDoUsuarioUseCase(RespostaDoUsuarioGateway respostaDoUsuarioGateway) {
        this.respostaDoUsuarioGateway = respostaDoUsuarioGateway;
    }

    public RespostaDoUsuario execute(Tentativa tentativa, Alternativa alternativa, RespostaDoUsuarioCompoundKey respostaDoUsuarioIdComposto){
        if (Objects.isNull(tentativa) || Objects.isNull(alternativa) || Objects.isNull(respostaDoUsuarioIdComposto)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigátorios");
        } else if (respostaDoUsuarioGateway.existsById(respostaDoUsuarioIdComposto)){
            throw new ConflitoException("Já existe uma resposta com o id informado");
        }

        RespostaDoUsuario respostaDoUsuario = new RespostaDoUsuario();

        respostaDoUsuario.setRespostaDoUsuarioCompoundKey(respostaDoUsuarioIdComposto);
        respostaDoUsuario.setAlternativa(alternativa);
        respostaDoUsuario.setTentativa(tentativa);

        return respostaDoUsuarioGateway.save(respostaDoUsuario);
    }
}
