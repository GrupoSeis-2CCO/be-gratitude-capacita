package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.AtualizarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.CriarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CriarCursoUseCase criarCursoUseCase;
    private final ListarCursoUseCase listarCursoUseCase;
    private final AtualizarCursoUseCase atualizarCursoUseCase;
    private final AtualizarOcultoUseCase atualizarOcultoUseCase;
    private final DeletarCursoUseCase deletarCursoUseCase;

    public CursoController(
            CriarCursoUseCase criarCursoUseCase,
           ListarCursoUseCase listarCursoUseCase,
           AtualizarCursoUseCase atualizarCursoUseCase,
           AtualizarOcultoUseCase atualizarOcultoUseCase,
           DeletarCursoUseCase deletarCursoUseCase
    ) {
        this.criarCursoUseCase = criarCursoUseCase;
        this.listarCursoUseCase = listarCursoUseCase;
        this.atualizarCursoUseCase = atualizarCursoUseCase;
        this.atualizarOcultoUseCase = atualizarOcultoUseCase;
        this.deletarCursoUseCase = deletarCursoUseCase;
    }

    @PostMapping
    public ResponseEntity<Curso> cadastrarCurso(
            @RequestBody CriarCursoCommand request
    ){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarCursoUseCase.execute(request));
        } catch (ConflitoException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Já existe um curso com o titulo informado", e
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos(){
        List<Curso> cursos = listarCursoUseCase.execute();

        if (cursos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(cursos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizarCurso(
            @PathVariable Integer idCurso,
            @RequestBody AtualizarCursoCommand request
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarCursoUseCase.execute(request, idCurso));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Não foi encontrado o curso com o id informado", e
            );
        } catch (ConflitoException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Já existe um curso com o titulo informado", e
            );
        }
    }

    @PutMapping("/atualizarOculto/{id}")
    public ResponseEntity<Curso> atualizarOculto(
            @PathVariable Integer idCurso
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarOcultoUseCase.execute(idCurso));
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Não foi encontrado o curso com o id informado", e
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarCurso(
            @PathVariable Integer idCurso
    ) {
        try {
            deletarCursoUseCase.execute(idCurso);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Não foi encontrado o curso com o id informado", e
            );
        }
    }
}
