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
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.ListarApostilaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.ListarVideoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.ListarTentativaPorMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.ListarQuestoesPorAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.ListarRespostasDoUsuarioUseCase;
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
    private final ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;
    private final ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;
    private final ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase;
    private final ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;
    private final ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase;
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
        ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase,
        ListarVideoPorCursoUseCase listarVideoPorCursoUseCase,
        ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase,
        ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase,
        ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase,
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
        this.listarApostilaPorCursoUseCase = listarApostilaPorCursoUseCase;
        this.listarVideoPorCursoUseCase = listarVideoPorCursoUseCase;
        this.listarAvaliacaoPorCursoUseCase = listarAvaliacaoPorCursoUseCase;
        this.listarQuestoesPorAvaliacaoUseCase = listarQuestoesPorAvaliacaoUseCase;
        this.listarRespostasDoUsuarioUseCase = listarRespostasDoUsuarioUseCase;
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

            // pré-calcula materiais totais do curso (apostilas + videos + questões de avaliações)
            int totalApostilas = 0;
            int totalVideos = 0;
            int totalQuestoes = 0;
            try {
                totalApostilas = listarApostilaPorCursoUseCase.execute(fkCurso).size();
            } catch (Exception e) {
                totalApostilas = 0;
            }
            try {
                totalVideos = listarVideoPorCursoUseCase.execute(fkCurso).size();
            } catch (Exception e) {
                totalVideos = 0;
            }
            try {
                java.util.List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = listarAvaliacaoPorCursoUseCase.execute(fkCurso);
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
            final int materiaisTotaisDoCurso = totalApostilas + totalVideos + totalQuestoes;

            List<ParticipanteCursoResponse> participantes = matriculas.stream().map(matricula -> {
                Usuario usuario = matricula.getUsuario();
                // Quantidade de materiais concluídos para esta matrícula (usado no front como "materiaisConcluidos")
                int materiaisConcluidos;
                try {
                    List<MaterialAluno> materiais = listarMaterialPorMatriculaUseCase.execute(matricula);
                    materiaisConcluidos = (int) materiais.stream()
                            .filter(mat -> Boolean.TRUE.equals(mat.getFinalizado()))
                            .count();

                    // também conta questões respondidas pelo usuário neste curso (cada questão conta como 1 material)
                    try {
                        List<servicos.gratitude.be_gratitude_capacita.core.domain.RespostaDoUsuario> respostas = listarRespostasDoUsuarioUseCase.execute(usuario);
                        long respostasNoCurso = respostas.stream()
                                .filter(r -> r != null && r.getTentativa() != null && r.getTentativa().getMatricula() != null && r.getTentativa().getMatricula().getCurso() != null && r.getTentativa().getMatricula().getCurso().getIdCurso() != null && r.getTentativa().getMatricula().getCurso().getIdCurso().equals(curso.getIdCurso()))
                                .map(r -> {
                                    if (r.getAlternativa() != null && r.getAlternativa().getQuestao() != null && r.getAlternativa().getQuestao().getIdQuestaoComposto() != null) {
                                        return r.getAlternativa().getQuestao().getIdQuestaoComposto();
                                    }
                                    return null;
                                })
                                .filter(java.util.Objects::nonNull)
                                .distinct()
                                .count();

                        materiaisConcluidos += (int) respostasNoCurso;
                    } catch (Exception ex){
                        // ignora falhas na leitura de respostas
                    }
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

                // Última tentativa do usuário neste curso (para nota exibida no front)
                Integer ultimaNotaAcertos = null;
                Integer ultimaNotaTotal = null;
                try {
                    List<Tentativa> tentativas = listarTentativaPorMatriculaUseCase.execute(matricula);
                    if (tentativas != null && !tentativas.isEmpty()) {
                        tentativas.sort((a, b) -> {
                            java.time.LocalDateTime da = a.getDtTentativa();
                            java.time.LocalDateTime db = b.getDtTentativa();
                            if (da == null && db == null) return 0;
                            if (da == null) return 1;
                            if (db == null) return -1;
                            return db.compareTo(da);
                        });
                        Tentativa last = tentativas.get(0);
                        if (last != null) {
                            ultimaNotaAcertos = last.getNotaAcertos();
                            ultimaNotaTotal = last.getNotaTotal();
                        }
                    }
                } catch (Exception e) {
                    // ignore
                }

                return new ParticipanteCursoResponse(
                    usuario.getIdUsuario(),
                    usuario.getNome(),
                    materiaisConcluidos,
                    materiaisTotaisDoCurso,
                    avaliacao,
                    ultimoAcesso,
                    ultimaNotaAcertos,
                    ultimaNotaTotal
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

    @GetMapping("/usuario/{fkUsuario}/cursos")
    public ResponseEntity<List<servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.UsuarioCursoResponse>> listarCursosPorUsuario(@PathVariable Integer fkUsuario) {
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(fkUsuario);
            List<Matricula> matriculas = listarMatriculaPorUsuarioUseCase.execute(usuario);

            List<servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.UsuarioCursoResponse> resp = matriculas.stream().map(m -> {
                String nomeCurso = null;
                if (m.getCurso() != null) {
                    nomeCurso = m.getCurso().getTituloCurso();
                    if (nomeCurso == null && m.getCurso().getIdCurso() != null) nomeCurso = "Curso " + m.getCurso().getIdCurso();
                }

                // cálculo simples de progresso: materiaisConcluidos / materiaisTotais (quando disponíveis)
                String progresso = null;
                try {
                    List<MaterialAluno> materiais = listarMaterialPorMatriculaUseCase.execute(m);
                    int materiaisConcluidos = (int) materiais.stream().filter(mat -> Boolean.TRUE.equals(mat.getFinalizado())).count();
                    int materiaisTotais = materiais.size();
                    if (materiaisTotais > 0) {
                        progresso = String.format("%d%% Concluído", (int) Math.round((materiaisConcluidos * 100.0) / materiaisTotais));
                    }
                } catch (Exception e) {
                    progresso = null;
                }

                java.time.LocalDateTime iniciado = m.getDtInscricao();
                java.time.LocalDateTime finalizado = m.getDataFinalizacao();

                return new servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.UsuarioCursoResponse(
                        nomeCurso,
                        progresso == null ? "Incompleto" : progresso,
                        iniciado,
                        finalizado
                );
            }).collect(Collectors.toList());

            if (resp.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(resp);
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