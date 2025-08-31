package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.adapters.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;

public class DeletarCursoUseCase {

    private final CursoGateway gateway;

    public DeletarCursoUseCase(CursoGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(Integer idCurso){
        if (!gateway.existsById(idCurso)){
            throw new NaoEncontradoException("Não foi encontrando um curso com o id informado");
        }

        gateway.deleteById(idCurso);
    }
}
