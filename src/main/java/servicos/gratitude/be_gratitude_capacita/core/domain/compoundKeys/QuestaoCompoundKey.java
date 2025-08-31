package servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys;

public class QuestaoCompoundKey {
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
}
