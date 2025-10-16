package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

import java.util.List;

public class AvaliacaoCompletaResponse {
    public Integer idAvaliacao;
    public Integer acertosMinimos;
    public Long idCurso;
    public List<QuestaoResponse> questoes;

    public static class QuestaoResponse {
        public Integer idQuestao;
        public String enunciado;
        public Integer numeroQuestao;
        public Integer fkAlternativaCorreta;
        public List<AlternativaResponse> alternativas;
    }

    public static class AlternativaResponse {
        public Integer idAlternativa;
        public String texto;
        public Integer ordemAlternativa;
    }
}
