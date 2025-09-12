package servicos.gratitude.be_gratitude_capacita.core.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;

public class Questao {
    private QuestaoCompoundKey idQuestaoComposto;
    private String enunciado;
    private Avaliacao avaliacao;
    private Alternativa fkAlternativaCorreta;

    public QuestaoCompoundKey getIdQuestaoComposto() {
        return idQuestaoComposto;
    }

    public void setIdQuestaoComposto(QuestaoCompoundKey idQuestaoComposto) {
        this.idQuestaoComposto = idQuestaoComposto;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Alternativa getFkAlternativaCorreta() {
        return fkAlternativaCorreta;
    }

    public void setFkAlternativaCorreta(Alternativa fkAlternativaCorreta) {
        this.fkAlternativaCorreta = fkAlternativaCorreta;
    }
}
