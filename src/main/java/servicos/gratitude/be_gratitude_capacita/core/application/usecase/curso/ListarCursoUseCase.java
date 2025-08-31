package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.List;

public class ListarCursoUseCase {

    private final CursoGateway gateway;

    public ListarCursoUseCase(CursoGateway gateway) {
        this.gateway = gateway;
    }

    public List<Curso> execute(){
        return gateway.findAll();
    }
}
