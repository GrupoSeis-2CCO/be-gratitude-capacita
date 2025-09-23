package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;

import java.util.List;

public interface ApostilaGateway {
    Apostila save(Apostila apostila);

    List<Apostila> findAllByCurso(Curso curso);

    Page<Apostila> findAllByCurso(Curso curso, Pageable pageable);

    Apostila findById(Integer id);

    Boolean existsById(Integer id);

    Boolean existsByNome(String nome);

    Apostila findByNome(String nome);

    void deleteById(Integer id);
}
