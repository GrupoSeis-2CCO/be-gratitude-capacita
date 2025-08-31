package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CargoEntity;

import java.util.ArrayList;
import java.util.List;

public class CargoMapper {

    public static List<Cargo> toDomain (List<CargoEntity> entities){
        List<Cargo> cargos = new ArrayList<>();

        for (CargoEntity entityDaVez : entities) {
            Cargo cargo = new Cargo();

            cargo.setIdCargo(entityDaVez.getIdCargo());
            cargo.setNomeCargo(entityDaVez.getNomeCargo());

            cargos.add(cargo);
        }

        return cargos;
    }
}
