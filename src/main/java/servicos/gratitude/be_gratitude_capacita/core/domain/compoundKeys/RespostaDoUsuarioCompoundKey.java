package servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys;

public class RespostaDoUsuarioCompoundKey {
    private TentativaCompoundKey idTentativaComposto;
    private AlternativaCompoundKey idAlternativaComposto;

    public TentativaCompoundKey getIdTentativaComposto() {
        return idTentativaComposto;
    }

    public void setIdTentativaComposto(TentativaCompoundKey idTentativaComposto) {
        this.idTentativaComposto = idTentativaComposto;
    }

    public AlternativaCompoundKey getIdAlternativaComposto() {
        return idAlternativaComposto;
    }

    public void setIdAlternativaComposto(AlternativaCompoundKey idAlternativaComposto) {
        this.idAlternativaComposto = idAlternativaComposto;
    }
}
