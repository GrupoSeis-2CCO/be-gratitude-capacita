package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cargo")
public class CargoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCargo;

    private String nomeCargo;

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }
}
