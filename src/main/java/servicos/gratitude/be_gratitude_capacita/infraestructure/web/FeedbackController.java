package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.feedback.CriarFeedbackCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.EncontrarCursoPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.CriarFeedbackUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.ListarFeedbacksPorCurso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
    private final CriarFeedbackUseCase criarFeedbackUseCase;
    private final ListarFeedbacksPorCurso listarFeedbacksPorCurso;
    private final EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase;

    public FeedbackController(CriarFeedbackUseCase criarFeedbackUseCase, ListarFeedbacksPorCurso listarFeedbacksPorCurso, EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase) {
        this.criarFeedbackUseCase = criarFeedbackUseCase;
        this.listarFeedbacksPorCurso = listarFeedbacksPorCurso;
        this.encontrarCursoPorIdUseCase = encontrarCursoPorIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Feedback> cadastrarFeedback(
            @RequestBody CriarFeedbackCommand request
    ){
        try {
            Curso curso = encontrarCursoPorIdUseCase.execute(request.idCurso());
            Feedback feedback = criarFeedbackUseCase.execute(request, curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        } catch (ConflitoException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e
            );
        }
    }

    @PostMapping("/{idCurso}")
    public ResponseEntity<List<Feedback>> listarFeedbackPorCurso(
            @PathVariable Integer idCurso
    ){
        try {
            Curso curso = encontrarCursoPorIdUseCase.execute(idCurso);
            List<Feedback> feedbacks = listarFeedbacksPorCurso.execute(curso);

            if (feedbacks.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(feedbacks);
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        } catch (ConflitoException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e
            );
        }
    }
}
