package servicos.gratitude.be_gratitude_capacita.infraestructure.web.request;

import java.util.List;

public class CreateAvaliacaoRequest {
    public Integer fkCurso;
    public Double notaMinima;
    public List<AvaliacaoUpdateRequest.QuestaoUpdateRequest> questoes;
}
