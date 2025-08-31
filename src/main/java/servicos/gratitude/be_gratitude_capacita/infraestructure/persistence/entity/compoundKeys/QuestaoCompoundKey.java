package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class QuestaoCompoundKey implements Serializable {
    private Integer idQuestao;
    private Integer fkAvaliacao;

    public Integer getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(Integer idQuestao) {
        this.idQuestao = idQuestao;
    }

    public Integer getFkAvaliacao() {
        return fkAvaliacao;
    }

    public void setFkAvaliacao(Integer fkAvaliacao) {
        this.fkAvaliacao = fkAvaliacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestaoCompoundKey that = (QuestaoCompoundKey) o;
        return Objects.equals(idQuestao, that.idQuestao) && Objects.equals(fkAvaliacao, that.fkAvaliacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestao, fkAvaliacao);
    }
}
