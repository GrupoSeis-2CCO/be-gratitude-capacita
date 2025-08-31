package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;

import java.util.List;

public interface AlternativaGateway {
    Alternativa save(Alternativa alternativa);
    List<Alternativa> findAll();
    Alternativa findById();
    Boolean existsById();
    void deleteById(AlternativaCompoundKey idComposto);
}
