package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;

public class DeletarCursoUseCase {

    private final CursoGateway cursoGateway;

    public DeletarCursoUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public void execute(Integer idCurso){
        if (!cursoGateway.existsById(idCurso)){
            throw new NaoEncontradoException("Não foi encontrando um curso com o id informado");
        }

        cursoGateway.deleteById(idCurso);
    }
}
