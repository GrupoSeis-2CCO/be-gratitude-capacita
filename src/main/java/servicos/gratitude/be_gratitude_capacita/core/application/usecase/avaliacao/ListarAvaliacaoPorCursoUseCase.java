package servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;

import java.util.List;

public class ListarAvaliacaoPorCursoUseCase {
    private final AvaliacaoGateway avaliacaoGateway;
    private final CursoGateway cursoGateway;

    public ListarAvaliacaoPorCursoUseCase(AvaliacaoGateway avaliacaoGateway, CursoGateway cursoGateway) {
        this.avaliacaoGateway = avaliacaoGateway;
        this.cursoGateway = cursoGateway;
    }

    public List<Avaliacao> execute(Integer fkCurso){
        if (!cursoGateway.existsById(fkCurso)){
            throw new ConflitoException("Não existe um curso com o id informado");
        }

        Curso curso = cursoGateway.findById(fkCurso);

        return avaliacaoGateway.findAllByCurso(curso);
    }
}
