package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.RespostaDoUsuarioCompoundKey;

@Entity
public class RespostaDoUsuarioEntity {
    @EmbeddedId
    private RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey;

    @ManyToOne(optional = false)
    @MapsId("idTentativaComposto")
    private TentativaEntity tentativa;

    @OneToOne(optional = false)
    @MapsId("idAlternativaComposto")
    private AlternativaEntity alternativa;

    public RespostaDoUsuarioCompoundKey getRespostaDoUsuarioCompoundKey() {
        return respostaDoUsuarioCompoundKey;
    }

    public void setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey) {
        this.respostaDoUsuarioCompoundKey = respostaDoUsuarioCompoundKey;
    }

    public TentativaEntity getTentativa() {
        return tentativa;
    }

    public void setTentativa(TentativaEntity tentativa) {
        this.tentativa = tentativa;
    }

    public AlternativaEntity getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(AlternativaEntity alternativa) {
        this.alternativa = alternativa;
    }
}
