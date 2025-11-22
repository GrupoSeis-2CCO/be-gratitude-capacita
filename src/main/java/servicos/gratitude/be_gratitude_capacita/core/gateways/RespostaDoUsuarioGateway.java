package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.RespostaDoUsuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.RespostaDoUsuarioCompoundKey;

import java.util.List;

public interface RespostaDoUsuarioGateway {
    RespostaDoUsuario save(RespostaDoUsuario respostaDoUsuario);
    List<RespostaDoUsuario> findAll();
    List<RespostaDoUsuario> findAllByTentativa(Tentativa tentativa);
    Boolean existsById(RespostaDoUsuarioCompoundKey idComposto);
    Boolean existsByAlternativa(servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa alternativa);
    long countByExamId(Integer examId);
    void deleteByExamId(Integer examId);
}
