package servicos.gratitude.be_gratitude_capacita.core.domain;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import java.time.LocalDateTime;

public class Tentativa {
    private TentativaCompoundKey idTentativaComposto;
    private LocalDateTime dtTentativa;
    private Matricula matricula;
    private Avaliacao avaliacao;

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
}
