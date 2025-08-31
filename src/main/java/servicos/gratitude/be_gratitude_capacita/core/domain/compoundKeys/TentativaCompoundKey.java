package servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys;

public class TentativaCompoundKey {
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
}
