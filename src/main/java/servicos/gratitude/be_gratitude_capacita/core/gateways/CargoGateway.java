package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;

import java.util.List;

public interface CargoGateway {
    List<Cargo> findAll();
}
