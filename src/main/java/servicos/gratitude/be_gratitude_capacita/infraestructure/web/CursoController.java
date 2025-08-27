package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.CriarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.CriarCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CriarCursoUseCase criarCursoUseCase;

    public CursoController(CriarCursoUseCase criarCursoUseCase) {
        this.criarCursoUseCase = criarCursoUseCase;
    }

    @PostMapping
    public ResponseEntity<Curso> cadastrarCurso(
            @RequestBody CriarCursoCommand request
    ){
        return ResponseEntity.status(201).body(criarCursoUseCase.execute(request));
    }
}
