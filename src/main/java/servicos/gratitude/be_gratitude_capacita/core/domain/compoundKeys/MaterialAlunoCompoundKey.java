package servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys;

public class MaterialAlunoCompoundKey {
    private Integer idMaterialAluno;
    private MatriculaCompoundKey idMatriculaComposto;

    public Integer getIdMaterialAluno() {
        return idMaterialAluno;
    }

    public void setIdMaterialAluno(Integer idMaterialAluno) {
        this.idMaterialAluno = idMaterialAluno;
    }

    public MatriculaCompoundKey getIdMatriculaComposto() {
        return idMatriculaComposto;
    }

    public void setIdMatriculaComposto(MatriculaCompoundKey idMatriculaComposto) {
        this.idMatriculaComposto = idMatriculaComposto;
    }
}
