package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TentativaCompoundKey implements Serializable {
    private Integer idTentativa;
    private MatriculaCompoundKey idMatriculaComposto;

    public Integer getIdTentativa() {
        return idTentativa;
    }

    public void setIdTentativa(Integer idTentativa) {
        this.idTentativa = idTentativa;
    }

    public MatriculaCompoundKey getIdMatriculaComposto() {
        return idMatriculaComposto;
    }

    public void setIdMatriculaComposto(MatriculaCompoundKey idMatriculaComposto) {
        this.idMatriculaComposto = idMatriculaComposto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TentativaCompoundKey that = (TentativaCompoundKey) o;
        return Objects.equals(idTentativa, that.idTentativa) && Objects.equals(idMatriculaComposto, that.idMatriculaComposto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTentativa, idMatriculaComposto);
    }
}
