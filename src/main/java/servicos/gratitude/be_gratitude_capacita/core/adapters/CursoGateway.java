package servicos.gratitude.be_gratitude_capacita.core.adapters;

import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.List;

public interface CursoGateway {
    Curso save(Curso curso);
    List<Curso> findAll();
    Curso findByTitulo(String titulo);
    Curso findById(Integer id);
    Boolean existsByTitulo(String titulo);
    Boolean existsById(Integer id);
    void deleteById(Integer id);
}
