package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.CriarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

public class CriarCursoUseCase {

    private final CursoGateway cursoGateway;

    public CriarCursoUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public Curso execute(CriarCursoCommand command){

        if (cursoGateway.existsByTitulo(command.tituloCurso())){
            throw new ConflitoException("Um curso com este titulo já foi cadastrado");
        }

        Curso curso = new Curso();
        curso.setTituloCurso(command.tituloCurso());
        curso.setDescricao(command.descricao());
        curso.setImagem(command.imagem());
        curso.setDuracaoEstimada(command.duracaoEstimada());
        curso.setOcultado(true);

        return cursoGateway.save(curso);
    }
}
