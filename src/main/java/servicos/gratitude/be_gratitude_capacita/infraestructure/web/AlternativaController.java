package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.alternativa.AtualizarTextoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.alternativa.CriarAlternativaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.AtualizarAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.CriarAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.DeletarAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.ListarAlternativasPorQuestaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.CriarNovaChaveCompostaAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaQuestaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.EncontrarQuestaoPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;

import java.util.List;

@RestController
@RequestMapping("/alternativas")
public class AlternativaController {
    private final AtualizarAlternativaUseCase atualizarAlternativaUseCase;
    private final CriarAlternativaUseCase criarAlternativaUseCase;
    private final DeletarAlternativaUseCase deletarAlternativaUseCase;
    private final ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase;
    private final CriarNovaChaveCompostaAlternativaUseCase criarChaveCompostaAlternativa;
    private final MontarChaveCompostaQuestaoUseCase montarChaveCompostaQuestaoUseCase;
    private final EncontrarQuestaoPorIdUseCase encontrarQuestaoPorIdUseCase;
    private final MontarChaveCompostaAlternativaUseCase montarChaveCompostaAlternativaUseCase;

    public AlternativaController(AtualizarAlternativaUseCase atualizarAlternativaUseCase, CriarAlternativaUseCase criarAlternativaUseCase, DeletarAlternativaUseCase deletarAlternativaUseCase, ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase, CriarNovaChaveCompostaAlternativaUseCase criarChaveCompostaAlternativa, MontarChaveCompostaQuestaoUseCase montarChaveCompostaQuestaoUseCase, EncontrarQuestaoPorIdUseCase encontrarQuestaoPorIdUseCase, MontarChaveCompostaAlternativaUseCase montarChaveCompostaAlternativaUseCase) {
        this.atualizarAlternativaUseCase = atualizarAlternativaUseCase;
        this.criarAlternativaUseCase = criarAlternativaUseCase;
        this.deletarAlternativaUseCase = deletarAlternativaUseCase;
        this.listarAlternativasPorQuestaoUseCase = listarAlternativasPorQuestaoUseCase;
        this.criarChaveCompostaAlternativa = criarChaveCompostaAlternativa;
        this.montarChaveCompostaQuestaoUseCase = montarChaveCompostaQuestaoUseCase;
        this.encontrarQuestaoPorIdUseCase = encontrarQuestaoPorIdUseCase;
        this.montarChaveCompostaAlternativaUseCase = montarChaveCompostaAlternativaUseCase;
    }

    @PostMapping
    public ResponseEntity<Alternativa> cadastrarAlternativa(
            @RequestBody CriarAlternativaCommand request
    ){
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(request.fkAvaliacao(), request.fkQuestao());
            AlternativaCompoundKey idAlternativaComposto = criarChaveCompostaAlternativa.execute(idQuestaoComposto);
            Alternativa alternativa = criarAlternativaUseCase.execute(request, idAlternativaComposto);
            return ResponseEntity.status(HttpStatus.CREATED).body(alternativa);
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

    @GetMapping("/{fkQuestao}/{fkAvaliacao}")
    public ResponseEntity<List<Alternativa>> listarAlternativaPorQuestao(
            @PathVariable Integer fkQuestao,
            @PathVariable Integer fkAvaliacao
    ){
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(fkAvaliacao, fkQuestao);
            Questao questao = encontrarQuestaoPorIdUseCase.execute(idQuestaoComposto);
            List<Alternativa> alternativas = listarAlternativasPorQuestaoUseCase.execute(questao);

            if (alternativas.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(alternativas);
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

    @PutMapping("/{idAlternativa}/{fkQuestao}/{fkAvaliacao}")
    public ResponseEntity<Alternativa> atualizarAlternativa(
            @RequestBody AtualizarTextoCommand texto,
            @PathVariable Integer idAlternativa,
            @PathVariable Integer fkQuestao,
            @PathVariable Integer fkAvaliacao
    ){
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(fkAvaliacao, fkQuestao);
            Questao questao = encontrarQuestaoPorIdUseCase.execute(idQuestaoComposto);
            AlternativaCompoundKey idAlternativaComposto = montarChaveCompostaAlternativaUseCase.execute(idQuestaoComposto, idAlternativa);
            Alternativa alternativaAtualizada = atualizarAlternativaUseCase.execute(idAlternativaComposto, texto);
            return ResponseEntity.status(HttpStatus.OK).body(alternativaAtualizada);
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

    @DeleteMapping("/{idAlternativa}/{fkQuestao}/{fkAvaliacao}")
    public ResponseEntity deletarAlternativa(
            @PathVariable Integer idAlternativa,
            @PathVariable Integer fkQuestao,
            @PathVariable Integer fkAvaliacao
    ){
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(fkAvaliacao, fkQuestao);
            Questao questao = encontrarQuestaoPorIdUseCase.execute(idQuestaoComposto);
            AlternativaCompoundKey idAlternativaComposto = montarChaveCompostaAlternativaUseCase.execute(idQuestaoComposto, idAlternativa);
            deletarAlternativaUseCase.execute(idAlternativaComposto);
            return ResponseEntity.status(HttpStatus.OK).build();
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
