package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

import java.time.LocalDateTime;

public class TentativaComCursoResponse {
    private Integer idTentativa;
    private Integer fkUsuario;
    private Integer fkCurso;
    private String nomeCurso;
    private Integer fkAvaliacao;
    private LocalDateTime dtTentativa;
    private Integer notaAcertos;
    private Integer notaTotal;
    private Double nota;

    public TentativaComCursoResponse() {
    }

    public TentativaComCursoResponse(Integer idTentativa, Integer fkUsuario, Integer fkCurso, String nomeCurso, 
                                      Integer fkAvaliacao, LocalDateTime dtTentativa, 
                                      Integer notaAcertos, Integer notaTotal, Double nota) {
        this.idTentativa = idTentativa;
        this.fkUsuario = fkUsuario;
        this.fkCurso = fkCurso;
        this.nomeCurso = nomeCurso;
        this.fkAvaliacao = fkAvaliacao;
        this.dtTentativa = dtTentativa;
        this.notaAcertos = notaAcertos;
        this.notaTotal = notaTotal;
        this.nota = nota;
    }

    public Integer getIdTentativa() {
        return idTentativa;
    }

    public void setIdTentativa(Integer idTentativa) {
        this.idTentativa = idTentativa;
    }

    public Integer getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Integer fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public Integer getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(Integer fkCurso) {
        this.fkCurso = fkCurso;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Integer getFkAvaliacao() {
        return fkAvaliacao;
    }

    public void setFkAvaliacao(Integer fkAvaliacao) {
        this.fkAvaliacao = fkAvaliacao;
    }

    public LocalDateTime getDtTentativa() {
        return dtTentativa;
    }

    public void setDtTentativa(LocalDateTime dtTentativa) {
        this.dtTentativa = dtTentativa;
    }

    public Integer getNotaAcertos() {
        return notaAcertos;
    }

    public void setNotaAcertos(Integer notaAcertos) {
        this.notaAcertos = notaAcertos;
    }

    public Integer getNotaTotal() {
        return notaTotal;
    }

    public void setNotaTotal(Integer notaTotal) {
        this.notaTotal = notaTotal;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
