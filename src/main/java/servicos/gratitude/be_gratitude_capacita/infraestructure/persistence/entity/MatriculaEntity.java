package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey;

import java.time.LocalDateTime;

@Entity
public class MatriculaEntity {
    @EmbeddedId
    private MatriculaEntityCompoundKey idMatriculaComposto;

    private LocalDateTime dtInscricao;
    private LocalDateTime ultimoAcesso;
    private Boolean isCompleto;
    private LocalDateTime dataFinalizacao;

    @ManyToOne(optional = false)
    @MapsId("fkUsuario")
    private UsuarioEntity usuario;

    @ManyToOne
    @MapsId("fkCurso")
    private CursoEntity curso;

    public MatriculaEntityCompoundKey getIdMatriculaComposto() {
        return idMatriculaComposto;
    }

    public void setIdMatriculaComposto(MatriculaEntityCompoundKey idMatriculaComposto) {
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

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public CursoEntity getCurso() {
        return curso;
    }

    public void setCurso(CursoEntity curso) {
        this.curso = curso;
    }
}
