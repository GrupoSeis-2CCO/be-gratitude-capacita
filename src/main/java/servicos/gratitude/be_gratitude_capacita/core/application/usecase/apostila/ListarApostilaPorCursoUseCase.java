package servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;

import java.util.List;

public class ListarApostilaPorCursoUseCase {
    private final ApostilaGateway apostilaGateway;
    private final CursoGateway cursoGateway;

    public ListarApostilaPorCursoUseCase(ApostilaGateway apostilaGateway, CursoGateway cursoGateway) {
        this.apostilaGateway = apostilaGateway;
        this.cursoGateway = cursoGateway;
    }

    public List<Apostila> execute(Integer fkCurso){
        if (!cursoGateway.existsById(fkCurso)){
            throw new ConflitoException("Não existe um curso com o id informado");
        }

        Curso curso = cursoGateway.findById(fkCurso);

        return apostilaGateway.findAllByCurso(curso);
    }
}
