package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

import java.time.LocalDateTime;

public class UsuarioCursoResponse {
    private String nomeCurso;
    private String progressoCurso;
    private LocalDateTime iniciado;
    private LocalDateTime finalizado;

    public UsuarioCursoResponse() {}

    public UsuarioCursoResponse(String nomeCurso, String progressoCurso, LocalDateTime iniciado, LocalDateTime finalizado) {
        this.nomeCurso = nomeCurso;
        this.progressoCurso = progressoCurso;
        this.iniciado = iniciado;
        this.finalizado = finalizado;
    }

    public String getNomeCurso() { return nomeCurso; }
    public String getProgressoCurso() { return progressoCurso; }
    public LocalDateTime getIniciado() { return iniciado; }
    public LocalDateTime getFinalizado() { return finalizado; }

    public void setNomeCurso(String nomeCurso) { this.nomeCurso = nomeCurso; }
    public void setProgressoCurso(String progressoCurso) { this.progressoCurso = progressoCurso; }
    public void setIniciado(LocalDateTime iniciado) { this.iniciado = iniciado; }
    public void setFinalizado(LocalDateTime finalizado) { this.finalizado = finalizado; }
}
