package servicos.gratitude.be_gratitude_capacita.infraestructure.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.TentativaEntityCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.QuestaoRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.AlternativaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.QuestaoEntityCompoundKey;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/exam")
public class ExamSubmissionController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExamSubmissionController.class);

    @Autowired
    private TentativaRepository tentativaRepository;
    @Autowired
    private RespostaDoUsuarioRepository respostaDoUsuarioRepository;
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    @Autowired
    private QuestaoRepository questaoRepository;
    @Autowired
    private AlternativaRepository alternativaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;



    @PostMapping("/{examId}/submit")
    @Transactional
    public ResponseEntity<?> submitExam(
            @PathVariable Integer examId,
            @RequestBody ExamSubmissionDTO submission
    ) {
        // 1. Buscar a matrícula do usuário no curso da avaliação
        Optional<AvaliacaoEntity> avaliacaoOpt = avaliacaoRepository.findById(examId);
        if (avaliacaoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Avaliação não encontrada"));
        }
        AvaliacaoEntity avaliacao = avaliacaoOpt.get();
        Integer cursoId = avaliacao.getFkCurso().getIdCurso();
        // Busca matrícula
        Optional<MatriculaEntity> matriculaOpt = matriculaRepository.findByFkUsuarioAndFkCurso(submission.userId, cursoId);
        MatriculaEntity matricula = null;
        if (matriculaOpt.isEmpty()) {
            // Para facilitar testes como "funcionário", tentamos criar automaticamente
            // uma matrícula temporária quando o usuário existe mas não está matriculado.
            log.warn("[ExamSubmission] Matrícula não encontrada para userId={}, cursoId={}; tentando criar matrícula automática para testes", submission.userId, cursoId);
            Optional<UsuarioEntity> userOpt = usuarioRepository.findById(submission.userId);
            if (userOpt.isPresent()) {
                // curso: podemos usar a entidade do avaliacao já carregada
                CursoEntity cursoEntity = avaliacao.getFkCurso();
                try {
                    MatriculaEntity m = new MatriculaEntity();
                    servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey key = new servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey();
                    key.setFkUsuario(submission.userId);
                    key.setFkCurso(cursoId);
                    m.setId(key);
                    m.setUsuario(userOpt.get());
                    m.setCurso(cursoEntity);
                    m.setDtInscricao(java.time.LocalDateTime.now());
                    m.setCompleto(false);
                    matricula = matriculaRepository.save(m);
                    log.info("[ExamSubmission] Matrícula automática criada para userId={} cursoId={}", submission.userId, cursoId);
                } catch (Exception e) {
                    log.error("[ExamSubmission] Falha ao criar matrícula automática:", e);
                    return ResponseEntity.badRequest().body(Map.of("error", "Matrícula não encontrada para o usuário e curso"));
                }
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Matrícula não encontrada para o usuário e curso"));
            }
        } else {
            matricula = matriculaOpt.get();
        }


        // 2. Gerar o próximo id_tentativa globalmente (a tabela tentativa tem PK somente em id_tentativa)
    int nextIdTentativa = Optional.ofNullable(tentativaRepository.findMaxTentativaId()).orElse(0) + 1;
    log.info("[ExamSubmission] nextIdTentativa={} (examId={}, userId={}, cursoId={})", nextIdTentativa, examId, submission.userId, cursoId);

        TentativaEntity tentativa = null;
        int attempts = 3;
        while (attempts-- > 0) {
            try {
                TentativaEntity t = new TentativaEntity();
                TentativaEntityCompoundKey tentativaKey = new TentativaEntityCompoundKey();
                tentativaKey.setIdMatriculaComposto(matricula.getId());
                tentativaKey.setIdTentativa(nextIdTentativa);
                t.setIdTentativaComposto(tentativaKey);
                t.setMatricula(matricula);
                t.setAvaliacao(avaliacao);
                t.setDtTentativa(LocalDateTime.now());
                tentativa = tentativaRepository.save(t);
                log.info("[ExamSubmission] tentativa salva id={} (user={}, curso={})", nextIdTentativa, submission.userId, cursoId);
                break;
            } catch (DataIntegrityViolationException dive) {
                // Se houve colisão de PK (Duplicate entry), recalcula o próximo id e tenta novamente
                if (dive.getMessage() != null && dive.getMessage().toLowerCase().contains("duplicate entry")) {
                    int prev = nextIdTentativa;
                    nextIdTentativa = Optional.ofNullable(tentativaRepository.findMaxTentativaId()).orElse(0) + 1;
                    log.warn("[ExamSubmission] Duplicate tentativa id {} -> retry with {}", prev, nextIdTentativa);
                    continue;
                }
                throw dive;
            }
        }
        if (tentativa == null) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Falha ao gerar id de tentativa"));
        }

        TentativaEntityCompoundKey tentativaKeyCompleto = tentativa.getIdTentativaComposto();


        // 3. Validar e salvar respostas
        // Note: frontend serializes object keys as strings in JSON ("1": 42). Accept string keys
        // and convert to Integer here to avoid 400 binding errors when mapping to Map<Integer,Integer>.
        if (submission.answers == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nenhuma resposta fornecida"));
        }
        for (Map.Entry<String, Integer> entry : submission.answers.entrySet()) {
            Integer questaoId;
            try {
                questaoId = Integer.valueOf(entry.getKey());
            } catch (NumberFormatException nfe) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid question id: " + entry.getKey()));
            }
            Integer alternativaId = entry.getValue();
            if (alternativaId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Alternativa nula para questão " + questaoId));
            }

            // Verificar que a questão pertence à avaliação
            QuestaoEntityCompoundKey qKey = new QuestaoEntityCompoundKey();
            qKey.setIdQuestao(questaoId);
            qKey.setFkAvaliacao(examId);
            Optional<QuestaoEntity> questaoOpt = questaoRepository.findById(qKey);
            if (questaoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", String.format("Questão %d não pertence à avaliação %d", questaoId, examId)));
            }

            // Verificar que alternativa existe e pertence à questão/avaliação
            Optional<AlternativaEntity> altOpt = alternativaRepository.findById(alternativaId);
            if (altOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", String.format("Alternativa %d não encontrada (questão %d)", alternativaId, questaoId)));
            }
            AlternativaEntity alternativa = altOpt.get();
            if (!questaoId.equals(alternativa.getFkQuestao()) || !examId.equals(alternativa.getFkAvaliacao())) {
                return ResponseEntity.badRequest().body(Map.of("error", String.format("Alternativa %d não pertence à questão %d na avaliação %d", alternativaId, questaoId, examId)));
            }

            RespostaDoUsuarioEntity resposta = new RespostaDoUsuarioEntity();
            RespostaDoUsuarioEntityCompoundKey respostaKey = new RespostaDoUsuarioEntityCompoundKey();
            // Use sempre a chave composta da tentativa gerenciada
            respostaKey.setIdTentativaComposto(tentativa.getIdTentativaComposto());
            respostaKey.setIdQuestao(questaoId);
            respostaKey.setIdAlternativa(alternativaId);
            respostaKey.setIdAvaliacao(examId);
            resposta.setRespostaDoUsuarioCompoundKey(respostaKey);
            resposta.setTentativa(tentativa); // referência gerenciada
            respostaDoUsuarioRepository.save(resposta);
        }

        return ResponseEntity.ok(Map.of(
                "status", "salvo",
                "examId", examId,
                "userId", submission.userId,
                "tentativaId", tentativa.getIdTentativaComposto(),
                "answers", submission.answers
        ));
    }

    public static class ExamSubmissionDTO {
        public Integer userId;
        // keys come as strings from JSON object keys, so use String -> Integer map and parse later
        public Map<String, Integer> answers;

        @Override
        public String toString() {
            return "ExamSubmissionDTO{" +
                    "userId=" + userId +
                    ", answers=" + answers +
                    '}';
        }
    }
}
