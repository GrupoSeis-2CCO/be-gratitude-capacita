package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.QuestaoCompoundKey;

@Entity
public class QuestaoEntity {
    @EmbeddedId
    private QuestaoCompoundKey idQuestaoComposto;

    private String enunciado;
    private Integer numeroQuestao;

    @ManyToOne(optional = false)
    @MapsId("fkAvaliacao")
    private AvaliacaoEntity avaliacao;

    @OneToOne
    private AlternativaEntity fkAlternativaCorreta;

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

    public Integer getNumeroQuestao() {
        return numeroQuestao;
    }

    public void setNumeroQuestao(Integer numeroQuestao) {
        this.numeroQuestao = numeroQuestao;
    }

    public AvaliacaoEntity getAvaliacaoEntity() {
        return avaliacao;
    }

    public void setAvaliacaoEntity(AvaliacaoEntity avaliacao) {
        this.avaliacao = avaliacao;
    }

    public AlternativaEntity getFkAlternativaCorreta() {
        return fkAlternativaCorreta;
    }

    public void setFkAlternativaCorreta(AlternativaEntity fkAlternativaCorreta) {
        this.fkAlternativaCorreta = fkAlternativaCorreta;
    }
}
