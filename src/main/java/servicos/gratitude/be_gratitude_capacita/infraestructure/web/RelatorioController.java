package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.relatorio.ObterEngajamentoDiarioPorCursoUseCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {
    private final ObterEngajamentoDiarioPorCursoUseCase obterEngajamentoUseCase;
    private static final Logger log = LoggerFactory.getLogger(RelatorioController.class);

    public RelatorioController(ObterEngajamentoDiarioPorCursoUseCase obterEngajamentoUseCase) {
        this.obterEngajamentoUseCase = obterEngajamentoUseCase;
    }

    @GetMapping("/curso/{fkCurso}/engajamento")
    public ResponseEntity<List<Map<String, Object>>> obterEngajamento(
            @PathVariable Integer fkCurso,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "days", required = false) Integer days
    ) {
        // default: últimos `days` dias (14) se from/to não informados
        if (from == null || to == null) {
            int d = (days == null || days <= 0) ? 14 : days;
            LocalDate end = LocalDate.now();
            LocalDate start = end.minusDays(d - 1);
            from = start;
            to = end;
        }

        List<Map<String, Object>> result = obterEngajamentoUseCase.execute(fkCurso, from, to);

        // Preencher zeros para datas sem eventos entre from..to
        List<Map<String, Object>> filled = fillDateRange(result, from, to);
        if (log.isDebugEnabled()) {
            log.debug("Relatorio: curso={} from={} to={} -> {} dias retornados", fkCurso, from, to, filled.size());
            if (!filled.isEmpty()) {
                // log first 5 entries
                int limit = Math.min(5, filled.size());
                for (int i = 0; i < limit; i++) {
                    Map<String, Object> row = filled.get(i);
                    log.debug("  sample[{}] = {}", i, row);
                }
            }
        }

        if (filled.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filled);
    }

    @GetMapping("/curso/{fkCurso}/progresso")
    public ResponseEntity<Map<String, Object>> obterProgresso(@PathVariable Integer fkCurso){
        Map<String, Object> dados = obterEngajamentoUseCase.progresso(fkCurso);
        if (dados == null || dados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/curso/{fkCurso}/desempenho")
    public ResponseEntity<Map<String,Object>> obterDesempenho(@PathVariable Integer fkCurso){
        Map<String,Object> dados = obterEngajamentoUseCase.desempenho(fkCurso);
        if (dados == null || dados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/curso/{fkCurso}/feedback")
    public ResponseEntity<Map<String,Object>> obterFeedback(@PathVariable Integer fkCurso){
        Map<String,Object> dados = obterEngajamentoUseCase.feedback(fkCurso);
        if (dados == null || dados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/curso/{fkCurso}/colaboradores")
    public ResponseEntity<List<Map<String,Object>>> obterColaboradores(@PathVariable Integer fkCurso,
                                                                       @RequestParam(name="limit", required=false) Integer limit,
                                                                       @RequestParam(name="order", required=false) String order){
        List<Map<String,Object>> dados = obterEngajamentoUseCase.colaboradores(fkCurso, limit, order);
        if (dados == null || dados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/curso/{fkCurso}/dashboard")
    public ResponseEntity<Map<String,Object>> obterDashboard(@PathVariable Integer fkCurso,
                                                            @RequestParam(name = "days", required = false) Integer days,
                                                            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                            @RequestParam(name = "colaboradoresLimit", required = false) Integer colaboradoresLimit) {
        // Determine date range
        LocalDate start, end;
        if (from != null && to != null) {
            start = from;
            end = to;
        } else {
            int d = (days == null || days <= 0) ? 14 : days;
            end = LocalDate.now();
            start = end.minusDays(d - 1);
        }

        Map<String,Object> out = new HashMap<>();
        try {
            // KPIs always calculated for the last 7 days relative to NOW (independent of filters)
            LocalDate kpiEnd = LocalDate.now();
            LocalDate kpiStart = kpiEnd.minusDays(7);
            Map<String,Object> kpis = obterEngajamentoUseCase.kpisEngajamento(fkCurso, kpiStart, kpiEnd);

            // Chart data uses the filtered range
            List<Map<String,Object>> engajamento = obterEngajamentoUseCase.execute(fkCurso, start, end);
            Map<String,Object> feedback = obterEngajamentoUseCase.feedback(fkCurso);

            out.put("kpis", kpis);
            out.put("engajamento", engajamento == null ? new java.util.ArrayList<>() : engajamento);
            out.put("feedback", feedback == null ? new HashMap<>() : feedback);
            out.put("generatedAt", java.time.OffsetDateTime.now().toString());
            out.put("period", Map.of("start", start.toString(), "end", end.toString()));

            return ResponseEntity.ok(out);
        } catch (Exception ex) {
            log.error("Erro ao gerar dashboard para curso {}: {}", fkCurso, ex.getMessage(), ex);
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Retorna os meses/anos em que o participante tem materiais concluídos.
     * Útil para popular filtros de data dinamicamente.
     */
    @GetMapping("/participante/{fkUsuario}/meses-disponiveis")
    public ResponseEntity<List<Map<String, Object>>> obterMesesDisponiveis(
            @PathVariable Integer fkUsuario,
            @RequestParam(name = "fkCurso", required = false) Integer fkCurso) {
        List<Map<String, Object>> meses = obterEngajamentoUseCase.mesesDisponiveisParticipante(fkUsuario, fkCurso);
        if (meses == null || meses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(meses);
    }

    private List<Map<String, Object>> fillDateRange(List<Map<String, Object>> existing, LocalDate from, LocalDate to) {
        Map<String, Integer> map = new HashMap<>();
        for (Map<String, Object> row : existing) {
            String d = (String) row.get("date");
            Integer v = row.get("value") == null ? 0 : ((Number) row.get("value")).intValue();
            map.put(d, v);
        }

        java.util.List<Map<String, Object>> out = new java.util.ArrayList<>();
        java.time.LocalDate cur = from;
        while (!cur.isAfter(to)) {
            String key = cur.toString();
            Map<String, Object> r = new HashMap<>();
            r.put("date", key);
            r.put("value", map.getOrDefault(key, 0));
            out.add(r);
            cur = cur.plusDays(1);
        }
        return out;
    }
}
