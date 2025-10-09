package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import java.io.Serializable;
import java.util.Objects;

public class FeedbackId implements Serializable {
    private Integer fkCurso;
    private Integer fkUsuario;

    public FeedbackId() {
    }

    public FeedbackId(Integer fkCurso, Integer fkUsuario) {
        this.fkCurso = fkCurso;
        this.fkUsuario = fkUsuario;
    }

    public Integer getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(Integer fkCurso) {
        this.fkCurso = fkCurso;
    }

    public Integer getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Integer fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackId that = (FeedbackId) o;
        return Objects.equals(fkCurso, that.fkCurso) && Objects.equals(fkUsuario, that.fkUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkCurso, fkUsuario);
    }
}
