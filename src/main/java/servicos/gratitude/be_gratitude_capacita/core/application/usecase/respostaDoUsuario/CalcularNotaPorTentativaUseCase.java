package servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario;

import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.RespostaDoUsuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RespostaDoUsuarioGateway;

import java.util.List;

public class CalcularNotaPorTentativaUseCase {
    private final RespostaDoUsuarioGateway respostaDoUsuarioGateway;
    private final QuestaoGateway questaoGateway;

    public CalcularNotaPorTentativaUseCase(RespostaDoUsuarioGateway respostaDoUsuarioGateway, QuestaoGateway questaoGateway) {
        this.respostaDoUsuarioGateway = respostaDoUsuarioGateway;
        this.questaoGateway = questaoGateway;
    }

    /**
     * Returns a NotaResult with correct count, total count and percent.
     * Total is the number of questions in the avaliacao, not just answered questions.
     */
    public NotaResult execute(Tentativa tentativa) {
        if (tentativa == null) throw new ValorInvalidoException("Tentativa inválida");

        // Get total number of questions from the avaliacao
        int totalQuestoes = 0;
        if (tentativa.getAvaliacao() != null) {
            List<Questao> questoes = questaoGateway.findAllByAvaliacao(tentativa.getAvaliacao());
            totalQuestoes = questoes != null ? questoes.size() : 0;
        }

        // Get user's answers
        List<RespostaDoUsuario> respostas = respostaDoUsuarioGateway.findAllByTentativa(tentativa);
        
        // If no questions in avaliacao or no answers, return 0/0
        if (totalQuestoes == 0) return new NotaResult(0, 0, 0.0);
        
        // If no answers but there are questions, return 0/totalQuestoes
        if (respostas == null || respostas.isEmpty()) return new NotaResult(0, totalQuestoes, 0.0);

        // Count correct answers
        int corretas = 0;
        for (RespostaDoUsuario r : respostas) {
            if (r.getAlternativa() == null || r.getAlternativa().getQuestao() == null) continue;
            
            // compare alternativa id with questao.fkAlternativaCorreta
            Integer escolhidaId = r.getAlternativa().getAlternativaChaveComposta() != null 
                ? r.getAlternativa().getAlternativaChaveComposta().getIdAlternativa() 
                : null;
            Integer corretaId = r.getAlternativa().getQuestao().getFkAlternativaCorreta() != null 
                && r.getAlternativa().getQuestao().getFkAlternativaCorreta().getAlternativaChaveComposta() != null
                    ? r.getAlternativa().getQuestao().getFkAlternativaCorreta().getAlternativaChaveComposta().getIdAlternativa() 
                    : null;
            
            if (escolhidaId != null && corretaId != null && escolhidaId.equals(corretaId)) {
                corretas++;
            }
        }

        double percent = totalQuestoes > 0 ? ((double) corretas / (double) totalQuestoes) * 100.0 : 0.0;
        return new NotaResult(corretas, totalQuestoes, percent);
    }
}
