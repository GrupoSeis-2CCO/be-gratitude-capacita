package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlternativaEntityCompoundKey implements Serializable {
    private Integer idAlternativa;
    private QuestaoEntityCompoundKey idQuestaoComposto;

    public Integer getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(Integer idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    public QuestaoEntityCompoundKey getIdQuestaoComposto() {
        return idQuestaoComposto;
    }

    public void setIdQuestaoComposto(QuestaoEntityCompoundKey idQuestaoComposto) {
        this.idQuestaoComposto = idQuestaoComposto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlternativaEntityCompoundKey that = (AlternativaEntityCompoundKey) o;
        return Objects.equals(idAlternativa, that.idAlternativa) && Objects.equals(idQuestaoComposto, that.idQuestaoComposto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlternativa, idQuestaoComposto);
    }
}
