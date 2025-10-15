package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RelatorioGateway;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioAdapter implements RelatorioGateway {
    private final EntityManager em;

    public RelatorioAdapter(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Map<String, Object>> obterEngajamentoDiarioPorCurso(Integer fkCurso, LocalDate from, LocalDate to) {
        // Query nativa que une material_aluno.ultimo_acesso e tentativa.dt_tentativa, faz union e agrupa por data (YYYY-MM-DD)
    // Count only finalized materials as engagement (each finalized material increments engagement by 1)
    String sql = "SELECT data, SUM(cnt) as value FROM (" +
        " SELECT DATE(ultimo_acesso) as data, COUNT(*) as cnt FROM material_aluno m " +
        " WHERE m.fk_curso = :fkCurso AND m.finalizada = 1 AND m.ultimo_acesso BETWEEN :fromTs AND :toTs GROUP BY DATE(m.ultimo_acesso) " +
        " UNION ALL " +
        " SELECT DATE(t.dt_tentativa) as data, COUNT(*) as cnt FROM tentativa t " +
        " WHERE t.fk_curso = :fkCurso AND t.dt_tentativa BETWEEN :fromTs AND :toTs GROUP BY DATE(t.dt_tentativa) " +
        ") as combined GROUP BY data ORDER BY data ASC";

        Query q = em.createNativeQuery(sql);
        // usamos start of day e end of day para garantir inclusão
        java.time.LocalDateTime fromTs = from.atStartOfDay();
        java.time.LocalDateTime toTs = to.atTime(23,59,59);

        q.setParameter("fkCurso", fkCurso);
        q.setParameter("fromTs", fromTs);
        q.setParameter("toTs", toTs);

        @SuppressWarnings("unchecked")
        List<Object[]> results = q.getResultList();

        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : results) {
            Object dataObj = row[0];
            Object valueObj = row[1];
            String dateStr;
            if (dataObj instanceof java.sql.Date) {
                dateStr = ((java.sql.Date) dataObj).toLocalDate().format(fmt);
            } else if (dataObj instanceof java.sql.Timestamp) {
                dateStr = ((java.sql.Timestamp) dataObj).toLocalDateTime().toLocalDate().format(fmt);
            } else {
                dateStr = String.valueOf(dataObj);
            }

            Integer cnt = valueObj == null ? 0 : ((Number) valueObj).intValue();

            Map<String, Object> map = new HashMap<>();
            map.put("date", dateStr);
            map.put("value", cnt);
            out.add(map);
        }

        return out;
    }
}
