package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RespostaDoUsuarioEntityCompoundKey implements Serializable {
    private TentativaEntityCompoundKey idTentativaComposto;
    private Integer idAlternativa;
    private Integer idQuestao;
    private Integer idAvaliacao;

    public TentativaEntityCompoundKey getIdTentativaComposto() {
        return idTentativaComposto;
    }

    public void setIdTentativaComposto(TentativaEntityCompoundKey idTentativaComposto) {
        this.idTentativaComposto = idTentativaComposto;
    }

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
        RespostaDoUsuarioEntityCompoundKey that = (RespostaDoUsuarioEntityCompoundKey) o;
        return Objects.equals(idTentativaComposto, that.idTentativaComposto) && Objects.equals(idAlternativa, that.idAlternativa) && Objects.equals(idQuestao, that.idQuestao) && Objects.equals(idAvaliacao, that.idAvaliacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTentativaComposto, idAlternativa, idQuestao, idAvaliacao);
    }
}
