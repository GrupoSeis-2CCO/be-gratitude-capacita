package servicos.gratitude.be_gratitude_capacita.infraestructure.web.request;

import java.util.List;

public class PublicarCursoRequest {
    private Boolean notificarTodos;
    private List<Integer> idsAlunosSelecionados;

    public PublicarCursoRequest() {}

    public PublicarCursoRequest(Boolean notificarTodos, List<Integer> idsAlunosSelecionados) {
        this.notificarTodos = notificarTodos;
        this.idsAlunosSelecionados = idsAlunosSelecionados;
    }

    public Boolean getNotificarTodos() { return notificarTodos; }
    public void setNotificarTodos(Boolean notificarTodos) { this.notificarTodos = notificarTodos; }

    public List<Integer> getIdsAlunosSelecionados() { return idsAlunosSelecionados; }
    public void setIdsAlunosSelecionados(List<Integer> idsAlunosSelecionados) { 
        this.idsAlunosSelecionados = idsAlunosSelecionados; 
    }
}
