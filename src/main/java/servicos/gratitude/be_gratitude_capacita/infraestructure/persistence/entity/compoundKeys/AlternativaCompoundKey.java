package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlternativaCompoundKey implements Serializable {
    private Integer idAlternativa;
    private QuestaoCompoundKey idQuestaoComposto;

    public Integer getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(Integer idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    public QuestaoCompoundKey getIdQuestaoComposto() {
        return idQuestaoComposto;
    }

    public void setIdQuestaoComposto(QuestaoCompoundKey idQuestaoComposto) {
        this.idQuestaoComposto = idQuestaoComposto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlternativaCompoundKey that = (AlternativaCompoundKey) o;
        return Objects.equals(idAlternativa, that.idAlternativa) && Objects.equals(idQuestaoComposto, that.idQuestaoComposto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlternativa, idQuestaoComposto);
    }
}
