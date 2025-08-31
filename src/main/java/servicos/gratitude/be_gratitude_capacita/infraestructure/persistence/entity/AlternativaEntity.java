package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey;

@Entity
public class AlternativaEntity {
    @EmbeddedId
    private AlternativaEntityCompoundKey alternativaChaveComposta;

    private String texto;
    private Integer ordem;

    @ManyToOne(optional = false)
    @MapsId("idQuestaoComposto")
    private QuestaoEntity questao;

    public AlternativaEntityCompoundKey getAlternativaChaveComposta() {
        return alternativaChaveComposta;
    }

    public void setAlternativaChaveComposta(AlternativaEntityCompoundKey alternativaChaveComposta) {
        this.alternativaChaveComposta = alternativaChaveComposta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public QuestaoEntity getQuestao() {
        return questao;
    }

    public void setQuestao(QuestaoEntity questao) {
        this.questao = questao;
    }
}
