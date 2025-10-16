package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.ListarQuestoesPorAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.ListarRespostasDoUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.ListarAlternativasPorQuestaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.AnswerSheetResponse;

import java.util.*;

@RestController
@RequestMapping("/exams")
public class LegacyAnswerSheetController {
    private final ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase;
    private final ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase;
    private final AlternativaController alternativaController;

    public LegacyAnswerSheetController(
            ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase,
            BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
            ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase,
            ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase,
            AlternativaController alternativaController
    ) {
        this.listarQuestoesPorAvaliacaoUseCase = listarQuestoesPorAvaliacaoUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.listarRespostasDoUsuarioUseCase = listarRespostasDoUsuarioUseCase;
        this.listarAlternativasPorQuestaoUseCase = listarAlternativasPorQuestaoUseCase;
        this.alternativaController = alternativaController;
    }

    @GetMapping("/{examId}/answersheet/{userId}")
    public ResponseEntity<AnswerSheetResponse> getAnswerSheetLegacy(
            @PathVariable Integer examId,
            @PathVariable Integer userId
    ) {
        List<Questao> questoes = listarQuestoesPorAvaliacaoUseCase.execute(examId);
        Usuario usuario = buscarUsuarioPorIdUseCase.execute(userId);
        List<RespostaDoUsuario> respostas = listarRespostasDoUsuarioUseCase.execute(usuario);

        AnswerSheetResponse resp = new AnswerSheetResponse();
        resp.questions = new ArrayList<>();
        resp.userAnswers = new HashMap<>();
        resp.correctAnswers = new HashMap<>();

        if (questoes != null) {
            for (Questao q : questoes) {
                if (q == null || q.getIdQuestaoComposto() == null) continue;
                Integer qId = q.getIdQuestaoComposto().getIdQuestao();
                AnswerSheetResponse.QuestionDTO qdto = new AnswerSheetResponse.QuestionDTO();
                qdto.id = qId;
                qdto.text = q.getEnunciado();
                qdto.alternatives = new ArrayList<>();
                List<Alternativa> alternativas = listarAlternativasPorQuestaoUseCase.execute(q);
                if (alternativas != null) {
                    for (Alternativa alt : alternativas) {
                        if (alt == null || alt.getAlternativaChaveComposta() == null) continue;
                        AnswerSheetResponse.AlternativeDTO adto = new AnswerSheetResponse.AlternativeDTO();
                        adto.id = alt.getAlternativaChaveComposta().getIdAlternativa();
                        adto.text = alt.getTexto();
                        qdto.alternatives.add(adto);
                    }
                }
                resp.questions.add(qdto);
                if (q.getFkAlternativaCorreta() != null && q.getFkAlternativaCorreta().getAlternativaChaveComposta() != null) {
                    resp.correctAnswers.put(qdto.id, q.getFkAlternativaCorreta().getAlternativaChaveComposta().getIdAlternativa());
                }
            }
        }

        if (respostas != null && !respostas.isEmpty()) {
            Integer chosenAttemptId = null;
            java.time.LocalDateTime chosenAttemptDt = null;
            for (RespostaDoUsuario r : respostas) {
                if (r == null) continue;
                Alternativa alt = r.getAlternativa();
                Questao q = (alt != null) ? alt.getQuestao() : null;
                if (q == null || q.getIdQuestaoComposto() == null) continue;
                Integer fkAvaliacao = q.getIdQuestaoComposto().getFkAvaliacao();
                if (fkAvaliacao == null || !fkAvaliacao.equals(examId)) continue;

                Integer attemptId = null;
                java.time.LocalDateTime dt = null;
                Tentativa t = r.getTentativa();
                if (t != null && t.getIdTentativaComposto() != null) {
                    attemptId = t.getIdTentativaComposto().getIdTentativa();
                    dt = t.getDtTentativa();
                }
                if (attemptId == null && r.getRespostaDoUsuarioCompoundKey() != null && r.getRespostaDoUsuarioCompoundKey().getIdTentativaComposto() != null) {
                    attemptId = r.getRespostaDoUsuarioCompoundKey().getIdTentativaComposto().getIdTentativa();
                }
                if (attemptId == null) continue;

                if (chosenAttemptDt == null || (dt != null && dt.isAfter(chosenAttemptDt))) {
                    chosenAttemptDt = dt;
                    chosenAttemptId = attemptId;
                } else if (chosenAttemptDt == null && (chosenAttemptId == null || attemptId > chosenAttemptId)) {
                    chosenAttemptId = attemptId;
                }
            }

            if (chosenAttemptId != null) {
                for (RespostaDoUsuario r : respostas) {
                    if (r == null) continue;
                    Integer attemptId = null;
                    Tentativa t = r.getTentativa();
                    if (t != null && t.getIdTentativaComposto() != null) {
                        attemptId = t.getIdTentativaComposto().getIdTentativa();
                    } else if (r.getRespostaDoUsuarioCompoundKey() != null && r.getRespostaDoUsuarioCompoundKey().getIdTentativaComposto() != null) {
                        attemptId = r.getRespostaDoUsuarioCompoundKey().getIdTentativaComposto().getIdTentativa();
                    }
                    if (attemptId == null || !attemptId.equals(chosenAttemptId)) continue;
                    Alternativa alt = r.getAlternativa();
                    if (alt == null) continue;
                    Questao q = alt.getQuestao();
                    if (q == null || q.getIdQuestaoComposto() == null) continue;
                    Integer fkAvaliacao = q.getIdQuestaoComposto().getFkAvaliacao();
                    if (fkAvaliacao == null || !fkAvaliacao.equals(examId)) continue;
                    Integer qid = q.getIdQuestaoComposto().getIdQuestao();
                    Integer aid = (alt.getAlternativaChaveComposta() != null) ? alt.getAlternativaChaveComposta().getIdAlternativa() : null;
                    if (qid != null && aid != null) {
                        resp.userAnswers.put(qid, aid);
                    }
                }
            }
        }

        return ResponseEntity.ok(resp);
    }
}