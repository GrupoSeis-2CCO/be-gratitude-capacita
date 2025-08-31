package servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys;

public class MatriculaCompoundKey {
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
}
