package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.TentativaEntityCompoundKey;

import java.time.LocalDateTime;

@Entity
public class TentativaEntity {
    @EmbeddedId
    private TentativaEntityCompoundKey idTentativaComposto;

    private LocalDateTime dtTentativa;

    @ManyToOne(optional = false)
    @MapsId("idMatriculaComposto")
    private MatriculaEntity matricula;

    @ManyToOne(optional = false)
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
