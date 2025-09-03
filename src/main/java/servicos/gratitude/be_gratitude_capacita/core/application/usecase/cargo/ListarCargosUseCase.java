package servicos.gratitude.be_gratitude_capacita.core.application.usecase.cargo;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CargoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;

import java.util.List;

public class ListarCargosUseCase {
    private final CargoGateway cargoGateway;

    public ListarCargosUseCase(CargoGateway cargoGateway) {
        this.cargoGateway = cargoGateway;
    }

    public List<Cargo> execute(){
        return cargoGateway.findAll();
    }
}
