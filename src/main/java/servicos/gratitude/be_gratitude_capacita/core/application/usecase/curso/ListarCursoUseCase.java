package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.List;

public class ListarCursoUseCase {

    private final CursoGateway cursoGateway;

    public ListarCursoUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public List<Curso> execute(){
        return cursoGateway.findAll();
    }
}
