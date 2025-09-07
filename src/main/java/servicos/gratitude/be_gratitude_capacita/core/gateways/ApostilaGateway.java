package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.List;

public interface ApostilaGateway {
    Apostila save(Apostila apostila);
    List<Apostila> findAllByCurso(Curso curso);
    Apostila findById(Integer id);
    Boolean existsById(Integer id);
    Boolean existsByNome(String nome);
    Apostila findByNome(String nome);
    void deleteById(Integer id);
}
