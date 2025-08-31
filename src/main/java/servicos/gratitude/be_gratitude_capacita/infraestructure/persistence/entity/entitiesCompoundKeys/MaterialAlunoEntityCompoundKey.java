package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MaterialAlunoEntityCompoundKey implements Serializable {
    private Integer idMaterialAluno;
    private MatriculaEntityCompoundKey idMatriculaComposto;

    public Integer getIdMaterialAluno() {
        return idMaterialAluno;
    }

    public void setIdMaterialAluno(Integer idMaterialAluno) {
        this.idMaterialAluno = idMaterialAluno;
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
        MaterialAlunoEntityCompoundKey that = (MaterialAlunoEntityCompoundKey) o;
        return Objects.equals(idMaterialAluno, that.idMaterialAluno) && Objects.equals(idMatriculaComposto, that.idMatriculaComposto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMaterialAluno, idMatriculaComposto);
    }
}
