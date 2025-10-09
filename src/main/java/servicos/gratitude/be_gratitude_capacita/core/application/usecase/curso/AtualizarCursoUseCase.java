package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.AtualizarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.Objects;

public class AtualizarCursoUseCase {
    private final CursoGateway cursoGateway;

    public AtualizarCursoUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public Curso execute(AtualizarCursoCommand command, Long idCurso) {
        Integer idAsInt = idCurso == null ? null : idCurso.intValue();

        if (!cursoGateway.existsById(idAsInt)) {
            throw new NaoEncontradoException("Não encontrado curso com id informado");
        }

        Curso cursoComMesmoTitulo = cursoGateway.findByTitulo(command.tituloCurso());

        if (Objects.nonNull(cursoComMesmoTitulo) && !cursoComMesmoTitulo.getIdCurso().equals(idCurso)) {
            throw new ConflitoException("Já existe um curso com esse título");
        }

        Curso cursoNoBanco = cursoGateway.findById(idAsInt);

        Curso curso = new Curso();
        curso.setTituloCurso(command.tituloCurso());
        curso.setDescricao(command.descricao());
        curso.setImagem(command.imagem());
        curso.setDuracaoEstimada(command.duracaoEstimada());
        curso.setIdCurso(idCurso);
        curso.setOcultado(cursoNoBanco.getOcultado());

        return cursoGateway.save(curso);
    }
}
