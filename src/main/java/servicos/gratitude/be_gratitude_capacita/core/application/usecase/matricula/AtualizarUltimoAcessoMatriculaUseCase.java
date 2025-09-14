package servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;

import java.time.LocalDateTime;

public class AtualizarUltimoAcessoMatriculaUseCase {
    private final MatriculaGateway matriculaGateway;

    public AtualizarUltimoAcessoMatriculaUseCase(MatriculaGateway matriculaGateway) {
        this.matriculaGateway = matriculaGateway;
    }

    public Matricula execute(MatriculaCompoundKey matriculaCompoundKey){
        if (!matriculaGateway.existsById(matriculaCompoundKey)){
            throw new NaoEncontradoException("Não foi encontrada matrícula para o id informado");
        }

        Matricula matriculaParaAtualizar = matriculaGateway.findById(matriculaCompoundKey);

        matriculaParaAtualizar.setUltimoAcesso(LocalDateTime.now());

        return matriculaGateway.save(matriculaParaAtualizar);
    }
}
