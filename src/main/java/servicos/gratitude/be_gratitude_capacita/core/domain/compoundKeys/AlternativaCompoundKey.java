package servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys;

public class AlternativaCompoundKey {
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
}
