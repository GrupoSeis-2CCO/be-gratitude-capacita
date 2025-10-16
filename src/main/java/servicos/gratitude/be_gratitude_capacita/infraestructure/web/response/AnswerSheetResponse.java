package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

import java.util.List;

public class AnswerSheetResponse {
    public static class QuestionDTO {
        public Integer id;
        public String text;
        public List<AlternativeDTO> alternatives;
    }
    public static class AlternativeDTO {
        public Integer id;
        public String text;
    }
    public List<QuestionDTO> questions;
    public java.util.Map<Integer, Integer> userAnswers;
    public java.util.Map<Integer, Integer> correctAnswers;
}
