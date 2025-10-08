package servicos.gratitude.be_gratitude_capacita.core.domain;

import java.time.LocalDateTime;

public class Acesso {
    private Integer idAcesso;
    private LocalDateTime dataAcesso;
    private Usuario fkusuario;

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

    public Usuario getFkusuario() {
        return fkusuario;
    }

    public void setFkusuario(Usuario fkusuario) {
        this.fkusuario = fkusuario;
    }
}
