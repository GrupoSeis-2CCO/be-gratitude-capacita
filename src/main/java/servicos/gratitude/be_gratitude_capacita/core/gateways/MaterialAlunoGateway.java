package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

import java.util.List;

public interface MaterialAlunoGateway {
    MaterialAluno save(MaterialAluno materialAluno);
    List<MaterialAluno> findAllByMatricula(Matricula matricula);
    MaterialAluno findById(MaterialAlunoCompoundKey idComposto);
    Boolean existsById(MaterialAlunoCompoundKey idComposto);
    void deleteById(MaterialAlunoCompoundKey idComposto);
}
