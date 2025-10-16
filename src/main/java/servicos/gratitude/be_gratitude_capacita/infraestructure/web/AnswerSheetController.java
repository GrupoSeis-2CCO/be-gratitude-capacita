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
@RequestMapping("/answersheet")
public class AnswerSheetController {
    private final ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase;
    private final ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase;
    private final AlternativaController alternativaController;
    private final servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.RespostaDoUsuarioRepository respostaDoUsuarioRepository;

    public AnswerSheetController(
            ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase,
            BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
            ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase,
            ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase,
            AlternativaController alternativaController,
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.RespostaDoUsuarioRepository respostaDoUsuarioRepository
    ) {
        this.listarQuestoesPorAvaliacaoUseCase = listarQuestoesPorAvaliacaoUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.listarRespostasDoUsuarioUseCase = listarRespostasDoUsuarioUseCase;
        this.listarAlternativasPorQuestaoUseCase = listarAlternativasPorQuestaoUseCase;
        this.alternativaController = alternativaController;
        this.respostaDoUsuarioRepository = respostaDoUsuarioRepository;
    }

    @GetMapping("/{examId}/{userId}")
    public ResponseEntity<AnswerSheetResponse> getAnswerSheet(
            @PathVariable Integer examId,
            @PathVariable Integer userId
    ) {
        System.out.println("[AnswerSheetController] Buscando gabarito: examId=" + examId + ", userId=" + userId);
        List<Questao> questoes = listarQuestoesPorAvaliacaoUseCase.execute(examId);
        System.out.println("[AnswerSheetController] Questões encontradas: " + (questoes != null ? questoes.size() : 0));
        
        // Buscar respostas diretamente do repositório
        List<servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.RespostaDoUsuarioEntity> respostasEntity = 
            respostaDoUsuarioRepository.findByUserIdAndExamId(userId, examId);
        System.out.println("[AnswerSheetController] Respostas encontradas no repositório: " + (respostasEntity != null ? respostasEntity.size() : 0));
        
        // Converter entities para domain
        List<RespostaDoUsuario> respostas = new ArrayList<>();
        if (respostasEntity != null) {
            for (var entity : respostasEntity) {
                if (entity != null && entity.getAlternativa() != null) {
                    RespostaDoUsuario resp = new RespostaDoUsuario();
                    resp.setRespostaDoUsuarioCompoundKey(null); // não precisamos da chave completa
                    
                    // Converter alternativa
                    Alternativa alt = new Alternativa();
                    if (entity.getAlternativa().getAlternativaChaveComposta() != null) {
                        servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey altKey = 
                            new servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey();
                        altKey.setIdAlternativa(entity.getAlternativa().getAlternativaChaveComposta().getIdAlternativa());
                        altKey.setIdQuestao(entity.getAlternativa().getAlternativaChaveComposta().getIdQuestao());
                        altKey.setIdAvaliacao(entity.getAlternativa().getAlternativaChaveComposta().getIdAvaliacao());
                        alt.setAlternativaChaveComposta(altKey);
                    }
                    resp.setAlternativa(alt);
                    
                    // Converter questão
                    if (entity.getAlternativa().getQuestao() != null) {
                        Questao q = new Questao();
                        if (entity.getAlternativa().getQuestao().getIdQuestaoComposto() != null) {
                            servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey qKey = 
                                new servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey();
                            qKey.setIdQuestao(entity.getAlternativa().getQuestao().getIdQuestaoComposto().getIdQuestao());
                            qKey.setFkAvaliacao(entity.getAlternativa().getQuestao().getIdQuestaoComposto().getFkAvaliacao());
                            q.setIdQuestaoComposto(qKey);
                        }
                        alt.setQuestao(q);
                    }
                    
                    respostas.add(resp);
                }
            }
        }
        System.out.println("[AnswerSheetController] Respostas do usuário convertidas: " + respostas.size());

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
                // Busca alternativas diretamente pelo use case para evitar exceções do controller
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
        // Mapear respostas do usuário diretamente (já foram filtradas por examId na query)
        if (respostas != null && !respostas.isEmpty()) {
            System.out.println("[AnswerSheetController] Processando " + respostas.size() + " respostas");
            for (RespostaDoUsuario r : respostas) {
                if (r == null) continue;
                Alternativa alt = r.getAlternativa();
                if (alt == null) continue;
                
                Questao q = alt.getQuestao();
                if (q == null || q.getIdQuestaoComposto() == null) continue;
                
                Integer qid = q.getIdQuestaoComposto().getIdQuestao();
                Integer aid = (alt.getAlternativaChaveComposta() != null) ? alt.getAlternativaChaveComposta().getIdAlternativa() : null;
                
                if (qid != null && aid != null) {
                    resp.userAnswers.put(qid, aid);
                    System.out.println("[AnswerSheetController] Adicionado resposta: questão " + qid + " -> alternativa " + aid);
                }
            }
        }
        System.out.println("[AnswerSheetController] Resposta final: questions=" + resp.questions.size() + 
                ", userAnswers=" + resp.userAnswers.size() + ", correctAnswers=" + resp.correctAnswers.size());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/debug/all-answers")
    public ResponseEntity<Map<String, Object>> debugAllAnswers() {
        List<RespostaDoUsuario> todasRespostas = listarRespostasDoUsuarioUseCase.execute(
            buscarUsuarioPorIdUseCase.execute(20) // usuario 20 como teste
        );
        Map<String, Object> debug = new HashMap<>();
        debug.put("totalRespostas", todasRespostas != null ? todasRespostas.size() : 0);
        debug.put("respostas", todasRespostas);
        return ResponseEntity.ok(debug);
    }
}
