package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.apostila.AtualizarApostilaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.apostila.CriarApostilaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/apostilas")
public class ApostilaController {
    private final CriarApostilaUseCase criarApostilaUseCase;
    private final AtualizarDadosApostilaUseCase atualizarDadosApostilaUseCase;
    private final AtualizarOcultoApostilaUseCase atualizarOcultoApostilaUseCase;
    private final DeletarApostilaUseCase deletarApostilaUseCase;
    private final ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;
    private final ListarApostilaPorCursoPaginadoUseCase listarApostilaPorCursoPaginadoUseCase;

    public ApostilaController(CriarApostilaUseCase criarApostilaUseCase,
            AtualizarDadosApostilaUseCase atualizarDadosApostilaUseCase,
            AtualizarOcultoApostilaUseCase atualizarOcultoApostilaUseCase,
            DeletarApostilaUseCase deletarApostilaUseCase, ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase,
            ListarApostilaPorCursoPaginadoUseCase listarApostilaPorCursoPaginadoUseCase) {
        this.criarApostilaUseCase = criarApostilaUseCase;
        this.atualizarDadosApostilaUseCase = atualizarDadosApostilaUseCase;
        this.atualizarOcultoApostilaUseCase = atualizarOcultoApostilaUseCase;
        this.deletarApostilaUseCase = deletarApostilaUseCase;
        this.listarApostilaPorCursoUseCase = listarApostilaPorCursoUseCase;
        this.listarApostilaPorCursoPaginadoUseCase = listarApostilaPorCursoPaginadoUseCase;
    }

    @PostMapping
    public ResponseEntity<Apostila> cadastrarApostila(
            @RequestBody CriarApostilaCommand request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarApostilaUseCase.execute(request));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{fkCurso}")
    public ResponseEntity<List<Apostila>> listarApostilaPorCurso(
            @PathVariable Integer fkCurso) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(listarApostilaPorCursoUseCase.execute(fkCurso));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{fkCurso}/paginated")
    public ResponseEntity<Page<Apostila>> listarApostilaPorCursoPaginado(
            @PathVariable Integer fkCurso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        try {
            Pageable pageable = Pageable.of(page, size, sortBy, sortDirection);
            Page<Apostila> apostilasPage = listarApostilaPorCursoPaginadoUseCase.execute(fkCurso, pageable);

            if (apostilasPage.empty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(apostilasPage);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/atualizar-dados/{idApostila}")
    public ResponseEntity<Apostila> atualizarDadosApostila(
            @RequestBody AtualizarApostilaCommand request,
            @PathVariable Integer idApostila) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(atualizarDadosApostilaUseCase.execute(request, idApostila));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/atualizar-oculto/{idApostila}")
    public ResponseEntity<Apostila> atualizarOcultoApostila(
            @PathVariable Integer idApostila) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(atualizarOcultoApostilaUseCase.execute(idApostila));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{idAPostila}")
    public ResponseEntity deletarApostila(
            @PathVariable Integer idApostila) {
        try {
            deletarApostilaUseCase.execute(idApostila);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
