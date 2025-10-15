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
