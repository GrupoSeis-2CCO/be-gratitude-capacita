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

        if (cursoGateway.existsByTitulo(command.tituloCurso())){
            throw new ConflitoException("Um curso com este titulo já foi cadastrado");
        }

        Curso curso = new Curso();
        curso.setTituloCurso(command.tituloCurso());
        curso.setDescricao(command.descricao());
        curso.setImagem(command.imagem());
        curso.setDuracaoEstimada(command.duracaoEstimada());
        curso.setOcultado(true);

        // Define ordem como última + 1
        try {
            java.util.List<Curso> existentes = cursoGateway.findAll();
            int max = 0;
            for (Curso c : existentes) {
                if (c.getOrdemCurso() != null && c.getOrdemCurso() > max) {
                    max = c.getOrdemCurso();
                }
            }
            curso.setOrdemCurso(max + 1);
        } catch (Exception ignored) {
            curso.setOrdemCurso(1);
        }

        return cursoGateway.save(curso);
    }
}
