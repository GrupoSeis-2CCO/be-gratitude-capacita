package servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa;

import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;

import java.util.List;

public class ListarTentativaPorUsuarioUseCase {
    private final TentativaGateway tentativaGateway;

    public ListarTentativaPorUsuarioUseCase(TentativaGateway tentativaGateway) {
        this.tentativaGateway = tentativaGateway;
    }

    public List<Tentativa> execute(Integer fkUsuario){
        return tentativaGateway.findAllByUsuario(fkUsuario);
    }
}
