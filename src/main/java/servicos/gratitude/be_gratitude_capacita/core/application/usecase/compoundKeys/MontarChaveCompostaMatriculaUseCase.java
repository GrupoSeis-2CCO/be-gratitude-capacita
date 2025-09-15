package servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;

public class MontarChaveCompostaMatriculaUseCase {
    private final CursoGateway cursoGateway;
    private final UsuarioGateway usuarioGateway;

    public MontarChaveCompostaMatriculaUseCase(CursoGateway cursoGateway, UsuarioGateway usuarioGateway) {
        this.cursoGateway = cursoGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public MatriculaCompoundKey execute(Integer fkCurso, Integer fkUsuario){
        if (fkCurso == null || fkUsuario == null || fkCurso <= 0 || fkUsuario <= 0){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        } else if (!cursoGateway.existsById(fkCurso)){
            throw new NaoEncontradoException("Não foi encontrado curso com o id informado");
        } else if (!usuarioGateway.existsById(fkUsuario)){
            throw new NaoEncontradoException("Não foi encontrado usuário com o id informado");
        }

        MatriculaCompoundKey idComposto = new MatriculaCompoundKey();
        idComposto.setFkCurso(fkCurso);
        idComposto.setFkUsuario(fkUsuario);

        return idComposto;
    }
}
