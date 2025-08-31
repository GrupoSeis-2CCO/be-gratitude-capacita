package servicos.gratitude.be_gratitude_capacita.core.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;

public class Alternativa {
    private AlternativaCompoundKey alternativaChaveComposta;
    private String texto;
    private Integer ordem;
    private Questao questao;

    public AlternativaCompoundKey getAlternativaChaveComposta() {
        return alternativaChaveComposta;
    }

    public void setAlternativaChaveComposta(AlternativaCompoundKey alternativaChaveComposta) {
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

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }
}
