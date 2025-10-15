package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey;

import java.time.LocalDateTime;

@Entity
@Table(name = "matricula")
public class MatriculaEntity {
    @EmbeddedId
    private MatriculaEntityCompoundKey id;

    @jakarta.persistence.Column(name = "FK_inicio")
    private LocalDateTime dtInscricao;

    @jakarta.persistence.Column(name = "ultimo_senso")
    private LocalDateTime ultimoAcesso;

    @jakarta.persistence.Column(name = "completo")
    private Boolean isCompleto;

    @jakarta.persistence.Column(name = "data_finalizado")
    private LocalDateTime dataFinalizacao;

    @ManyToOne(optional = false)
    @MapsId("fkUsuario")
    @jakarta.persistence.JoinColumn(name = "fk_usuario")
    private UsuarioEntity usuario;

    @ManyToOne(optional = false)
    @MapsId("fkCurso")
    @jakarta.persistence.JoinColumn(name = "fk_curso")
    private CursoEntity curso;

    public MatriculaEntityCompoundKey getId() {
        return id;
    }

    public void setId(MatriculaEntityCompoundKey id) {
        this.id = id;
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
