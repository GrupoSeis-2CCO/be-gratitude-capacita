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
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.ListarVideoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.ListarApostilaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.MaterialResponse;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CriarCursoUseCase criarCursoUseCase;
    private final ListarCursoUseCase listarCursoUseCase;
    private final ListarCursoPaginadoUseCase listarCursoPaginadoUseCase;
    private final AtualizarCursoUseCase atualizarCursoUseCase;
    private final AtualizarOcultoUseCase atualizarOcultoUseCase;
    private final DeletarCursoUseCase deletarCursoUseCase;
    private final ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;
    private final ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;
    private final ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase;

    public CursoController(
            CriarCursoUseCase criarCursoUseCase,
            ListarCursoUseCase listarCursoUseCase,
            ListarCursoPaginadoUseCase listarCursoPaginadoUseCase,
            AtualizarCursoUseCase atualizarCursoUseCase,
            AtualizarOcultoUseCase atualizarOcultoUseCase,
            DeletarCursoUseCase deletarCursoUseCase,
            ListarVideoPorCursoUseCase listarVideoPorCursoUseCase,
            ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase,
            ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase) {
        this.criarCursoUseCase = criarCursoUseCase;
        this.listarCursoUseCase = listarCursoUseCase;
        this.listarCursoPaginadoUseCase = listarCursoPaginadoUseCase;
        this.atualizarCursoUseCase = atualizarCursoUseCase;
        this.atualizarOcultoUseCase = atualizarOcultoUseCase;
        this.deletarCursoUseCase = deletarCursoUseCase;
        this.listarVideoPorCursoUseCase = listarVideoPorCursoUseCase;
        this.listarApostilaPorCursoUseCase = listarApostilaPorCursoUseCase;
        this.listarAvaliacaoPorCursoUseCase = listarAvaliacaoPorCursoUseCase;
    }

    @GetMapping("/{idCurso}/materiais")
    public ResponseEntity<List<MaterialResponse>> listarMateriaisDoCurso(@PathVariable Integer idCurso) {
        try {
            // Videos
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Video> videos = listarVideoPorCursoUseCase.execute(idCurso);
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Apostila> apostilas = listarApostilaPorCursoUseCase.execute(idCurso);
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = listarAvaliacaoPorCursoUseCase.execute(idCurso);

            List<MaterialResponse> out = new java.util.ArrayList<>();
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Video v : videos) {
                out.add(new MaterialResponse(v.getIdVideo(), "video", v.getNomeVideo(), v.getDescricaoVideo(), v.getUrlVideo(), v.getOrdemVideo()));
            }
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Apostila a : apostilas) {
                out.add(new MaterialResponse(a.getIdApostila(), "apostila", a.getNomeApostilaOriginal(), a.getDescricaoApostila(), null, a.getOrdemApostila()));
            }
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao av : avaliacoes) {
                out.add(new MaterialResponse(av.getIdAvaliacao(), "avaliacao", "Avaliação #" + av.getIdAvaliacao(), null, null));
            }

            if (out.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao listar materiais do curso", e);
        }
    }

    @GetMapping("/{idCurso}/materiais/{idMaterial}")
    public ResponseEntity<MaterialResponse> obterMaterialDoCurso(@PathVariable Integer idCurso, @PathVariable Integer idMaterial) {
        try {
            // Videos
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Video> videos = listarVideoPorCursoUseCase.execute(idCurso);
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Video v : videos) {
                if (v.getIdVideo() != null && v.getIdVideo().equals(idMaterial)) {
                    return ResponseEntity.ok(new MaterialResponse(v.getIdVideo(), "video", v.getNomeVideo(), v.getDescricaoVideo(), v.getUrlVideo()));
                }
            }

            // Apostilas
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Apostila> apostilas = listarApostilaPorCursoUseCase.execute(idCurso);
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Apostila a : apostilas) {
                if (a.getIdApostila() != null && a.getIdApostila().equals(idMaterial)) {
                    return ResponseEntity.ok(new MaterialResponse(a.getIdApostila(), "apostila", a.getNomeApostilaOriginal(), a.getDescricaoApostila(), a.getUrlArquivo()));
                }
            }

            // Avaliacoes
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = listarAvaliacaoPorCursoUseCase.execute(idCurso);
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao av : avaliacoes) {
                if (av.getIdAvaliacao() != null && av.getIdAvaliacao().equals(idMaterial)) {
                    return ResponseEntity.ok(new MaterialResponse(av.getIdAvaliacao(), "avaliacao", "Avaliação #" + av.getIdAvaliacao(), null, null));
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter material do curso", e);
        }
    }

    @PostMapping
    public ResponseEntity<Curso> cadastrarCurso(
            @RequestBody CriarCursoCommand request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarCursoUseCase.execute(request));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos() {
        List<Curso> cursos = listarCursoUseCase.execute();

        if (cursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(cursos);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Curso>> listarCursosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        Pageable pageable = Pageable.of(page, size, sortBy, sortDirection);
        Page<Curso> cursosPage = listarCursoPaginadoUseCase.execute(pageable);

        if (cursosPage.empty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(cursosPage);
    }

    @PutMapping("/{idCurso}")
    public ResponseEntity<Curso> atualizarCurso(
            @PathVariable Integer idCurso,
            @RequestBody AtualizarCursoCommand request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarCursoUseCase.execute(request, idCurso));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PutMapping("/atualizarOculto/{idCurso}")
    public ResponseEntity<Curso> atualizarOculto(
            @PathVariable Integer idCurso) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarOcultoUseCase.execute(idCurso));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{idCurso}")
    public ResponseEntity deletarCurso(
            @PathVariable Integer idCurso) {
        try {
            deletarCursoUseCase.execute(idCurso);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
