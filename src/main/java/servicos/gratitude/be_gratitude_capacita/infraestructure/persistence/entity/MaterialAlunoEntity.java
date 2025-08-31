package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.MaterialAlunoCompoundKey;

import java.time.LocalDateTime;

@Entity
public class MaterialAlunoEntity {
    @EmbeddedId
    private MaterialAlunoCompoundKey idMaterialAlunoComposto;

    private Boolean isFinalizado;
    private LocalDateTime ultimoAcesso;

    @ManyToOne
    private VideoEntity fkVideo;

    @ManyToOne
    private ApostilaEntity fkApostila;

    @ManyToOne(optional = false)
    @MapsId("idMatriculaComposto")
    private MatriculaEntity matricula;

    public MaterialAlunoCompoundKey getIdMaterialAlunoComposto() {
        return idMaterialAlunoComposto;
    }

    public void setIdMaterialAlunoComposto(MaterialAlunoCompoundKey idMaterialAlunoComposto) {
        this.idMaterialAlunoComposto = idMaterialAlunoComposto;
    }

    public Boolean getFinalizado() {
        return isFinalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        isFinalizado = finalizado;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public VideoEntity getFkVideo() {
        return fkVideo;
    }

    public void setFkVideo(VideoEntity fkVideo) {
        this.fkVideo = fkVideo;
    }

    public ApostilaEntity getFkApostila() {
        return fkApostila;
    }

    public void setFkApostila(ApostilaEntity fkApostila) {
        this.fkApostila = fkApostila;
    }

    public MatriculaEntity getMatricula() {
        return matricula;
    }

    public void setMatricula(MatriculaEntity matricula) {
        this.matricula = matricula;
    }
}
