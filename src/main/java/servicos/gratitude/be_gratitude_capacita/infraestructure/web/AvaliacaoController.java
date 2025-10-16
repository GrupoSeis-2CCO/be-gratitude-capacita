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
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    private final CriarAvaliacaoUseCase criarAvaliacaoUseCase;
    private final AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase;
    private final AvaliacaoGateway avaliacaoGateway;

    public AvaliacaoController(CriarAvaliacaoUseCase criarAvaliacaoUseCase, AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase, AvaliacaoGateway avaliacaoGateway) {
        this.criarAvaliacaoUseCase = criarAvaliacaoUseCase;
        this.atualizarAcertosMinimosAvaliacaoUseCase = atualizarAcertosMinimosAvaliacaoUseCase;
        this.avaliacaoGateway = avaliacaoGateway;
    }
    @GetMapping
    public ResponseEntity<?> listarAvaliacoes() {
        try {
            return ResponseEntity.ok(avaliacaoGateway.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar avaliações", e);
        }
    }

    @PostMapping
    public ResponseEntity<Avaliacao> cadastrarAvaliacao(
            @RequestBody CriarAvaliacaoCommand request
    ){
        try {
            System.out.println("[AvaliacaoController] fkCurso recebido: " + request.fkCurso());
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
