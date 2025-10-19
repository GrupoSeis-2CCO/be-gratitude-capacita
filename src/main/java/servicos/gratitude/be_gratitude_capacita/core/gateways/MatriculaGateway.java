package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

import java.util.List;

public interface MatriculaGateway {
    Matricula save(Matricula matricula);
    List<Matricula> findAllByUsuario(Usuario usuario);
    List<Matricula> findAllByCurso(Curso curso);
    
    // Alias para findAllByCurso (compatibilidade com novo UseCase)
    default List<Matricula> findByCurso(Curso curso) {
        return findAllByCurso(curso);
    }
    
    Matricula findById(MatriculaCompoundKey idComposto);
    Boolean existsById(MatriculaCompoundKey idComposto);
    void deleteById(MatriculaCompoundKey idComposto);
}
