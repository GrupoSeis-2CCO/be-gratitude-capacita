package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;

import java.util.List;

public interface AlternativaGateway {
    Alternativa save(Alternativa alternativa);
    List<Alternativa> findAllByQuestao(Questao questao);
    Alternativa findById(AlternativaCompoundKey idComposto);
    Boolean existsById(AlternativaCompoundKey idComposto);
    void deleteById(AlternativaCompoundKey idComposto);
}
