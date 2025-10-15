package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey;

@Entity
@Table(name = "alternativa")
public class AlternativaEntity {
    @Id
    @Column(name = "id_alternativa")
    private Integer idAlternativa;

    @Column(name = "FK_questao")
    private Integer fkQuestao;

    @Column(name = "FK_avaliacao")
    private Integer fkAvaliacao;

    @Column(name = "texto")
    private String texto;

    @Column(name = "ordem_alternativa")
    private Integer ordem;

    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "FK_questao", referencedColumnName = "id_questao", insertable = false, updatable = false),
        @JoinColumn(name = "FK_avaliacao", referencedColumnName = "fk_avaliacao", insertable = false, updatable = false)
    })
    private QuestaoEntity questao;

    public Integer getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(Integer idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    public Integer getFkQuestao() {
        return fkQuestao;
    }

    public void setFkQuestao(Integer fkQuestao) {
        this.fkQuestao = fkQuestao;
    }

    public Integer getFkAvaliacao() {
        return fkAvaliacao;
    }

    public void setFkAvaliacao(Integer fkAvaliacao) {
        this.fkAvaliacao = fkAvaliacao;
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

    // compatibility methods for legacy mapper that expect a compound-key object
    public servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey getAlternativaChaveComposta(){
        servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey k = new servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey();
        k.setIdAlternativa(this.idAlternativa);
        k.setIdQuestao(this.fkQuestao);
        k.setIdAvaliacao(this.fkAvaliacao);
        return k;
    }

    public void setAlternativaChaveComposta(servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey k){
        if (k == null) return;
        this.idAlternativa = k.getIdAlternativa();
        this.fkQuestao = k.getIdQuestao();
        this.fkAvaliacao = k.getIdAvaliacao();
    }
}
