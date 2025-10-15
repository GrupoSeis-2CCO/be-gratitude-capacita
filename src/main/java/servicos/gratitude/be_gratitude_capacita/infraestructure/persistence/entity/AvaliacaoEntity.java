package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "avaliacao")
public class AvaliacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Integer idAvaliacao;

    @Column(name = "nota_minima")
    private Integer acertosMinimos;

    @OneToOne(optional = false)
    @JoinColumn(name = "FK_curso")
    private CursoEntity fkCurso;

    public Integer getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(Integer idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public Integer getAcertosMinimos() {
        return acertosMinimos;
    }

    public void setAcertosMinimos(Integer acertosMinimos) {
        this.acertosMinimos = acertosMinimos;
    }

    public CursoEntity getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(CursoEntity fkCurso) {
        this.fkCurso = fkCurso;
    }
}
