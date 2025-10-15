package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno.*;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.EncontrarMatriculaPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

import java.util.List;

@RestController
@RequestMapping({"/materiais-alunos", "/material-aluno"})
public class MaterialAlunoController {
    private final CriarMaterialAlunoUseCase criarMaterialAlunoUseCase;
    private final ListarMaterialPorMatriculaUseCase listarMaterialPorMatriculaUseCase;
    private final AtualizarUltimoAcessoMaterialUseCase atualizarUltimoAcessoMaterialUseCase;
    private final FinalizarMaterialUseCase finalizarMaterialUseCase;
    private final DeletarMaterialUseCase deletarMaterialUseCase;
    private final EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase;

    public MaterialAlunoController(
            CriarMaterialAlunoUseCase criarMaterialAlunoUseCase,
            ListarMaterialPorMatriculaUseCase listarMaterialPorMatriculaUseCase,
            AtualizarUltimoAcessoMaterialUseCase atualizarUltimoAcessoMaterialUseCase,
            FinalizarMaterialUseCase finalizarMaterialUseCase,
            DeletarMaterialUseCase deletarMaterialUseCase,
            EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase
    ) {
        this.criarMaterialAlunoUseCase = criarMaterialAlunoUseCase;
        this.listarMaterialPorMatriculaUseCase = listarMaterialPorMatriculaUseCase;
        this.atualizarUltimoAcessoMaterialUseCase = atualizarUltimoAcessoMaterialUseCase;
        this.finalizarMaterialUseCase = finalizarMaterialUseCase;
        this.deletarMaterialUseCase = deletarMaterialUseCase;
        this.encontrarMatriculaPorIdUseCase = encontrarMatriculaPorIdUseCase;
    }

    @GetMapping("/listar-por-matricula/{fkUsuario}/{fkCurso}")
    public ResponseEntity<List<MaterialAluno>> listarMateriaisPorMatricula(
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
        idMatriculaComposto.setFkUsuario(fkUsuario);
        idMatriculaComposto.setFkCurso(fkCurso);

        Matricula matricula = encontrarMatriculaPorIdUseCase.execute(idMatriculaComposto);
        List<MaterialAluno> materiais = listarMaterialPorMatriculaUseCase.execute(matricula);

        return ResponseEntity.ok(materiais);
    }

