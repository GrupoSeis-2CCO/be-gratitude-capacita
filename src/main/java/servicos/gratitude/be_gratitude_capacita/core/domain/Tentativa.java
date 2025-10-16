package servicos.gratitude.be_gratitude_capacita.core.domain;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import java.time.LocalDateTime;

public class Tentativa {
    private TentativaCompoundKey idTentativaComposto;
    private LocalDateTime dtTentativa;
    private Matricula matricula;
    private Avaliacao avaliacao;
    private Double nota;
    private Integer notaAcertos;
    private Integer notaTotal;

    public TentativaCompoundKey getIdTentativaComposto() {
        return idTentativaComposto;
    }

    public void setIdTentativaComposto(TentativaCompoundKey idTentativaComposto) {
        this.idTentativaComposto = idTentativaComposto;
    }

    public LocalDateTime getDtTentativa() {
        return dtTentativa;
    }

    public void setDtTentativa(LocalDateTime dtTentativa) {
        this.dtTentativa = dtTentativa;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
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
}
