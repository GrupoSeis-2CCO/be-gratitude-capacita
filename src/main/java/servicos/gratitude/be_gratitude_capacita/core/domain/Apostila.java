package servicos.gratitude.be_gratitude_capacita.core.domain;

import java.time.LocalDateTime;

public class Apostila {
    private Integer idApostila;
    private String nomeApostilaOriginal;
    private String nomeApostilaArmazenamento;
    private String descricaoApostila;
    private Integer tamanhoBytes;
    private LocalDateTime dataPostadoApostila;
    private LocalDateTime dataAtualizacaoApostila;
    private Boolean isApostilaOculto;
    private Integer ordemApostila;
    private Curso fkCurso;

    public Integer getIdApostila() {
        return idApostila;
    }

    public void setIdApostila(Integer idApostila) {
        this.idApostila = idApostila;
    }

    public String getNomeApostilaOriginal() {
        return nomeApostilaOriginal;
    }

    public void setNomeApostilaOriginal(String nomeApostilaOriginal) {
        this.nomeApostilaOriginal = nomeApostilaOriginal;
    }

    public String getNomeApostilaArmazenamento() {
        return nomeApostilaArmazenamento;
    }

    public void setNomeApostilaArmazenamento(String nomeApostilaArmazenamento) {
        this.nomeApostilaArmazenamento = nomeApostilaArmazenamento;
    }

    public String getDescricaoApostila() {
        return descricaoApostila;
    }

    public void setDescricaoApostila(String descricaoApostila) {
        this.descricaoApostila = descricaoApostila;
    }

    public Integer getTamanhoBytes() {
        return tamanhoBytes;
    }

    public void setTamanhoBytes(Integer tamanhoBytes) {
        this.tamanhoBytes = tamanhoBytes;
    }

    public LocalDateTime getDataPostadoApostila() {
        return dataPostadoApostila;
    }

    public void setDataPostadoApostila(LocalDateTime dataPostadoApostila) {
        this.dataPostadoApostila = dataPostadoApostila;
    }

    public LocalDateTime getDataAtualizacaoApostila() {
        return dataAtualizacaoApostila;
    }

    public void setDataAtualizacaoApostila(LocalDateTime dataAtualizacaoApostila) {
        this.dataAtualizacaoApostila = dataAtualizacaoApostila;
    }

    public Boolean getApostilaOculto() {
        return isApostilaOculto;
    }

    public void setApostilaOculto(Boolean apostilaOculto) {
        isApostilaOculto = apostilaOculto;
    }

    public Integer getOrdemApostila() {
        return ordemApostila;
    }

    public void setOrdemApostila(Integer ordemApostila) {
        this.ordemApostila = ordemApostila;
    }

    public Curso getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(Curso fkCurso) {
        this.fkCurso = fkCurso;
    }
}
