package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CargoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CargoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CargoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CargoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CargoAdapter implements CargoGateway {

    private final CargoRepository cargoRepository;

    public CargoAdapter(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @Override
    public List<Cargo> findAll() {
        return CargoMapper.toDomains(cargoRepository.findAll());
    }

    @Override
    public Boolean existsById(Integer idCargo) {
        return cargoRepository.existsById(idCargo);
    }

    @Override
    public Cargo findById(Integer idCargo) {
        Optional<CargoEntity> entity = cargoRepository.findById(idCargo);

        return entity.map(CargoMapper::toDomain).orElse(null);
    }
}
