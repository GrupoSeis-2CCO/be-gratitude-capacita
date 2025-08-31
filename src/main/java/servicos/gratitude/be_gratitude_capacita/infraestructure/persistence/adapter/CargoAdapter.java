package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.adapters.CargoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CargoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CargoRepository;

import java.util.List;

@Service
public class CargoAdapter implements CargoGateway {

    private final CargoRepository repository;

    public CargoAdapter(CargoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Cargo> findAll() {
        return CargoMapper.toDomain(repository.findAll());
    }
}
