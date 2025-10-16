package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

public interface AvaliacaoGateway {
    Avaliacao save(Avaliacao avaliacao);
    Boolean existsById(Integer id);
    Avaliacao findById(Integer id);
    Boolean existsByCurso(Curso curso);
    java.util.List<Avaliacao> findAllByCurso(Curso curso);
    java.util.List<Avaliacao> findAll();
}
