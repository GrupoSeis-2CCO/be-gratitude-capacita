package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.Objects;

public class AtualizarOcultoUseCase {

    private final CursoGateway cursoGateway;

    public AtualizarOcultoUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public Curso execute(Integer idCurso){
        Curso cursoParaAtualizar = cursoGateway.findById(idCurso);

        if (Objects.isNull(cursoParaAtualizar)){
            throw new NaoEncontradoException("Não foi encontrado um curso para o id informado");
        }

        Curso curso = new Curso();
        curso.setTituloCurso(cursoParaAtualizar.getTituloCurso());
        curso.setDescricao(cursoParaAtualizar.getDescricao());
        curso.setImagem(cursoParaAtualizar.getImagem());
        curso.setDuracaoEstimada(cursoParaAtualizar.getDuracaoEstimada());
        curso.setIdCurso(idCurso);
        curso.setOcultado(!cursoParaAtualizar.getOcultado());

        return cursoGateway.save(curso);
    }
}
