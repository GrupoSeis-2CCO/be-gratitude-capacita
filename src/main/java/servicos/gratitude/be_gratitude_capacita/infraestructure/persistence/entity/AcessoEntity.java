package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class AcessoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAcesso;
    private LocalDateTime dataAcesso;
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_usuario")
    private UsuarioEntity fkUsuario;

    public Integer getIdAcesso() {
        return idAcesso;
    }

    public void setIdAcesso(Integer idAcesso) {
        this.idAcesso = idAcesso;
    }

    public LocalDateTime getDataAcesso() {
        return dataAcesso;
    }

    public void setDataAcesso(LocalDateTime dataAcesso) {
        this.dataAcesso = dataAcesso;
    }

    public UsuarioEntity getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(UsuarioEntity fkUsuario) {
        this.fkUsuario = fkUsuario;
    }
}
