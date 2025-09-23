package servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;

public class ListarApostilaPorCursoPaginadoUseCase {

    private final ApostilaGateway apostilaGateway;
    private final CursoGateway cursoGateway;

    public ListarApostilaPorCursoPaginadoUseCase(ApostilaGateway apostilaGateway, CursoGateway cursoGateway) {
        this.apostilaGateway = apostilaGateway;
        this.cursoGateway = cursoGateway;
    }

    public Page<Apostila> execute(Integer cursoId, Pageable pageable) {
        Curso curso = cursoGateway.findById(cursoId);
        if (curso == null) {
            throw new NaoEncontradoException("Curso não encontrado com ID: " + cursoId);
        }

        return apostilaGateway.findAllByCurso(curso, pageable);
    }
}