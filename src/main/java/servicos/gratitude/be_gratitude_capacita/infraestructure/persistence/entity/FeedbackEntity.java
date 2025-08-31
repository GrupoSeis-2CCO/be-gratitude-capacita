package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FeedbackEntity {
    @Id
    private CursoEntity fkCurso;

    private Integer estrelas;
    private String motivo;

    @ManyToOne
    private UsuarioEntity fkUsuario;

    public CursoEntity getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(CursoEntity fkCurso) {
        this.fkCurso = fkCurso;
    }

    public Integer getEstrelas() {
        return estrelas;
    }

    public void setEstrelas(Integer estrelas) {
        this.estrelas = estrelas;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public UsuarioEntity getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(UsuarioEntity fkUsuario) {
        this.fkUsuario = fkUsuario;
    }
}
