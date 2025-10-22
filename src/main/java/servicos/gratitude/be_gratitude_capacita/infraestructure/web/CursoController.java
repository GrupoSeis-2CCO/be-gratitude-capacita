package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.AtualizarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.CriarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.PublicarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.ListarVideoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.ListarApostilaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.ListarQuestoesPorAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.ListarMatriculaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.MaterialResponse;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.PagedResponse;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.CursoResponse;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.request.PublicarCursoRequest;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.EncontrarCursoPorIdUseCase;

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
    private final servicos.gratitude.be_gratitude_capacita.infraestructure.service.CascadeDeletionService cascadeDeletionService;
    private final ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;
    private final ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;
    private final ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase;
    private final ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;
    private final ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase;
    private final EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase;
    private final PublicarCursoUseCase publicarCursoUseCase;

    public CursoController(
            CriarCursoUseCase criarCursoUseCase,
            ListarCursoUseCase listarCursoUseCase,
            ListarCursoPaginadoUseCase listarCursoPaginadoUseCase,
            AtualizarCursoUseCase atualizarCursoUseCase,
            AtualizarOcultoUseCase atualizarOcultoUseCase,
            DeletarCursoUseCase deletarCursoUseCase,
            servicos.gratitude.be_gratitude_capacita.infraestructure.service.CascadeDeletionService cascadeDeletionService,
            ListarVideoPorCursoUseCase listarVideoPorCursoUseCase,
            ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase,
            ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase,
            ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase,
            ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase,
            EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase,
            PublicarCursoUseCase publicarCursoUseCase) {
        this.criarCursoUseCase = criarCursoUseCase;
        this.listarCursoUseCase = listarCursoUseCase;
        this.listarCursoPaginadoUseCase = listarCursoPaginadoUseCase;
        this.atualizarCursoUseCase = atualizarCursoUseCase;
        this.atualizarOcultoUseCase = atualizarOcultoUseCase;
    this.deletarCursoUseCase = deletarCursoUseCase;
    this.cascadeDeletionService = cascadeDeletionService;
        this.listarVideoPorCursoUseCase = listarVideoPorCursoUseCase;
        this.listarApostilaPorCursoUseCase = listarApostilaPorCursoUseCase;
        this.listarAvaliacaoPorCursoUseCase = listarAvaliacaoPorCursoUseCase;
        this.listarQuestoesPorAvaliacaoUseCase = listarQuestoesPorAvaliacaoUseCase;
        this.listarMatriculaPorCursoUseCase = listarMatriculaPorCursoUseCase;
        this.encontrarCursoPorIdUseCase = encontrarCursoPorIdUseCase;
        this.publicarCursoUseCase = publicarCursoUseCase;
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
                // Return the URL to the stored PDF so the frontend can render/download without
                // requiring the single-material endpoint (which may 404 on mixed id spaces)
                out.add(new MaterialResponse(
                        a.getIdApostila(),
                        "apostila",
                        a.getNomeApostilaOriginal(),
                        a.getDescricaoApostila(),
                        a.getUrlArquivo(),
                        a.getOrdemApostila()
                ));
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

    @GetMapping("/{idCurso}/materiais/paginated")
    public ResponseEntity<PagedResponse<MaterialResponse>> listarMateriaisDoCursoPaginado(
        @PathVariable Integer idCurso,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) Integer limit
    ) {
        try {
            if (page < 0) page = 0;
            if (size <= 0) size = 10;

            List<servicos.gratitude.be_gratitude_capacita.core.domain.Video> videos = listarVideoPorCursoUseCase.execute(idCurso);
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Apostila> apostilas = listarApostilaPorCursoUseCase.execute(idCurso);
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = listarAvaliacaoPorCursoUseCase.execute(idCurso);

            List<MaterialResponse> out = new java.util.ArrayList<>();
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Video v : videos) {
                out.add(new MaterialResponse(v.getIdVideo(), "video", v.getNomeVideo(), v.getDescricaoVideo(), v.getUrlVideo(), v.getOrdemVideo()));
            }
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Apostila a : apostilas) {
                out.add(new MaterialResponse(
                        a.getIdApostila(),
                        "apostila",
                        a.getNomeApostilaOriginal(),
                        a.getDescricaoApostila(),
                        a.getUrlArquivo(),
                        a.getOrdemApostila()
                ));
            }
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao av : avaliacoes) {
                out.add(new MaterialResponse(av.getIdAvaliacao(), "avaliacao", "Avaliação #" + av.getIdAvaliacao(), null, null));
            }

            if (out.isEmpty()) return ResponseEntity.noContent().build();

            int effSize = (limit != null && limit > 0) ? limit : size;
            int startIndex = (offset != null && offset >= 0) ? offset : (page * effSize);

            int start = Math.min(startIndex, out.size());
            int end = Math.min(start + effSize, out.size());
            List<MaterialResponse> slice = out.subList(start, end);
            int respPage = (offset != null && limit != null && limit > 0) ? (offset / limit) : page;
            PagedResponse<MaterialResponse> resp = new PagedResponse<>(slice, respPage, effSize, out.size());
            return ResponseEntity.ok(resp);
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
        } catch (DataIntegrityViolationException e) {
            String msg = "Dados inválidos: um ou mais campos excedem o tamanho máximo permitido.";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg, e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage() != null ? e.getMessage() : "Requisição inválida para cadastro do curso.", e);
        } catch (ResponseStatusException e) {
            throw e; // rethrow explicit status exceptions above
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao cadastrar curso", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<CursoResponse>> listarCursos() {
        List<Curso> cursos = listarCursoUseCase.execute();

        if (cursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<CursoResponse> resp = new java.util.ArrayList<>();
        for (Curso c : cursos) {
            CursoResponse r = new CursoResponse();
            r.setIdCurso(c.getIdCurso());
            r.setTituloCurso(c.getTituloCurso());
            r.setDescricao(c.getDescricao());
            r.setImagem(c.getImagem());
            r.setOcultado(c.getOcultado());
            r.setDuracaoEstimada(c.getDuracaoEstimada());

            // compute totals: apostilas + videos + questoes
            int totalApostilas = 0;
            int totalVideos = 0;
            int totalQuestoes = 0;
            try {
                totalApostilas = listarApostilaPorCursoUseCase.execute(c.getIdCurso()).size();
            } catch (Exception e) {
                totalApostilas = 0;
            }
            try {
                totalVideos = listarVideoPorCursoUseCase.execute(c.getIdCurso()).size();
            } catch (Exception e) {
                totalVideos = 0;
            }
            try {
                List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = listarAvaliacaoPorCursoUseCase.execute(c.getIdCurso());
                for (servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao av : avaliacoes) {
                    try {
                        totalQuestoes += listarQuestoesPorAvaliacaoUseCase.execute(av.getIdAvaliacao()).size();
                    } catch (Exception ex) {
                        // ignore per-avaliacao errors
                    }
                }
            } catch (Exception e) {
                totalQuestoes = 0;
            }
            r.setTotalMateriais(totalApostilas + totalVideos + totalQuestoes);

            // compute total alunos
            int totalAlunos = 0;
            try {
                if (c != null && c.getIdCurso() != null) {
                    // build a minimal Curso domain object for the use case
                    Curso cursoForMatriculas = new Curso();
                    cursoForMatriculas.setIdCurso(c.getIdCurso());
                    totalAlunos = listarMatriculaPorCursoUseCase.execute(cursoForMatriculas).size();
                }
            } catch (Exception e) {
                totalAlunos = 0;
            }
            r.setTotalAlunos(totalAlunos);

            resp.add(r);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp);
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

    @GetMapping("/{idCurso}/detalhes")
    public ResponseEntity<CursoResponse> obterDetalhesCurso(@PathVariable Integer idCurso) {
        try {
            // busca o curso e monta a resposta com campos computados
            Curso curso = encontrarCursoPorIdUseCase.execute(idCurso);

            CursoResponse r = new CursoResponse();
            r.setIdCurso(curso.getIdCurso());
            r.setTituloCurso(curso.getTituloCurso());
            r.setDescricao(curso.getDescricao());
            r.setImagem(curso.getImagem());
            r.setOcultado(curso.getOcultado());
            r.setDuracaoEstimada(curso.getDuracaoEstimada());

            int totalApostilas = 0;
            int totalVideos = 0;
            int totalQuestoes = 0;
            try {
                totalApostilas = listarApostilaPorCursoUseCase.execute(idCurso).size();
            } catch (Exception ignored) {}
            try {
                totalVideos = listarVideoPorCursoUseCase.execute(idCurso).size();
            } catch (Exception ignored) {}
            try {
                List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = listarAvaliacaoPorCursoUseCase.execute(idCurso);
                for (servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao av : avaliacoes) {
                    try {
                        totalQuestoes += listarQuestoesPorAvaliacaoUseCase.execute(av.getIdAvaliacao()).size();
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}
            r.setTotalMateriais(totalApostilas + totalVideos + totalQuestoes);

            int totalAlunos = 0;
            try {
                if (curso != null && curso.getIdCurso() != null) {
                    Curso cursoForMatriculas = new Curso();
                    cursoForMatriculas.setIdCurso(curso.getIdCurso());
                    totalAlunos = listarMatriculaPorCursoUseCase.execute(cursoForMatriculas).size();
                }
            } catch (Exception ignored) {}
            r.setTotalAlunos(totalAlunos);

            return ResponseEntity.ok(r);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter detalhes do curso", e);
        }
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
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Não foi possível atualizar: dados inválidos ou conflito de integridade.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage() != null ? e.getMessage() : "Requisição inválida para atualização do curso.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar curso", e);
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
            // Executa deleção total, removendo vínculos primeiro
            cascadeDeletionService.deleteCursoComVinculos(idCurso);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir curso", e);
        }
    }

    @PostMapping("/{idCurso}/publicar")
    public ResponseEntity<CursoResponse> publicarCurso(
            @PathVariable Integer idCurso,
            @RequestBody PublicarCursoRequest request) {
        try {
            PublicarCursoCommand command = new PublicarCursoCommand(
                idCurso,
                request.getNotificarTodos(),
                request.getIdsAlunosSelecionados()
            );

            Curso cursoPublicado = publicarCursoUseCase.execute(command);
            CursoResponse response = new CursoResponse();
            response.setIdCurso(cursoPublicado.getIdCurso());
            response.setTituloCurso(cursoPublicado.getTituloCurso());
            response.setDescricao(cursoPublicado.getDescricao());
            response.setImagem(cursoPublicado.getImagem());
            response.setOcultado(cursoPublicado.getOcultado());
            response.setDuracaoEstimada(cursoPublicado.getDuracaoEstimada());

            return ResponseEntity.ok(response);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao publicar curso: " + e.getMessage());
        }
    }
}
