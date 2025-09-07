package servicos.gratitude.be_gratitude_capacita.core.domain;

public class Feedback {
    private Integer fkCurso;
    private Integer estrelas;
    private String motivo;
    private Usuario fkUsuario;
    private Curso curso;

    public Integer getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(Integer fkCurso) {
        this.fkCurso = fkCurso;
    }

    public Integer getEstrelas() {
        return estrelas;
    }

    public void setEstrelas(Integer estrelas) {
        this.estrelas = estrelas;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Usuario fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
