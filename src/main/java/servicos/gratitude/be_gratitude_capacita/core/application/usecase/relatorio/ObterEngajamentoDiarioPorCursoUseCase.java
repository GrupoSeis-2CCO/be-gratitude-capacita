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

    public Map<String, Object> progresso(Integer fkCurso){
        if (Objects.isNull(fkCurso)){
            throw new ValorInvalidoException("Curso inválido para gerar progresso");
        }
        return relatorioGateway.obterProgressoCurso(fkCurso);
    }

    public Map<String, Object> desempenho(Integer fkCurso){
        if (Objects.isNull(fkCurso)){
            throw new ValorInvalidoException("Curso inválido para gerar desempenho");
        }
        return relatorioGateway.obterDesempenhoCurso(fkCurso);
    }

    public Map<String, Object> feedback(Integer fkCurso){
        if (Objects.isNull(fkCurso)){
            throw new ValorInvalidoException("Curso inválido para gerar feedback");
        }
        return relatorioGateway.obterFeedbackCurso(fkCurso);
    }

    public List<Map<String,Object>> colaboradores(Integer fkCurso, Integer limit, String order){
        if (Objects.isNull(fkCurso)){
            throw new ValorInvalidoException("Curso inválido para gerar ranking");
        }
        return relatorioGateway.obterColaboradoresCurso(fkCurso, limit, order);
    }

    public Map<String, Object> kpisEngajamento(Integer fkCurso, LocalDate from, LocalDate to){
        if (Objects.isNull(fkCurso)){
            throw new ValorInvalidoException("Curso inválido para gerar KPIs de engajamento");
        }
        return relatorioGateway.obterKpisEngajamento(fkCurso, from, to);
    }
}