    @DeleteMapping("/{idMaterialAluno}/{fkUsuario}/{fkCurso}")
    public ResponseEntity<Void> deletarMaterialAluno(
            @PathVariable Integer idMaterialAluno,
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
        idMatriculaComposto.setFkUsuario(fkUsuario);
        idMatriculaComposto.setFkCurso(fkCurso);

        MaterialAlunoCompoundKey idMaterialAlunoComposto = new MaterialAlunoCompoundKey();
        idMaterialAlunoComposto.setIdMaterialAluno(idMaterialAluno);
        idMaterialAlunoComposto.setIdMatriculaComposto(idMatriculaComposto);

        deletarMaterialUseCase.execute(idMaterialAlunoComposto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/finalizar/{idMaterialAluno}/{fkUsuario}/{fkCurso}")
    public ResponseEntity<MaterialAluno> finalizarMaterialAluno(
            @PathVariable Integer idMaterialAluno,
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
        idMatriculaComposto.setFkUsuario(fkUsuario);
        idMatriculaComposto.setFkCurso(fkCurso);

        MaterialAlunoCompoundKey idMaterialAlunoComposto = new MaterialAlunoCompoundKey();
        idMaterialAlunoComposto.setIdMaterialAluno(idMaterialAluno);
        idMaterialAlunoComposto.setIdMatriculaComposto(idMatriculaComposto);

        MaterialAluno atualizado = finalizarMaterialUseCase.execute(idMaterialAlunoComposto);

        return ResponseEntity.ok(atualizado);
    }

    // Accept POST as well (some clients send POST instead of PUT) — delegate to the PUT handler
    @PostMapping("/finalizar/{idMaterialAluno}/{fkUsuario}/{fkCurso}")
    public ResponseEntity<MaterialAluno> finalizarMaterialAlunoPost(
            @PathVariable Integer idMaterialAluno,
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        return finalizarMaterialAluno(idMaterialAluno, fkUsuario, fkCurso);
    }


    @PutMapping("/finalizar-por-material/{tipo}/{idMaterial}/{fkUsuario}/{fkCurso}")
    public ResponseEntity<MaterialAluno> finalizarPorMaterial(
            @PathVariable String tipo,
            @PathVariable Integer idMaterial,
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        // build matricula key and retrieve matricula
        MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
        idMatriculaComposto.setFkUsuario(fkUsuario);
        idMatriculaComposto.setFkCurso(fkCurso);

        Matricula matricula = encontrarMatriculaPorIdUseCase.execute(idMatriculaComposto);

        // list materialAluno rows for this matricula and find the one matching the provided material id
        List<MaterialAluno> materiais = listarMaterialPorMatriculaUseCase.execute(matricula);

        MaterialAluno encontrado = materiais.stream().filter(m -> {
            if ("video".equalsIgnoreCase(tipo) && m.getFkVideo() != null && m.getFkVideo().getIdVideo() != null) {
                return m.getFkVideo().getIdVideo().equals(idMaterial);
            }
            if ("apostila".equalsIgnoreCase(tipo) && m.getFkApostila() != null && m.getFkApostila().getIdApostila() != null) {
                return m.getFkApostila().getIdApostila().equals(idMaterial);
            }
            return false;
        }).findFirst().orElse(null);

        if (encontrado == null) {
            // try to create a MaterialAluno record (user may not have had a tracking row)
            // determine whether it's a video or apostila and find the corresponding domain object
            servicos.gratitude.be_gratitude_capacita.core.domain.Video video = null;
            servicos.gratitude.be_gratitude_capacita.core.domain.Apostila apostila = null;

            if ("video".equalsIgnoreCase(tipo)) {
                // construct a lightweight Video domain with id set
                video = new servicos.gratitude.be_gratitude_capacita.core.domain.Video();
                video.setIdVideo(idMaterial);
            } else if ("apostila".equalsIgnoreCase(tipo)) {
                apostila = new servicos.gratitude.be_gratitude_capacita.core.domain.Apostila();
                apostila.setIdApostila(idMaterial);
            }

            // build a new MaterialAlunoCompoundKey with a generated id (null id will let the gateway assign one if supported)
            MaterialAlunoCompoundKey novoIdComposto = new MaterialAlunoCompoundKey();
            novoIdComposto.setIdMaterialAluno(null);
            novoIdComposto.setIdMatriculaComposto(idMatriculaComposto);

            MaterialAluno criado = criarMaterialAlunoUseCase.execute(novoIdComposto, video, apostila, matricula);

            // Some persistence setups (embedded id) may return the created domain with a null
            // id inside the compound key. In that case re-query the materials for the matricula
            // and locate the newly-created record to obtain the assigned id before finalizing.
            servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey idToFinalize = criado.getIdMaterialAlunoComposto();

            if (idToFinalize == null || idToFinalize.getIdMaterialAluno() == null) {
                // re-query materials for this matricula
                List<MaterialAluno> refreshed = listarMaterialPorMatriculaUseCase.execute(matricula);

                // Try to detect the newly-created record by comparing IDs (set difference)
                java.util.Set<Integer> existingIds = new java.util.HashSet<>();
                for (MaterialAluno m : materiais) {
                    try {
                        Integer existingId = m.getIdMaterialAlunoComposto() != null ? m.getIdMaterialAlunoComposto().getIdMaterialAluno() : null;
                        if (existingId != null) existingIds.add(existingId);
                    } catch (Exception ignored) {}
                }

                MaterialAluno found = null;
                for (MaterialAluno m : refreshed) {
                    try {
                        Integer candidateId = m.getIdMaterialAlunoComposto() != null ? m.getIdMaterialAlunoComposto().getIdMaterialAluno() : null;
                        if (candidateId != null && !existingIds.contains(candidateId)) {
                            found = m;
                            break;
                        }
                    } catch (Exception ignored) {}
                }

                // If set-diff didn't find it (mapper hides fkVideo/fkApostila), fall back to matching by referenced material id
                if (found == null) {
                    for (MaterialAluno m : refreshed) {
                        if ("video".equalsIgnoreCase(tipo) && m.getFkVideo() != null && m.getFkVideo().getIdVideo() != null) {
                            if (m.getFkVideo().getIdVideo().equals(idMaterial)) { found = m; break; }
                        }
                        if ("apostila".equalsIgnoreCase(tipo) && m.getFkApostila() != null && m.getFkApostila().getIdApostila() != null) {
                            if (m.getFkApostila().getIdApostila().equals(idMaterial)) { found = m; break; }
                        }
                    }
                }

                if (found != null) {
                    idToFinalize = found.getIdMaterialAlunoComposto();
                } else {
                    throw new NaoEncontradoException("Não foi possível localizar o material recém-criado para finalizar");
                }
            }

            // finalize the newly created record (using the compound id that includes the assigned id)
            MaterialAluno atualizado = finalizarMaterialUseCase.execute(idToFinalize);

            return ResponseEntity.ok(atualizado);
        }

        MaterialAluno atualizado = finalizarMaterialUseCase.execute(encontrado.getIdMaterialAlunoComposto());

        return ResponseEntity.ok(atualizado);
    }

    // Accept POST as well (some clients send POST instead of PUT) — delegate to the PUT handler
    @PostMapping("/finalizar-por-material/{tipo}/{idMaterial}/{fkUsuario}/{fkCurso}")
    public ResponseEntity<MaterialAluno> finalizarPorMaterialPost(
            @PathVariable String tipo,
            @PathVariable Integer idMaterial,
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        return finalizarPorMaterial(tipo, idMaterial, fkUsuario, fkCurso);
    }

    // Note: controller is reachable via both /materiais-alunos and /material-aluno (backwards-compatible)
}