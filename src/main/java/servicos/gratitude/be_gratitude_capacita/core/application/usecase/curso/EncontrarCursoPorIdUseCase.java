package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;

public class EncontrarCursoPorIdUseCase {
    private final CursoGateway cursoGateway;

    public EncontrarCursoPorIdUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public Curso execute(Integer idCurso){
        if (idCurso == null || idCurso<= 0){
          throw new ValorInvalidoException("Valores inválidos para campos obrigátorios");
        } else if (!cursoGateway.existsById(idCurso)){
            throw new NaoEncontradoException("Não encontrado curso com o id informado");
        }

        return cursoGateway.findById(idCurso);
    }
}
