package servicos.gratitude.be_gratitude_capacita.core.gateways;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RelatorioGateway {
    /**
     * Retorna um mapa (lista de pares) de datas para contagem de eventos no curso entre as datas informadas.
     * A chave é a data (LocalDate) e o valor é a contagem (Integer).
     */
    List<Map<String, Object>> obterEngajamentoDiarioPorCurso(Integer fkCurso, LocalDate from, LocalDate to);
}
