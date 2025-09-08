package servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao;

import servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.CriarAvaliacaoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;

import java.util.Objects;

public class CriarAvaliacaoUseCase {
    private final AvaliacaoGateway avaliacaoGateway;
    private final CursoGateway cursoGateway;

    public CriarAvaliacaoUseCase(AvaliacaoGateway avaliacaoGateway, CursoGateway cursoGateway) {
        this.avaliacaoGateway = avaliacaoGateway;
        this.cursoGateway = cursoGateway;
    }

    public Avaliacao execute(CriarAvaliacaoCommand command){
        Curso curso = cursoGateway.findById(command.idCurso());

        if (Objects.isNull(curso)){
            throw new NaoEncontradoException("Não encontrado curso com o id informado");
        } else if (avaliacaoGateway.existsByCurso(curso)){
            throw new ConflitoException("Já existe uma avaliação no curso informado");
        }

        Avaliacao avaliacao = new Avaliacao();

        avaliacao.setFkCurso(curso);

        return avaliacaoGateway.save(avaliacao);
    }
}
