package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TentativaEntityCompoundKey implements Serializable {
    @jakarta.persistence.Column(name = "id_tentativa")
    private Integer idTentativa;
    private MatriculaEntityCompoundKey idMatriculaComposto;

    public Integer getIdTentativa() {
        return idTentativa;
    }

    public void setIdTentativa(Integer idTentativa) {
        this.idTentativa = idTentativa;
    }

    public MatriculaEntityCompoundKey getIdMatriculaComposto() {
        return idMatriculaComposto;
    }

    public void setIdMatriculaComposto(MatriculaEntityCompoundKey idMatriculaComposto) {
        this.idMatriculaComposto = idMatriculaComposto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TentativaEntityCompoundKey that = (TentativaEntityCompoundKey) o;
        return Objects.equals(idTentativa, that.idTentativa) && Objects.equals(idMatriculaComposto, that.idMatriculaComposto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTentativa, idMatriculaComposto);
    }
}
