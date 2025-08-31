package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RespostaDoUsuarioCompoundKey implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RespostaDoUsuarioCompoundKey that = (RespostaDoUsuarioCompoundKey) o;
        return Objects.equals(idTentativaComposto, that.idTentativaComposto) && Objects.equals(idAlternativaComposto, that.idAlternativaComposto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTentativaComposto, idAlternativaComposto);
    }
}
