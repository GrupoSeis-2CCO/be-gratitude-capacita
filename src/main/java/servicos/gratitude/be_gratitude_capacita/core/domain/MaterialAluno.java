package servicos.gratitude.be_gratitude_capacita.core.domain;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;

import java.time.LocalDateTime;

public class MaterialAluno {
    private MaterialAlunoCompoundKey idMaterialAlunoComposto;
    private Boolean isFinalizado;
    private LocalDateTime ultimoAcesso;
    private Video fkVideo;
    private Apostila fkApostila;
    private Matricula matricula;

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
}
