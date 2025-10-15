package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MaterialAlunoEntityCompoundKey;

import java.time.LocalDateTime;

@Entity
@Table(name = "material_aluno")
public class MaterialAlunoEntity {
    @EmbeddedId
    private MaterialAlunoEntityCompoundKey idMaterialAlunoComposto;

    @Column(name = "finalizada")
    private Boolean isFinalizado;

    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    @ManyToOne
    @JoinColumn(name = "FK_video")
    private VideoEntity fkVideo;

    @ManyToOne
    @JoinColumn(name = "FK_apostila")
    private ApostilaEntity fkApostila;

    @ManyToOne(optional = false)
    @MapsId("idMatriculaComposto")
    @JoinColumns({
        @JoinColumn(name = "FK_usuario", referencedColumnName = "FK_usuario"),
        @JoinColumn(name = "FK_curso", referencedColumnName = "fk_curso")
    })
    private MatriculaEntity matricula;

    public MaterialAlunoEntityCompoundKey getIdMaterialAlunoComposto() {
        return idMaterialAlunoComposto;
    }

    public void setIdMaterialAlunoComposto(MaterialAlunoEntityCompoundKey idMaterialAlunoComposto) {
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
