package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;

import java.util.List;

public interface TentativaGateway {
    Tentativa save(Tentativa tentativa);
    List<Tentativa> findAllByMatricula(Matricula matricula);
    List<Tentativa> findAllByUsuario(Integer fkUsuario);
    Tentativa findById(TentativaCompoundKey idComposto);
    Boolean existsById(TentativaCompoundKey idComposto);
}
