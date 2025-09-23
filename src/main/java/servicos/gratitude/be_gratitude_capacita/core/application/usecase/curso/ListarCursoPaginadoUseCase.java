package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;

public class ListarCursoPaginadoUseCase {

    private final CursoGateway cursoGateway;

    public ListarCursoPaginadoUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public Page<Curso> execute(Pageable pageable) {
        return cursoGateway.findAll(pageable);
    }
}