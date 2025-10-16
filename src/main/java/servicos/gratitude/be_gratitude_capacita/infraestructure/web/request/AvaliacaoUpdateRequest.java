package servicos.gratitude.be_gratitude_capacita.infraestructure.web.request;

import java.util.List;

public class AvaliacaoUpdateRequest {
    public Integer acertosMinimos;
    public List<QuestaoUpdateRequest> questoes;

    public static class QuestaoUpdateRequest {
        public Integer idQuestao; // pode ser null para novas
        public String enunciado;
        public Integer numeroQuestao;
        public Integer fkAlternativaCorreta;
        public List<AlternativaUpdateRequest> alternativas;
    }

    public static class AlternativaUpdateRequest {
        public Integer idAlternativa; // pode ser null para novas
        public String texto;
        public Integer ordemAlternativa;
    }
}
