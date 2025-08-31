package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;

@Entity
public class RespostaDoUsuarioEntity {
    @EmbeddedId
    private RespostaDoUsuarioEntityCompoundKey respostaDoUsuarioEntityCompoundKey;

    @ManyToOne(optional = false)
    @MapsId("idTentativaComposto")
    private TentativaEntity tentativa;

    @OneToOne(optional = false)
    @MapsId("idAlternativaComposto")
    private AlternativaEntity alternativa;

    public RespostaDoUsuarioEntityCompoundKey getRespostaDoUsuarioCompoundKey() {
        return respostaDoUsuarioEntityCompoundKey;
    }

    public void setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioEntityCompoundKey respostaDoUsuarioEntityCompoundKey) {
        this.respostaDoUsuarioEntityCompoundKey = respostaDoUsuarioEntityCompoundKey;
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
