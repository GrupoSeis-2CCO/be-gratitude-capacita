package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.ListarVideoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.ListarApostilaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.ListarCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.MaterialResponse;

import java.util.List;
import java.util.ArrayList;

/**
 * Controller para listar todos os materiais (vídeos, apostilas, avaliações) de forma global.
 * Endpoint criado para suportar testes de carga e listagem geral no frontend.
 */
@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final ListarCursoUseCase listarCursoUseCase;
    private final ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;
    private final ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;
    private final ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase;

    public MaterialController(
            ListarCursoUseCase listarCursoUseCase,
            ListarVideoPorCursoUseCase listarVideoPorCursoUseCase,
            ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase,
            ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase) {
        this.listarCursoUseCase = listarCursoUseCase;
        this.listarVideoPorCursoUseCase = listarVideoPorCursoUseCase;
        this.listarApostilaPorCursoUseCase = listarApostilaPorCursoUseCase;
        this.listarAvaliacaoPorCursoUseCase = listarAvaliacaoPorCursoUseCase;
    }

    /**
     * Lista TODOS os materiais de TODOS os cursos.
     * 
     * GET /materiais
     * 
     * @return Lista consolidada de todos os vídeos, apostilas e avaliações
     */
    @GetMapping
    public ResponseEntity<List<MaterialResponse>> listarTodosOsMateriais() {
        try {
            List<MaterialResponse> allMaterials = new ArrayList<>();

            // Buscar todos os cursos
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Curso> cursos = listarCursoUseCase.execute();

            // Para cada curso, buscar seus materiais
            for (servicos.gratitude.be_gratitude_capacita.core.domain.Curso curso : cursos) {
                Integer idCurso = curso.getIdCurso();
                
                try {
                    // Videos
                    List<servicos.gratitude.be_gratitude_capacita.core.domain.Video> videos = 
                        listarVideoPorCursoUseCase.execute(idCurso);
                    for (servicos.gratitude.be_gratitude_capacita.core.domain.Video v : videos) {
                        allMaterials.add(new MaterialResponse(
                            v.getIdVideo(), 
                            "video", 
                            v.getNomeVideo(), 
                            v.getDescricaoVideo(), 
                            v.getUrlVideo(), 
                            v.getOrdemVideo()
                        ));
                    }

                    // Apostilas
                    List<servicos.gratitude.be_gratitude_capacita.core.domain.Apostila> apostilas = 
                        listarApostilaPorCursoUseCase.execute(idCurso);
                    for (servicos.gratitude.be_gratitude_capacita.core.domain.Apostila a : apostilas) {
                        allMaterials.add(new MaterialResponse(
                            a.getIdApostila(), 
                            "apostila", 
                            a.getNomeApostilaOriginal(), 
                            a.getDescricaoApostila(), 
                            a.getUrlArquivo(), 
                            a.getOrdemApostila()
                        ));
                    }

                    // Avaliações
                    List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = 
                        listarAvaliacaoPorCursoUseCase.execute(idCurso);
                    for (servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao av : avaliacoes) {
                        allMaterials.add(new MaterialResponse(
                            av.getIdAvaliacao(), 
                            "avaliacao", 
                            "Avaliação #" + av.getIdAvaliacao(), // Avaliação não tem titulo
                            "Mínimo de acertos: " + av.getAcertosMinimos(), 
                            null, // Avaliações não têm URL
                            0 // Avaliação não tem ordem
                        ));
                    }
                } catch (NaoEncontradoException e) {
                    // Se um curso não tem materiais, continua para o próximo
                    continue;
                }
            }

            if (allMaterials.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(allMaterials);

        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao listar materiais: " + e.getMessage(), 
                e
            );
        }
    }
}
