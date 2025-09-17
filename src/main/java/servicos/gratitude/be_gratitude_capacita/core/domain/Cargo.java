package servicos.gratitude.be_gratitude_capacita.core.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

public class Cargo {
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
