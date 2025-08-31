package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ApostilaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idApostila;

    private String nomeApostilaOriginal;
    private String nomeApostilaArmazenamento;
    private String descricaoApostila;
    private Integer tamanhoBytes;
    private LocalDateTime dataPostadoApostila;
    private LocalDateTime dataAtualizacaoApostila;
    private Boolean isApostilaOculto;
    private Integer ordemApostila;

    @ManyToOne(optional = false)
    private CursoEntity fkCurso;

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

    public CursoEntity getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(CursoEntity fkCurso) {
        this.fkCurso = fkCurso;
    }
}
