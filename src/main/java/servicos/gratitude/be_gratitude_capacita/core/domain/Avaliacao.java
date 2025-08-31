package servicos.gratitude.be_gratitude_capacita.core.domain;

public class Avaliacao {
    private Integer idAvaliacao;
    private Integer acertosMinimos;
    private Curso fkCurso;

    public Integer getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(Integer idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public Integer getAcertosMinimos() {
        return acertosMinimos;
    }

    public void setAcertosMinimos(Integer acertosMinimos) {
        this.acertosMinimos = acertosMinimos;
    }

    public Curso getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(Curso fkCurso) {
        this.fkCurso = fkCurso;
    }
}
