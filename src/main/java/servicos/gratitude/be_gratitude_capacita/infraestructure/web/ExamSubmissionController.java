package servicos.gratitude.be_gratitude_capacita.infraestructure.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.TentativaEntityCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.*;
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



    @PostMapping("/{examId}/submit")
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
        if (matriculaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Matrícula não encontrada para o usuário e curso"));
        }
        MatriculaEntity matricula = matriculaOpt.get();


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


        // 3. Salvar respostas
        for (Map.Entry<Integer, Integer> entry : submission.answers.entrySet()) {
            Integer questaoId = entry.getKey();
            Integer alternativaId = entry.getValue();
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
        public Map<Integer, Integer> answers;

        @Override
        public String toString() {
            return "ExamSubmissionDTO{" +
                    "userId=" + userId +
                    ", answers=" + answers +
                    '}';
        }
    }
}
