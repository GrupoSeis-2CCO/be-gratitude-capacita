package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface RelatorioRepository {

    /**
     * Retorna lista de mapas com 'data' (yyyy-MM-dd) e 'value' (contagem) para o curso entre from e to.
     * Implementado manualmente em adapter usando EntityManager (não como um JpaRepository padrão).
     */
    List<Map<String, Object>> obterEngajamentoDiarioPorCurso(@Param("fkCurso") Integer fkCurso, @Param("from") LocalDate from, @Param("to") LocalDate to);
}
