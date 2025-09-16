package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.matricula.CriarMatriculaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.EncontrarCursoPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.*;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {
    private final CriarMatriculaUseCase criarMatriculaUseCase;
    private final AtualizarUltimoAcessoMatriculaUseCase atualizarUltimoAcessoMatriculaUseCase;
    private final CompletarMatriculaUseCase completarMatriculaUseCase;
    private final DeletarMatriculaUseCase deletarMatriculaUseCase;
    private final ListarMatriculaPorUsuarioUseCase listarMatriculaPorUsuarioUseCase;
    private final ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase;
    private final EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    public MatriculaController(
            CriarMatriculaUseCase criarMatriculaUseCase,
            AtualizarUltimoAcessoMatriculaUseCase atualizarUltimoAcessoMatriculaUseCase,
            CompletarMatriculaUseCase completarMatriculaUseCase,
            DeletarMatriculaUseCase deletarMatriculaUseCase,
            ListarMatriculaPorUsuarioUseCase listarMatriculaPorUsuarioUseCase,
            ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase,
            BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
            EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase
    ) {
        this.criarMatriculaUseCase = criarMatriculaUseCase;
        this.atualizarUltimoAcessoMatriculaUseCase = atualizarUltimoAcessoMatriculaUseCase;
        this.completarMatriculaUseCase = completarMatriculaUseCase;
        this.deletarMatriculaUseCase = deletarMatriculaUseCase;
        this.listarMatriculaPorUsuarioUseCase = listarMatriculaPorUsuarioUseCase;
        this.listarMatriculaPorCursoUseCase = listarMatriculaPorCursoUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.encontrarCursoPorIdUseCase = encontrarCursoPorIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Matricula> cadastrarMatricula(
            @RequestBody CriarMatriculaCommand request
    ) {
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(request.fkUsuario());
            Curso curso = encontrarCursoPorIdUseCase.execute(request.fkCurso());

            MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
            idMatriculaComposto.setFkCurso(curso.getIdCurso());
            idMatriculaComposto.setFkUsuario(usuario.getIdUsuario());

            Matricula matricula = criarMatriculaUseCase.execute(request, idMatriculaComposto, usuario, curso);

            return ResponseEntity.status(HttpStatus.CREATED).body(matricula);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ConflitoException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping("/usuario/{fkUsuario}")
    public ResponseEntity<List<Matricula>> listarPorUsuario(@PathVariable Integer fkUsuario) {
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(fkUsuario);
            List<Matricula> matriculas = listarMatriculaPorUsuarioUseCase.execute(usuario);
            return ResponseEntity.ok(matriculas);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @GetMapping("/curso/{fkCurso}")
    public ResponseEntity<List<Matricula>> listarPorCurso(@PathVariable Integer fkCurso) {
        try {
            Curso curso = encontrarCursoPorIdUseCase.execute(fkCurso);
            List<Matricula> matriculas = listarMatriculaPorCursoUseCase.execute(curso);
            return ResponseEntity.ok(matriculas);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/atualizar-ultimo-acesso/{fkUsuario}/{fkCurso}")
    public ResponseEntity<Matricula> atualizarUltimoAcesso(
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        try {
            MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
            idMatriculaComposto.setFkCurso(fkCurso);
            idMatriculaComposto.setFkUsuario(fkUsuario);

            Matricula matricula = atualizarUltimoAcessoMatriculaUseCase.execute(idMatriculaComposto);

            return ResponseEntity.ok(matricula);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/completar/{fkUsuario}/{fkCurso}")
    public ResponseEntity<Matricula> completarMatricula(
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        try {
            MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
            idMatriculaComposto.setFkCurso(fkCurso);
            idMatriculaComposto.setFkUsuario(fkUsuario);

            Matricula matricula = completarMatriculaUseCase.execute(idMatriculaComposto);

            return ResponseEntity.ok(matricula);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{fkUsuario}/{fkCurso}")
    public ResponseEntity<Void> deletarMatricula(
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        try {
            MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
            idMatriculaComposto.setFkCurso(fkCurso);
            idMatriculaComposto.setFkUsuario(fkUsuario);

            deletarMatriculaUseCase.execute(idMatriculaComposto);

            return ResponseEntity.ok().build();
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}