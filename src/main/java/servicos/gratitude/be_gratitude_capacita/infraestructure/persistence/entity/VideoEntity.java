package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVideo;

    private String nomeVideo;
    private String descricaoVideo;
    private String urlVideo;
    private LocalDateTime dataPostadoVideo;
    private LocalDateTime dataAtualizacaoVideo;
    private Integer ordemVideo;
    private Boolean isVideoOculto;

    @ManyToOne(optional = false)
    private CursoEntity fkCurso;

    public Integer getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Integer idVideo) {
        this.idVideo = idVideo;
    }

    public String getNomeVideo() {
        return nomeVideo;
    }

    public void setNomeVideo(String nomeVideo) {
        this.nomeVideo = nomeVideo;
    }

    public String getDescricaoVideo() {
        return descricaoVideo;
    }

    public void setDescricaoVideo(String descricaoVideo) {
        this.descricaoVideo = descricaoVideo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public LocalDateTime getDataPostadoVideo() {
        return dataPostadoVideo;
    }

    public void setDataPostadoVideo(LocalDateTime dataPostadoVideo) {
        this.dataPostadoVideo = dataPostadoVideo;
    }

    public LocalDateTime getDataAtualizacaoVideo() {
        return dataAtualizacaoVideo;
    }

    public void setDataAtualizacaoVideo(LocalDateTime dataAtualizacaoVideo) {
        this.dataAtualizacaoVideo = dataAtualizacaoVideo;
    }

    public Integer getOrdemVideo() {
        return ordemVideo;
    }

    public void setOrdemVideo(Integer ordemVideo) {
        this.ordemVideo = ordemVideo;
    }

    public Boolean getVideoOculto() {
        return isVideoOculto;
    }

    public void setVideoOculto(Boolean videoOculto) {
        isVideoOculto = videoOculto;
    }

    public CursoEntity getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(CursoEntity fkCurso) {
        this.fkCurso = fkCurso;
    }
}
