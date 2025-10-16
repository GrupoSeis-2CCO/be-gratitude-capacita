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
        System.out.println("[CriarAvaliacaoUseCase] fkCurso recebido: " + command.fkCurso());
        System.out.println("[CriarAvaliacaoUseCase] notaMinima recebida: " + command.notaMinima());
        Curso curso = cursoGateway.findById(command.fkCurso());

        if (Objects.isNull(curso)){
            throw new NaoEncontradoException("Não encontrado curso com o id informado");
        } else if (avaliacaoGateway.existsByCurso(curso)){
            throw new ConflitoException("Já existe uma avaliação no curso informado");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setFkCurso(curso);
        // Salvar notaMinima como acertosMinimos (ajuste conforme regra de negócio)
        if (command.notaMinima() == null) {
            throw new IllegalArgumentException("notaMinima não pode ser nula");
        }
        avaliacao.setAcertosMinimos(command.notaMinima().intValue());

        return avaliacaoGateway.save(avaliacao);
    }
}
