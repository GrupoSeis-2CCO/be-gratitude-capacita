package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.adapters.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.AtualizarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.Objects;

public class AtualizarCursoUseCase {
    private final CursoGateway gateway;

    public AtualizarCursoUseCase(CursoGateway gateway) {
        this.gateway = gateway;
    }

    public Curso execute(AtualizarCursoCommand command, Integer idCurso){

        if (!gateway.existsById(idCurso)){
            throw new NaoEncontradoException("Não encontrado curso com id informado");
        }

        Curso cursoComMesmoTitulo = gateway.findByTitulo(command.tituloCurso());

        if (!Objects.isNull(cursoComMesmoTitulo) && (cursoComMesmoTitulo.getIdCurso() != idCurso)){
            throw new ConflitoException("Já existe um curso com esse título");
        }

        Curso curso = new Curso();
        curso.setTituloCurso(command.tituloCurso());
        curso.setDescricao(command.descricao());
        curso.setImagem(command.imagem());
        curso.setDuracaoEstimada(command.duracaoEstimada());
        curso.setIdCurso(idCurso);
        curso.setOcultado(cursoComMesmoTitulo.getOcultado());

        return gateway.save(curso);
    }
}
