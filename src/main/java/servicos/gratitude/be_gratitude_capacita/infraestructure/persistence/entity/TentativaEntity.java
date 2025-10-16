package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.TentativaEntityCompoundKey;

import java.time.LocalDateTime;

@Entity
@Table(name = "tentativa")

public class TentativaEntity {
    @EmbeddedId
    private TentativaEntityCompoundKey idTentativaComposto;


    private LocalDateTime dtTentativa;

    @ManyToOne(optional = false, fetch = jakarta.persistence.FetchType.LAZY)
    @MapsId("idMatriculaComposto")
    @JoinColumns({
        @JoinColumn(name = "fk_usuario", referencedColumnName = "fk_usuario"),
        @JoinColumn(name = "fk_curso", referencedColumnName = "fk_curso")
    })
    private MatriculaEntity matricula;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_avaliacao")
    private AvaliacaoEntity avaliacao;


    public TentativaEntityCompoundKey getIdTentativaComposto() {
        return idTentativaComposto;
    }

    public void setIdTentativaComposto(TentativaEntityCompoundKey idTentativaComposto) {
        this.idTentativaComposto = idTentativaComposto;
    }

    public LocalDateTime getDtTentativa() {
        return dtTentativa;
    }

    public void setDtTentativa(LocalDateTime dtTentativa) {
        this.dtTentativa = dtTentativa;
    }

    public MatriculaEntity getMatricula() {
        return matricula;
    }

    public void setMatricula(MatriculaEntity matricula) {
        this.matricula = matricula;
    }

    public AvaliacaoEntity getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(AvaliacaoEntity avaliacao) {
        this.avaliacao = avaliacao;
    }
}
