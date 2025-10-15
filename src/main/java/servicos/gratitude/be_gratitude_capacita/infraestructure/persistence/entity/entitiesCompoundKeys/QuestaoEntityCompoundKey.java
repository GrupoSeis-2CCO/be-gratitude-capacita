package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class QuestaoEntityCompoundKey implements Serializable {
    @jakarta.persistence.Column(name = "id_questao")
    private Integer idQuestao;
    @jakarta.persistence.Column(name = "fk_avaliacao")
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
        QuestaoEntityCompoundKey that = (QuestaoEntityCompoundKey) o;
        return Objects.equals(idQuestao, that.idQuestao) && Objects.equals(fkAvaliacao, that.fkAvaliacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestao, fkAvaliacao);
    }
}
