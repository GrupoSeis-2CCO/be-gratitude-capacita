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

    public Curso execute(AtualizarCursoCommand command, Integer idCurso){

        if (!cursoGateway.existsById(idCurso)){
            throw new NaoEncontradoException("Não encontrado curso com id informado");
        }

        // Validation: titulo required and length constraints
        if (command.tituloCurso() == null || command.tituloCurso().trim().isEmpty()){
            throw new IllegalArgumentException("Título do curso é obrigatório.");
        }
        if (command.tituloCurso().length() > 100){
            throw new IllegalArgumentException("Título do curso deve ter no máximo 100 caracteres.");
        }

        if (command.descricao() != null && command.descricao().length() > 255){
            throw new IllegalArgumentException("Descrição do curso deve ter no máximo 255 caracteres.");
        }

        if (command.duracaoEstimada() != null && command.duracaoEstimada() < 0){
            throw new IllegalArgumentException("Duração estimada inválida.");
        }

        Curso cursoComMesmoTitulo = cursoGateway.findByTitulo(command.tituloCurso());

        if (Objects.nonNull(cursoComMesmoTitulo) && (cursoComMesmoTitulo.getIdCurso() != idCurso)){
            throw new ConflitoException("Já existe um curso com esse título");
        }

        Curso cursoNoBanco = cursoGateway.findById(idCurso);

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
