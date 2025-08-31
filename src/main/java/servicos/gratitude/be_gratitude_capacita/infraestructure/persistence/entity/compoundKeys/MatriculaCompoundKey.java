package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MatriculaCompoundKey implements Serializable {
    private Integer fkUsuario;
    private Integer fkCurso;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatriculaCompoundKey that = (MatriculaCompoundKey) o;
        return Objects.equals(fkUsuario, that.fkUsuario) && Objects.equals(fkCurso, that.fkCurso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkUsuario, fkCurso);
    }
}
