package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

import java.util.List;

public class ExamDTO {
    public Integer idAvaliacao;
    public String nomeCurso;
    public List<QuestionDTO> questoes;

    public static class QuestionDTO {
        public Integer idQuestao;
        public String enunciado;
        public Integer numeroQuestao;
        public List<AlternativeDTO> alternativas;
    }

    public static class AlternativeDTO {
        public Integer idAlternativa;
        public String texto;
        public Integer ordem;
    }
}
