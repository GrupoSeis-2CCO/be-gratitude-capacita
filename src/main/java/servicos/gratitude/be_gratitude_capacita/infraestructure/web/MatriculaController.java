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

import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.ParticipanteCursoResponse;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno.ListarMaterialPorMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.ListarTentativaPorMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.ListarFeedbacksPorCurso;
import java.util.Collections;
import java.util.stream.Collectors;

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

    private final ListarMaterialPorMatriculaUseCase listarMaterialPorMatriculaUseCase;
    private final ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase;
    private final ListarFeedbacksPorCurso listarFeedbacksPorCurso;

    public MatriculaController(
            CriarMatriculaUseCase criarMatriculaUseCase,
            AtualizarUltimoAcessoMatriculaUseCase atualizarUltimoAcessoMatriculaUseCase,
            CompletarMatriculaUseCase completarMatriculaUseCase,
            DeletarMatriculaUseCase deletarMatriculaUseCase,
            ListarMatriculaPorUsuarioUseCase listarMatriculaPorUsuarioUseCase,
            ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase,
            BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
            EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase,
            ListarMaterialPorMatriculaUseCase listarMaterialPorMatriculaUseCase,
            ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase,
            ListarFeedbacksPorCurso listarFeedbacksPorCurso
    ) {
        this.criarMatriculaUseCase = criarMatriculaUseCase;
        this.atualizarUltimoAcessoMatriculaUseCase = atualizarUltimoAcessoMatriculaUseCase;
        this.completarMatriculaUseCase = completarMatriculaUseCase;
        this.deletarMatriculaUseCase = deletarMatriculaUseCase;
        this.listarMatriculaPorUsuarioUseCase = listarMatriculaPorUsuarioUseCase;
        this.listarMatriculaPorCursoUseCase = listarMatriculaPorCursoUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.encontrarCursoPorIdUseCase = encontrarCursoPorIdUseCase;
        this.listarMaterialPorMatriculaUseCase = listarMaterialPorMatriculaUseCase;
        this.listarTentativaPorMatriculaUseCase = listarTentativaPorMatriculaUseCase;
        this.listarFeedbacksPorCurso = listarFeedbacksPorCurso;
    }

    @GetMapping("/curso/{fkCurso}/participantes")
    public ResponseEntity<List<ParticipanteCursoResponse>> listarParticipantesPorCurso(@PathVariable Integer fkCurso) {
        try {
            Curso curso = encontrarCursoPorIdUseCase.execute(fkCurso);
            List<Matricula> matriculas = listarMatriculaPorCursoUseCase.execute(curso);
            List<servicos.gratitude.be_gratitude_capacita.core.domain.Feedback> _feedbacksTemp;
            try {
                _feedbacksTemp = listarFeedbacksPorCurso.execute(curso);
            } catch (Exception e) {
                _feedbacksTemp = Collections.emptyList();
            }
            final List<servicos.gratitude.be_gratitude_capacita.core.domain.Feedback> feedbacksDoCurso = _feedbacksTemp;

            List<ParticipanteCursoResponse> participantes = matriculas.stream().map(matricula -> {
                Usuario usuario = matricula.getUsuario();
                // Quantidade de materiais concluídos para esta matrícula (usado no front como "materiaisConcluidos")
                int materiaisConcluidos;
                try {
                    List<MaterialAluno> materiais = listarMaterialPorMatriculaUseCase.execute(matricula);
            materiaisConcluidos = (int) materiais.stream()
                .filter(mat -> Boolean.TRUE.equals(mat.getFinalizado()))
                .count();
                } catch (Exception e) {
                    materiaisConcluidos = 0;
                }

                // Último acesso
                java.time.LocalDateTime ultimoAcesso = matricula.getUltimoAcesso();

                // Avaliação média (média das estrelas dos feedbacks do curso para o usuário)
                Double avaliacao = null;
                if (ultimoAcesso != null) {
                    List<servicos.gratitude.be_gratitude_capacita.core.domain.Feedback> feedbacksUsuario = feedbacksDoCurso.stream()
                        .filter(f -> f.getFkUsuario() != null && f.getFkUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                        .collect(Collectors.toList());
                    if (!feedbacksUsuario.isEmpty()) {
                        avaliacao = feedbacksUsuario.stream()
                                .mapToInt(servicos.gratitude.be_gratitude_capacita.core.domain.Feedback::getEstrelas)
                                .average()
                                .orElse(Double.NaN);
                    }
                }

                return new ParticipanteCursoResponse(
                    usuario.getIdUsuario(),
                    usuario.getNome(),
                    materiaisConcluidos,
                    avaliacao,
                    ultimoAcesso
                );
            }).collect(Collectors.toList());
            if (participantes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(participantes);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
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