package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;

public interface AvaliacaoService {
    Avaliacao save(Avaliacao avaliacao);
    Boolean existsById(Integer id);
    Avaliacao findById(Integer id);
}
