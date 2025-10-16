package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.ListarQuestoesPorAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.ListarAlternativasPorQuestaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.ExamDTO;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exam")
public class ExamController {
    private final AvaliacaoGateway avaliacaoGateway;
    private final ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;
    private final ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase;

    public ExamController(
        AvaliacaoGateway avaliacaoGateway,
        ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase,
        ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase
    ) {
        this.avaliacaoGateway = avaliacaoGateway;
        this.listarQuestoesPorAvaliacaoUseCase = listarQuestoesPorAvaliacaoUseCase;
        this.listarAlternativasPorQuestaoUseCase = listarAlternativasPorQuestaoUseCase;
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamDTO> getExam(@PathVariable Integer examId) {
        Avaliacao avaliacao = avaliacaoGateway.findById(examId);
        if (avaliacao == null) {
            return ResponseEntity.notFound().build();
        }
        List<Questao> questoes = listarQuestoesPorAvaliacaoUseCase.execute(examId);
        ExamDTO dto = new ExamDTO();
        dto.idAvaliacao = avaliacao.getIdAvaliacao();
        dto.nomeCurso = avaliacao.getFkCurso() != null ? avaliacao.getFkCurso().toString() : null;
        dto.questoes = questoes.stream().map(q -> {
            ExamDTO.QuestionDTO qdto = new ExamDTO.QuestionDTO();
            qdto.idQuestao = q.getIdQuestaoComposto() != null ? q.getIdQuestaoComposto().getIdQuestao() : null;
            qdto.enunciado = q.getEnunciado();
            qdto.numeroQuestao = q.getNumeroQuestao();
            List<Alternativa> alternativas = listarAlternativasPorQuestaoUseCase.execute(q);
            qdto.alternativas = alternativas.stream().map(a -> {
                ExamDTO.AlternativeDTO adto = new ExamDTO.AlternativeDTO();
                adto.idAlternativa = a.getAlternativaChaveComposta() != null ? a.getAlternativaChaveComposta().getIdAlternativa() : null;
                adto.texto = a.getTexto();
                adto.ordem = a.getOrdem();
                return adto;
            }).collect(Collectors.toList());
            return qdto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
