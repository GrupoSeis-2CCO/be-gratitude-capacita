package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.TentativaCompoundKey;

import java.time.LocalDateTime;

@Entity
public class TentativaEntity {
    @EmbeddedId
    private TentativaCompoundKey idTentativaComposto;

    private LocalDateTime dtTentativa;

    @ManyToOne(optional = false)
    @MapsId("idMatriculaComposto")
    private MatriculaEntity matricula;

    @ManyToOne(optional = false)
    private AvaliacaoEntity avaliacao;

    public TentativaCompoundKey getIdTentativaComposto() {
        return idTentativaComposto;
    }

    public void setIdTentativaComposto(TentativaCompoundKey idTentativaComposto) {
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
