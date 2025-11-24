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
    /**
     * Retorna métricas agregadas de progresso do curso.
     * Chaves esperadas: cursoId, materiaisTotal, alunosMatriculados, alunosCompletaramTudo, percentualAlunosCompleto,
     * mediaPercentualProgresso, materiaisVideos, materiaisApostilas, generatedAt.
     */
    Map<String, Object> obterProgressoCurso(Integer fkCurso);
    /**
     * Retorna métricas agregadas de desempenho de avaliações do curso.
     */
    Map<String, Object> obterDesempenhoCurso(Integer fkCurso);
    /**
     * Retorna métricas agregadas de feedback do curso.
     */
    Map<String, Object> obterFeedbackCurso(Integer fkCurso);
    /**
     * Ranking de colaboradores/alunos do curso.
     * Lista de mapas com chaves: usuarioId, nome (se disponível), materiaisFinalizados, percentualProgresso,
     * tentativas, acuraciaMedia (pode ser null), ultimaAtividade.
     */
    List<Map<String,Object>> obterColaboradoresCurso(Integer fkCurso, Integer limit, String order);
    /**
     * Retorna KPIs de engajamento e lista de inativos.
     * Chaves: ativosSemanaPct, ativos3xSemanaPct, concluindoMais1CursoPct, inativosCount, inativosLista (List<Map<nome,email>>)
     */
    Map<String, Object> obterKpisEngajamento(Integer fkCurso, LocalDate from, LocalDate to);
}
