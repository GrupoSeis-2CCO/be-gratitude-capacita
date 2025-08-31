package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RespostaDoUsuarioEntityCompoundKey implements Serializable {
    private TentativaEntityCompoundKey idTentativaComposto;
    private AlternativaEntityCompoundKey idAlternativaComposto;

    public TentativaEntityCompoundKey getIdTentativaComposto() {
        return idTentativaComposto;
    }

    public void setIdTentativaComposto(TentativaEntityCompoundKey idTentativaComposto) {
        this.idTentativaComposto = idTentativaComposto;
    }

    public AlternativaEntityCompoundKey getIdAlternativaComposto() {
        return idAlternativaComposto;
    }

    public void setIdAlternativaComposto(AlternativaEntityCompoundKey idAlternativaComposto) {
        this.idAlternativaComposto = idAlternativaComposto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RespostaDoUsuarioEntityCompoundKey that = (RespostaDoUsuarioEntityCompoundKey) o;
        return Objects.equals(idTentativaComposto, that.idTentativaComposto) && Objects.equals(idAlternativaComposto, that.idAlternativaComposto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTentativaComposto, idAlternativaComposto);
    }
}
