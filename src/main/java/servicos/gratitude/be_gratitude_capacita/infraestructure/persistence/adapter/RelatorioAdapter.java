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

    @Override
    public Map<String, Object> obterProgressoCurso(Integer fkCurso) {
        // Total de vídeos e apostilas
        String sqlMateriais = "SELECT " +
            " (SELECT COUNT(*) FROM video v WHERE v.fk_curso = :fkCurso) AS total_videos, " +
            " (SELECT COUNT(*) FROM apostila a WHERE a.fk_curso = :fkCurso) AS total_apostilas";
        Query qMateriais = em.createNativeQuery(sqlMateriais);
        qMateriais.setParameter("fkCurso", fkCurso);
        Object[] rowMateriais = (Object[]) qMateriais.getSingleResult();
        int totalVideos = ((Number) rowMateriais[0]).intValue();
        int totalApostilas = ((Number) rowMateriais[1]).intValue();
        int materiaisTotal = totalVideos + totalApostilas;

        // Alunos matriculados
        String sqlMatriculas = "SELECT COUNT(*) FROM matricula m WHERE m.fk_curso = :fkCurso";
        Query qMatriculas = em.createNativeQuery(sqlMatriculas);
        qMatriculas.setParameter("fkCurso", fkCurso);
        int alunosMatriculados = ((Number) qMatriculas.getSingleResult()).intValue();

        // Percentual de progresso médio por aluno (materiais finalizados / materiais total)
        // material_aluno tem fk_video ou fk_apostila, consideramos finalizada=1
        String sqlMediaProgresso = "SELECT IFNULL(AVG(prog.percentual),0) FROM (" +
            " SELECT m.fk_usuario, (COUNT(CASE WHEN ma.finalizada = 1 THEN 1 END) / :matTotal) * 100 AS percentual" +
            " FROM matricula m LEFT JOIN material_aluno ma ON ma.fk_usuario = m.fk_usuario AND ma.fk_curso = m.fk_curso" +
            " WHERE m.fk_curso = :fkCurso GROUP BY m.fk_usuario" +
            ") prog";
        Query qMedia = em.createNativeQuery(sqlMediaProgresso);
        qMedia.setParameter("fkCurso", fkCurso);
        qMedia.setParameter("matTotal", materiaisTotal == 0 ? 1 : materiaisTotal); // evitar divisão por zero
        double mediaPercentualProgresso = ((Number) qMedia.getSingleResult()).doubleValue();

        // Alunos que completaram tudo (matricula.completo = 1 ou progresso = 100%)
        String sqlCompletaram = "SELECT COUNT(*) FROM matricula m WHERE m.fk_curso = :fkCurso AND m.completo = 1";
        Query qCompletaram = em.createNativeQuery(sqlCompletaram);
        qCompletaram.setParameter("fkCurso", fkCurso);
        int alunosCompletaramTudo = ((Number) qCompletaram.getSingleResult()).intValue();

        double percentualAlunosCompleto = alunosMatriculados == 0 ? 0.0 : (alunosCompletaramTudo * 100.0 / alunosMatriculados);

        Map<String, Object> out = new HashMap<>();
        out.put("cursoId", fkCurso);
        out.put("materiaisTotal", materiaisTotal);
        out.put("materiaisVideos", totalVideos);
        out.put("materiaisApostilas", totalApostilas);
        out.put("alunosMatriculados", alunosMatriculados);
        out.put("alunosCompletaramTudo", alunosCompletaramTudo);
        out.put("percentualAlunosCompleto", percentualAlunosCompleto);
        out.put("mediaPercentualProgresso", mediaPercentualProgresso);
        out.put("generatedAt", java.time.OffsetDateTime.now().toString());
        return out;
    }

    @Override
    public Map<String, Object> obterDesempenhoCurso(Integer fkCurso) {
        // Avaliacoes total
        String sqlAvals = "SELECT COUNT(*) FROM avaliacao a WHERE a.fk_curso = :fkCurso";
        Query qAvals = em.createNativeQuery(sqlAvals).setParameter("fkCurso", fkCurso);
        int avaliacoesTotal = ((Number) qAvals.getSingleResult()).intValue();

        // Tentativas total
        String sqlTentativas = "SELECT COUNT(*) FROM tentativa t WHERE t.fk_curso = :fkCurso";
        Query qTent = em.createNativeQuery(sqlTentativas).setParameter("fkCurso", fkCurso);
        int tentativasTotal = ((Number) qTent.getSingleResult()).intValue();

        // Média tentativas por aluno
        String sqlMediaTent = "SELECT IFNULL(AVG(sub.cnt),0) FROM (SELECT fk_usuario, COUNT(*) cnt FROM tentativa WHERE fk_curso = :fkCurso GROUP BY fk_usuario) sub";
        Query qMediaTent = em.createNativeQuery(sqlMediaTent).setParameter("fkCurso", fkCurso);
        double mediaTentativasPorAluno = ((Number) qMediaTent.getSingleResult()).doubleValue();

        // Última tentativa
        String sqlUltima = "SELECT MAX(dt_tentativa) FROM tentativa WHERE fk_curso = :fkCurso";
        Query qUltima = em.createNativeQuery(sqlUltima).setParameter("fkCurso", fkCurso);
        Object ultimaObj = qUltima.getSingleResult();
        String ultimaTentativaEm = ultimaObj == null ? null : String.valueOf(ultimaObj);

        // Acurácia média e distribuição: requer saber qual alternativa é correta (não há campo explícito). Placeholder.
        double mediaPercentualAcerto = 0.0; // TODO: calcular quando houver flag de alternativa correta.
        Map<String,Integer> distribuicao = new HashMap<>();

        Map<String,Object> out = new HashMap<>();
        out.put("cursoId", fkCurso);
        out.put("avaliacoesTotal", avaliacoesTotal);
        out.put("tentativasTotal", tentativasTotal);
        out.put("mediaTentativasPorAluno", mediaTentativasPorAluno);
        out.put("mediaPercentualAcerto", mediaPercentualAcerto);
        out.put("distribuicaoPercentualFaixas", distribuicao);
        out.put("ultimaTentativaEm", ultimaTentativaEm);
        out.put("generatedAt", java.time.OffsetDateTime.now().toString());
        return out;
    }

    @Override
    public Map<String, Object> obterFeedbackCurso(Integer fkCurso) {
        String sqlTotal = "SELECT COUNT(*) FROM feedback f WHERE f.fk_curso = :fkCurso";
        int totalFeedbacks = ((Number) em.createNativeQuery(sqlTotal).setParameter("fkCurso", fkCurso).getSingleResult()).intValue();

        String sqlMedia = "SELECT IFNULL(AVG(estrelas),0) FROM feedback WHERE fk_curso = :fkCurso";
        double mediaEstrelas = ((Number) em.createNativeQuery(sqlMedia).setParameter("fkCurso", fkCurso).getSingleResult()).doubleValue();

        String sqlDist = "SELECT estrelas, COUNT(*) FROM feedback WHERE fk_curso = :fkCurso GROUP BY estrelas";
        @SuppressWarnings("unchecked")
        List<Object[]> distRows = em.createNativeQuery(sqlDist).setParameter("fkCurso", fkCurso).getResultList();
        Map<String,Integer> distribuicao = new HashMap<>();
        for (Object[] r : distRows) {
            distribuicao.put(String.valueOf(r[0]), ((Number) r[1]).intValue());
        }

        String sqlUltimos = "SELECT fk_usuario, estrelas, motivo FROM feedback WHERE fk_curso = :fkCurso ORDER BY fk_usuario DESC LIMIT 5"; // Sem timestamp, ordena por usuario
        @SuppressWarnings("unchecked")
        List<Object[]> ultRows = em.createNativeQuery(sqlUltimos).setParameter("fkCurso", fkCurso).getResultList();
        List<Map<String,Object>> ultimos = new ArrayList<>();
        for (Object[] r : ultRows) {
            Map<String,Object> map = new HashMap<>();
            map.put("usuarioId", ((Number) r[0]).intValue());
            map.put("estrelas", ((Number) r[1]).intValue());
            map.put("motivo", r[2]);
            ultimos.add(map);
        }

        Map<String,Object> out = new HashMap<>();
        out.put("cursoId", fkCurso);
        out.put("totalFeedbacks", totalFeedbacks);
        out.put("mediaEstrelas", mediaEstrelas);
        out.put("distribuicaoEstrelas", distribuicao);
        out.put("ultimosFeedbacks", ultimos);
        out.put("generatedAt", java.time.OffsetDateTime.now().toString());
        return out;
    }

    @Override
    public List<Map<String, Object>> obterColaboradoresCurso(Integer fkCurso, Integer limit, String order) {
        if (limit == null) limit = 20;

        // Total materiais para cálculo progresso
        String sqlMatTot = "SELECT (SELECT COUNT(*) FROM video v WHERE v.fk_curso = :fkCurso) + (SELECT COUNT(*) FROM apostila a WHERE a.fk_curso = :fkCurso)";
        int materiaisTotal = ((Number) em.createNativeQuery(sqlMatTot).setParameter("fkCurso", fkCurso).getSingleResult()).intValue();
        if (materiaisTotal == 0) materiaisTotal = 1; // evitar divisão por zero

        // Consulta principal: para cada usuário matriculado no curso, traz nome, melhor porcentagem de acerto entre as tentativas (best_percent), número de tentativas e materiais finalizados
        String sql =
            "SELECT u.id_usuario AS usuarioId, u.nome AS nome, " +
            " COALESCE((SELECT MAX(at.percent_correct) FROM (" +
            "   SELECT t2.id_tentativa, t2.fk_usuario, (SUM(CASE WHEN a.id_alternativa = q.fk_alternativa_correta THEN 1 ELSE 0 END) * 100.0) / COUNT(*) AS percent_correct " +
            "   FROM tentativa t2 " +
            "   JOIN resposta_do_usuario ru ON ru.fk_tentativa = t2.id_tentativa " +
            "   JOIN alternativa a ON a.id_alternativa = ru.fk_alternativa " +
            "   JOIN questao q ON q.id_questao = a.fk_questao " +
            "   WHERE t2.fk_usuario = u.id_usuario AND t2.fk_curso = :fkCurso " +
            "   GROUP BY t2.id_tentativa" +
            " ) at), 0) AS best_percent, " +
            " (SELECT COUNT(*) FROM tentativa t3 WHERE t3.fk_usuario = u.id_usuario AND t3.fk_curso = :fkCurso) AS attempts, " +
            " (SELECT COUNT(*) FROM material_aluno ma WHERE ma.fk_usuario = u.id_usuario AND ma.fk_curso = :fkCurso AND ma.finalizada = 1) AS materiais_finalizados " +
            " FROM matricula m " +
            " JOIN usuario u ON u.id_usuario = m.fk_usuario " +
            " WHERE m.fk_curso = :fkCurso " +
            " GROUP BY u.id_usuario, u.nome " +
            " ORDER BY best_percent DESC, attempts ASC " +
            " LIMIT :lim";

        Query q = em.createNativeQuery(sql);
        q.setParameter("fkCurso", fkCurso);
        q.setParameter("lim", limit);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        List<Map<String,Object>> out = new ArrayList<>();
        for (Object[] r : rows) {
            int usuarioId = r[0] == null ? 0 : ((Number) r[0]).intValue();
            String nome = r[1] == null ? "" : String.valueOf(r[1]);
            double bestPercent = r[2] == null ? 0.0 : ((Number) r[2]).doubleValue();
            int attempts = r[3] == null ? 0 : ((Number) r[3]).intValue();
            int materiaisFinalizados = r[4] == null ? 0 : ((Number) r[4]).intValue();

            double percentualProgresso = (materiaisFinalizados * 100.0) / materiaisTotal;

            Map<String,Object> map = new HashMap<>();
            map.put("usuarioId", usuarioId);
            map.put("nome", nome);
            map.put("bestPercent", bestPercent);
            map.put("tentativas", attempts);
            map.put("materiaisFinalizados", materiaisFinalizados);
            map.put("percentualProgresso", percentualProgresso);
            out.add(map);
        }
        return out;
    }

    @Override
    public Map<String, Object> obterKpisEngajamento(Integer fkCurso, LocalDate from, LocalDate to) {
        // Datas para SQL
        java.time.LocalDateTime fromTs = from.atStartOfDay();
        java.time.LocalDateTime toTs = to.atTime(23,59,59);

        // 1. Total Alunos Matriculados
        String sqlTotal = "SELECT COUNT(*) FROM matricula m WHERE m.fk_curso = :fkCurso";
        int totalAlunos = ((Number) em.createNativeQuery(sqlTotal).setParameter("fkCurso", fkCurso).getSingleResult()).intValue();
        if (totalAlunos == 0) totalAlunos = 1; // Avoid division by zero

        // 2. Ativos no período (acesso material ou tentativa entre from e to)
        String sqlAtivos = "SELECT COUNT(DISTINCT m.fk_usuario) " +
            "FROM matricula m " +
            "LEFT JOIN material_aluno ma ON ma.fk_usuario = m.fk_usuario AND ma.fk_curso = m.fk_curso " +
            "LEFT JOIN tentativa t ON t.fk_usuario = m.fk_usuario AND t.fk_curso = m.fk_curso " +
            "WHERE m.fk_curso = :fkCurso " +
            "AND (ma.ultimo_acesso BETWEEN :fromTs AND :toTs " +
            "     OR t.dt_tentativa BETWEEN :fromTs AND :toTs)";
        
        Query qAtivos = em.createNativeQuery(sqlAtivos);
        qAtivos.setParameter("fkCurso", fkCurso);
        qAtivos.setParameter("fromTs", fromTs);
        qAtivos.setParameter("toTs", toTs);
        int ativosSemana = ((Number) qAtivos.getSingleResult()).intValue();

        // 3. Ativos 3x ou mais no período (dias distintos)
        String sqlAtivos3x = "SELECT COUNT(*) FROM (" +
            " SELECT u_id FROM (" +
            "   SELECT fk_usuario as u_id, DATE(ultimo_acesso) as d FROM material_aluno WHERE fk_curso = :fkCurso AND ultimo_acesso BETWEEN :fromTs AND :toTs " +
            "   UNION " +
            "   SELECT fk_usuario as u_id, DATE(dt_tentativa) as d FROM tentativa WHERE fk_curso = :fkCurso AND dt_tentativa BETWEEN :fromTs AND :toTs " +
            " ) as dates " +
            " GROUP BY u_id " +
            " HAVING COUNT(DISTINCT d) >= 3 " +
            ") as count_users";
        
        Query qAtivos3x = em.createNativeQuery(sqlAtivos3x);
        qAtivos3x.setParameter("fkCurso", fkCurso);
        qAtivos3x.setParameter("fromTs", fromTs);
        qAtivos3x.setParameter("toTs", toTs);
        int ativos3x = ((Number) qAtivos3x.getSingleResult()).intValue();

        // 4. Concluindo +1 curso no período (qualquer curso)
        String sqlConcluiuMais1 = "SELECT COUNT(DISTINCT m.fk_usuario) " +
            "FROM matricula m " +
            "JOIN matricula m_all ON m_all.fk_usuario = m.fk_usuario " +
            "WHERE m.fk_curso = :fkCurso " +
            "AND m_all.data_finalizado BETWEEN :fromTs AND :toTs";
        
        Query qConcluiu = em.createNativeQuery(sqlConcluiuMais1);
        qConcluiu.setParameter("fkCurso", fkCurso);
        qConcluiu.setParameter("fromTs", fromTs);
        qConcluiu.setParameter("toTs", toTs);
        int concluiuMais1 = ((Number) qConcluiu.getSingleResult()).intValue();

        // 5. Inativos (> 15 dias antes do fim do período)
        // Consideramos inativo quem não teve acesso nos últimos 15 dias RELATIVO AO FIM DO PERIODO (toTs)
        // Ou seja, MAX(ultimo_acesso) < (toTs - 15 days)
        String sqlInativos = "SELECT u.nome, u.email, " +
            "DATEDIFF(:toTs, GREATEST(COALESCE(ma.last_mat, '1900-01-01'), COALESCE(t.last_tent, '1900-01-01'))) " +
            "FROM matricula m " +
            "JOIN usuario u ON u.id_usuario = m.fk_usuario " +
            "LEFT JOIN (SELECT fk_usuario, MAX(ultimo_acesso) as last_mat FROM material_aluno WHERE fk_curso = :fkCurso GROUP BY fk_usuario) ma ON ma.fk_usuario = m.fk_usuario " +
            "LEFT JOIN (SELECT fk_usuario, MAX(dt_tentativa) as last_tent FROM tentativa WHERE fk_curso = :fkCurso GROUP BY fk_usuario) t ON t.fk_usuario = m.fk_usuario " +
            "WHERE m.fk_curso = :fkCurso " +
            "AND (GREATEST(COALESCE(ma.last_mat, '1900-01-01'), COALESCE(t.last_tent, '1900-01-01')) < DATE_SUB(:toTs, INTERVAL 15 DAY))";
        
        Query qInativos = em.createNativeQuery(sqlInativos);
        qInativos.setParameter("fkCurso", fkCurso);
        qInativos.setParameter("toTs", toTs);
        
        @SuppressWarnings("unchecked")
        List<Object[]> rowsInativos = qInativos.getResultList();
        List<Map<String, Object>> listaInativos = new ArrayList<>();
        for (Object[] row : rowsInativos) {
            Map<String, Object> user = new HashMap<>();
            user.put("nome", row[0] != null ? row[0].toString() : "Sem Nome");
            user.put("email", row[1] != null ? row[1].toString() : "Sem Email");
            user.put("diasInativo", row[2] != null ? ((Number) row[2]).intValue() : 0);
            listaInativos.add(user);
        }

        Map<String, Object> out = new HashMap<>();
        out.put("ativosSemanaPct", (ativosSemana * 100.0) / totalAlunos);
        out.put("ativosSemanaCount", ativosSemana);
        out.put("ativos3xSemanaPct", (ativos3x * 100.0) / totalAlunos);
        out.put("ativos3xSemanaCount", ativos3x);
        out.put("concluindoMais1CursoPct", (concluiuMais1 * 100.0) / totalAlunos);
        out.put("concluindoMais1CursoCount", concluiuMais1);
        out.put("inativosCount", listaInativos.size());
        out.put("inativosLista", listaInativos);
        
        return out;
    }

    @Override
    public List<Map<String, Object>> obterMesesDisponiveisParticipante(Integer fkUsuario, Integer fkCurso) {
        // Retorna os meses/anos distintos em que o participante finalizou materiais
        String sql = "SELECT DISTINCT YEAR(ma.ultimo_acesso) as ano, MONTH(ma.ultimo_acesso) as mes " +
            "FROM material_aluno ma " +
            "WHERE ma.fk_usuario = :fkUsuario " +
            "AND (:fkCurso IS NULL OR ma.fk_curso = :fkCurso) " +
            "AND ma.finalizada = 1 " +
            "AND ma.ultimo_acesso IS NOT NULL " +
            "ORDER BY ano DESC, mes DESC";

        Query q = em.createNativeQuery(sql);
        q.setParameter("fkUsuario", fkUsuario);
        q.setParameter("fkCurso", fkCurso);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        String[] mesesPt = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                           "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : rows) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            
            Map<String, Object> map = new HashMap<>();
            map.put("year", year);
            map.put("month", month);
            map.put("monthName", mesesPt[month - 1]);
            out.add(map);
        }

        return out;
    }
}
