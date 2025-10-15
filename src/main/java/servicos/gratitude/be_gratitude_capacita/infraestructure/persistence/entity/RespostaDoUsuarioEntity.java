package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;

@Entity
@Table(name = "resposta_do_usuario")
public class RespostaDoUsuarioEntity {
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idTentativaComposto.idTentativa", column = @Column(name = "FK_tentativa")),
        @AttributeOverride(name = "idTentativaComposto.idMatriculaComposto.fkUsuario", column = @Column(name = "FK_usuario")),
        @AttributeOverride(name = "idTentativaComposto.idMatriculaComposto.fkCurso", column = @Column(name = "FK_curso")),
        @AttributeOverride(name = "idAlternativa", column = @Column(name = "FK_alternativa")),
        @AttributeOverride(name = "idQuestao", column = @Column(name = "FK_questao")),
        @AttributeOverride(name = "idAvaliacao", column = @Column(name = "FK_avaliacao"))
    })
    private RespostaDoUsuarioEntityCompoundKey respostaDoUsuarioEntityCompoundKey;

    @ManyToOne(optional = false)
    @MapsId("idTentativaComposto")
    @JoinColumns({
        @JoinColumn(name = "FK_tentativa", referencedColumnName = "id_tentativa"),
        @JoinColumn(name = "FK_usuario", referencedColumnName = "fk_usuario"),
        @JoinColumn(name = "FK_curso", referencedColumnName = "fk_curso")
    })
    private TentativaEntity tentativa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "FK_alternativa", referencedColumnName = "id_alternativa", insertable = false, updatable = false)
    private AlternativaEntity alternativa;

    public RespostaDoUsuarioEntityCompoundKey getRespostaDoUsuarioCompoundKey() {
        return respostaDoUsuarioEntityCompoundKey;
    }

    public void setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioEntityCompoundKey respostaDoUsuarioEntityCompoundKey) {
        this.respostaDoUsuarioEntityCompoundKey = respostaDoUsuarioEntityCompoundKey;
    }

    public TentativaEntity getTentativa() {
        return tentativa;
    }

    public void setTentativa(TentativaEntity tentativa) {
        this.tentativa = tentativa;
    }

    public AlternativaEntity getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(AlternativaEntity alternativa) {
        this.alternativa = alternativa;
    }
}
