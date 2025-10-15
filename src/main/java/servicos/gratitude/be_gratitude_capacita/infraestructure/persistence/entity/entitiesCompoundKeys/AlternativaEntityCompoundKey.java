package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlternativaEntityCompoundKey implements Serializable {
    @jakarta.persistence.Column(name = "id_alternativa")
    private Integer idAlternativa;
    @jakarta.persistence.Column(name = "FK_questao")
    private Integer idQuestao;
    @jakarta.persistence.Column(name = "FK_avaliacao")
    private Integer idAvaliacao;

    public Integer getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(Integer idAlternativa) {
        this.idAlternativa = idAlternativa;
    }
    public Integer getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(Integer idQuestao) {
        this.idQuestao = idQuestao;
    }

    public Integer getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(Integer idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlternativaEntityCompoundKey that = (AlternativaEntityCompoundKey) o;
        return Objects.equals(idAlternativa, that.idAlternativa) && Objects.equals(idQuestao, that.idQuestao) && Objects.equals(idAvaliacao, that.idAvaliacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlternativa, idQuestao, idAvaliacao);
    }
}
