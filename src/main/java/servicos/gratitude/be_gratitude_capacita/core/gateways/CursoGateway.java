package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;

import java.util.List;

public interface CursoGateway {
    Curso save(Curso curso);

    List<Curso> findAll();

    Page<Curso> findAll(Pageable pageable);

    Curso findByTitulo(String titulo);

    Curso findById(Integer id);

    Boolean existsByTitulo(String titulo);

    Boolean existsById(Integer id);

    void deleteById(Integer id);
}
