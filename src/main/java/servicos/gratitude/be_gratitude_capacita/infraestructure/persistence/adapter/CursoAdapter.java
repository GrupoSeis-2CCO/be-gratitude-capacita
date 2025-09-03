package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CursoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CursoAdapter implements CursoGateway {

    private final CursoRepository cursoRepository;

    public CursoAdapter(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public Curso save(Curso curso) {
        CursoEntity cursoEntity = CursoMapper.toEntity(curso);
        cursoRepository.save(cursoEntity);

        return CursoMapper.toDomain(cursoEntity);
    }

    @Override
    public List<Curso> findAll() {
        List<CursoEntity> entities = cursoRepository.findAll();

        return CursoMapper.toDomains(entities);
    }

    @Override
    public Curso findByTitulo(String titulo) {
        Optional<CursoEntity> entity = cursoRepository.findByTituloCurso(titulo);

        return entity.map(CursoMapper::toDomain).orElse(null);
    }

    @Override
    public Curso findById(Integer id) {
        Optional<CursoEntity> entity = cursoRepository.findById(id);

        return entity.map(CursoMapper::toDomain).orElse(null);

    }

    @Override
    public Boolean existsByTitulo(String titulo) {
        return cursoRepository.existsByTituloCurso(titulo);
    }

    @Override
    public Boolean existsById(Integer id) {
        return cursoRepository.existsById(id);
    }

    @Override
    public void deleteById(Integer id) {
        cursoRepository.deleteById(id);
    }
}
