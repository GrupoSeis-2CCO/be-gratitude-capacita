package servicos.gratitude.be_gratitude_capacita.core.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

import java.time.LocalDateTime;

public class Matricula {
    private MatriculaCompoundKey idMatriculaComposto;
    private LocalDateTime dtInscricao;
    private LocalDateTime ultimoAcesso;
    private Boolean isCompleto;
    private LocalDateTime dataFinalizacao;
    private Usuario usuario;
    private Curso curso;

    public MatriculaCompoundKey getIdMatriculaComposto() {
        return idMatriculaComposto;
    }

    public void setIdMatriculaComposto(MatriculaCompoundKey idMatriculaComposto) {
        this.idMatriculaComposto = idMatriculaComposto;
    }

    public LocalDateTime getDtInscricao() {
        return dtInscricao;
    }

    public void setDtInscricao(LocalDateTime dtInscricao) {
        this.dtInscricao = dtInscricao;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Boolean getCompleto() {
        return isCompleto;
    }

    public void setCompleto(Boolean completo) {
        isCompleto = completo;
    }

    public LocalDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
