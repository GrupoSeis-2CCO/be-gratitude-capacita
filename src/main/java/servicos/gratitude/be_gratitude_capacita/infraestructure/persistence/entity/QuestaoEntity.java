package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.QuestaoEntityCompoundKey;

@Entity
@Table(name = "questao")
public class QuestaoEntity {
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idQuestao", column = @Column(name = "id_questao")),
        @AttributeOverride(name = "fkAvaliacao", column = @Column(name = "fk_avaliacao"))
    })
    private QuestaoEntityCompoundKey idQuestaoComposto;

    @Column(name = "enunciado")
    private String enunciado;

    @Column(name = "numero_questao")
    private Integer numeroQuestao;

    @ManyToOne(optional = false)
    @MapsId("fkAvaliacao")
    @JoinColumn(name = "fk_avaliacao", referencedColumnName = "id_avaliacao")
    private AvaliacaoEntity avaliacao;

    @ManyToOne(optional = true)
    @JoinColumn(name = "fk_alternativa_correta", referencedColumnName = "id_alternativa")
    private AlternativaEntity fkAlternativaCorreta;

    public QuestaoEntityCompoundKey getIdQuestaoComposto() {
        return idQuestaoComposto;
    }

    public void setIdQuestaoComposto(QuestaoEntityCompoundKey idQuestaoComposto) {
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
