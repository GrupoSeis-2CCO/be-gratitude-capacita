package servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

public class DeletarMatriculaUseCase {
    private final MatriculaGateway matriculaGateway;

    public DeletarMatriculaUseCase(MatriculaGateway matriculaGateway) {
        this.matriculaGateway = matriculaGateway;
    }

    public void execute(MatriculaCompoundKey matriculaCompoundKey){
        if (!matriculaGateway.existsById(matriculaCompoundKey)){
            throw new NaoEncontradoException("Não foi encontrada matrícula para o id informado");
        }

        matriculaGateway.deleteById(matriculaCompoundKey);
    }
}
