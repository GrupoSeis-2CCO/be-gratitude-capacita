package servicos.gratitude.be_gratitude_capacita.core.application.usecase.cargo;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CargoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;

import java.util.List;

public class ListarCargosUseCase {
    private final CargoGateway gateway;

    public ListarCargosUseCase(CargoGateway gateway) {
        this.gateway = gateway;
    }

    public List<Cargo> execute(){
        return gateway.findAll();
    }
}
