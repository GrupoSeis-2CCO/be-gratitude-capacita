package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;

import java.util.List;

public interface QuestaoGateway {
    Questao save(Questao questao);
    List<Questao> findAllByAvaliacao(Avaliacao avaliacao);
    Questao findById(QuestaoCompoundKey idComposto);
    Boolean existsById(QuestaoCompoundKey idComposto);
    void deleteById(QuestaoCompoundKey idComposto);
}
