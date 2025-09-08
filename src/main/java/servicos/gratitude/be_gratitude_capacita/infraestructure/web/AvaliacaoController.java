package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.CriarAvaliacaoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.DefinirAcertosMinimosCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.AtualizarAcertosMinimosAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.CriarAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    private final CriarAvaliacaoUseCase criarAvaliacaoUseCase;
    private final AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase;

    public AvaliacaoController(CriarAvaliacaoUseCase criarAvaliacaoUseCase, AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase) {
        this.criarAvaliacaoUseCase = criarAvaliacaoUseCase;
        this.atualizarAcertosMinimosAvaliacaoUseCase = atualizarAcertosMinimosAvaliacaoUseCase;
    }

    @PostMapping
    public ResponseEntity<Avaliacao> cadastrarAvaliacao(
            @RequestBody CriarAvaliacaoCommand request
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(criarAvaliacaoUseCase.execute(request));
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

    @PutMapping("/atualizar-acertos/{idAvaliacao}")
    public ResponseEntity<Avaliacao> atualizarAcertosMinimosAvaliacao(
            @RequestBody DefinirAcertosMinimosCommand request,
            @PathVariable Integer idAvaliacao
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarAcertosMinimosAvaliacaoUseCase.execute(idAvaliacao, request));
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        }
    }
}
