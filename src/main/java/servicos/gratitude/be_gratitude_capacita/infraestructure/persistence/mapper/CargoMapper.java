package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CargoEntity;

import java.util.ArrayList;
import java.util.List;

public class CargoMapper {

    public static Cargo toDomain(CargoEntity entity){
        Cargo cargo = new Cargo();

        cargo.setIdCargo(entity.getIdCargo());
        cargo.setNomeCargo(entity.getNomeCargo());

        return cargo;
    }

    public static CargoEntity toEntity(Cargo cargo){
        CargoEntity entity = new CargoEntity();

        entity.setIdCargo(cargo.getIdCargo());
        entity.setNomeCargo(cargo.getNomeCargo());

        return entity;
    }

    public static List<Cargo> toDomains(List<CargoEntity> entities){
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
