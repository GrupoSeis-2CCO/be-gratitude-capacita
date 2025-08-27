package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.adapters.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CursoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CursoAdapter implements CursoGateway {

    private final CursoRepository repository;

    public CursoAdapter(CursoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Curso save(Curso curso) {
        CursoEntity cursoEntity = CursoMapper.toEntity(curso);
        repository.save(cursoEntity);

        return CursoMapper.toDomain(cursoEntity);
    }

    @Override
    public List<Curso> findAll() {
        List<CursoEntity> entities = repository.findAll();

        return CursoMapper.toDomains(entities);
    }

    @Override
    public Curso findByTitulo(String titulo) {
        Optional<CursoEntity> entity = repository.findByTituloCurso(titulo);

        if (entity.isEmpty()){
            throw new NaoEncontradoException("""
                Nao foi encontrado curso com o titulo %s
            """.formatted(titulo));
        }

        return CursoMapper.toDomain(entity.get());
    }

    @Override
    public Curso findById(Integer id) {
        Optional<CursoEntity> entity = repository.findById(id);

        if (entity.isEmpty()){
            throw new NaoEncontradoException("""
                Nao foi encontrado curso com o id %d
            """.formatted(id));
        }

        return CursoMapper.toDomain(entity.get());
    }

    @Override
    public Boolean existsByTitulo(String titulo) {
        return repository.existsByTitulo(titulo);
    }

    @Override
    public Boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
