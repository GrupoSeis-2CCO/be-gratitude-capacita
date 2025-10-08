package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class AcessoEntity {
    private Integer idAcesso;
    private LocalDateTime dataAcesso;
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
