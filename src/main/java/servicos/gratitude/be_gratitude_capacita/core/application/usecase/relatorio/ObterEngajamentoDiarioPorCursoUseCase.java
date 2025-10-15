package servicos.gratitude.be_gratitude_capacita.core.application.usecase.relatorio;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RelatorioGateway;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObterEngajamentoDiarioPorCursoUseCase {
    private final RelatorioGateway relatorioGateway;

    public ObterEngajamentoDiarioPorCursoUseCase(RelatorioGateway relatorioGateway) {
        this.relatorioGateway = relatorioGateway;
    }

    public List<Map<String, Object>> execute(Integer fkCurso, LocalDate from, LocalDate to){
        if (Objects.isNull(fkCurso) || Objects.isNull(from) || Objects.isNull(to)){
            throw new ValorInvalidoException("Parâmetros inválidos para gerar relatório");
        }

        return relatorioGateway.obterEngajamentoDiarioPorCurso(fkCurso, from, to);
    }
}
