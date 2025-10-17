package servicos.gratitude.be_gratitude_capacita.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;

import java.time.LocalDateTime;

public class MaterialAluno {
    private MaterialAlunoCompoundKey idMaterialAlunoComposto;
    private Boolean isFinalizado;
    private LocalDateTime ultimoAcesso;
    private Video fkVideo;
    private Apostila fkApostila;
    private Matricula matricula;
    // Lightweight IDs exposed in JSON to help clients correlate records without needing nested objects
    private Integer idVideo;      // mirrors fkVideo.idVideo when present
    private Integer idApostila;   // mirrors fkApostila.idApostila when present

    public MaterialAlunoCompoundKey getIdMaterialAlunoComposto() {
        return idMaterialAlunoComposto;
    }

    public void setIdMaterialAlunoComposto(MaterialAlunoCompoundKey idMaterialAlunoComposto) {
        this.idMaterialAlunoComposto = idMaterialAlunoComposto;
    }

    // allow both "finalizado" and legacy "finalizada" in JSON
    @JsonProperty("finalizado")
    public Boolean getFinalizado() {
        return isFinalizado;
    }

    @JsonProperty("finalizado")
    public void setFinalizado(Boolean finalizado) {
        isFinalizado = finalizado;
    }

    // Backwards-compatibility for clients reading "finalizada"
    @JsonProperty("finalizada")
    public Boolean getFinalizadaAlias() { return isFinalizado; }
    @JsonProperty("finalizada")
    public void setFinalizadaAlias(Boolean finalizada) { this.isFinalizado = finalizada; }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Video getFkVideo() {
        return fkVideo;
    }

    public void setFkVideo(Video fkVideo) {
        this.fkVideo = fkVideo;
    }

    public Apostila getFkApostila() {
        return fkApostila;
    }

    public void setFkApostila(Apostila fkApostila) {
        this.fkApostila = fkApostila;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    // Flatten convenience: expose the generated idMaterialAluno at top-level for clients
    @JsonProperty("idMaterialAluno")
    public Integer getIdMaterialAluno() {
        return idMaterialAlunoComposto != null ? idMaterialAlunoComposto.getIdMaterialAluno() : null;
    }

    @JsonProperty("idMaterialAluno")
    public void setIdMaterialAluno(Integer id) {
        if (id == null) return;
        if (this.idMaterialAlunoComposto == null) this.idMaterialAlunoComposto = new MaterialAlunoCompoundKey();
        this.idMaterialAlunoComposto.setIdMaterialAluno(id);
    }

    // Expose explicit association IDs in JSON (backend is the source of truth for conclusions)
    @JsonProperty("idVideo")
    public Integer getIdVideo() { return idVideo; }
    @JsonProperty("idVideo")
    public void setIdVideo(Integer idVideo) { this.idVideo = idVideo; }

    @JsonProperty("idApostila")
    public Integer getIdApostila() { return idApostila; }
    @JsonProperty("idApostila")
    public void setIdApostila(Integer idApostila) { this.idApostila = idApostila; }
}
