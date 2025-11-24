package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;
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
        List<CursoEntity> entities = cursoRepository.findAllByOrderByOrdemCursoAsc();
        return CursoMapper.toDomains(entities);
    }

    @Override
    public Page<Curso> findAll(Pageable pageable) {
        // Converte Pageable do domínio para PageRequest do Spring
        PageRequest pageRequest = createPageRequest(pageable);

        // Executa consulta paginada
        org.springframework.data.domain.Page<CursoEntity> springPage = cursoRepository.findAll(pageRequest);

        // Converte entidades para domínio
        List<Curso> cursos = CursoMapper.toDomains(springPage.getContent());

        // Retorna Page do domínio
        return Page.of(cursos, pageable.page(), pageable.size(), springPage.getTotalElements());
    }

    private PageRequest createPageRequest(Pageable pageable) {
        if (pageable.sortBy() != null && pageable.sortDirection() != null) {
            Sort.Direction direction = "DESC".equalsIgnoreCase(pageable.sortDirection())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, pageable.sortBy());
            return PageRequest.of(pageable.page(), pageable.size(), sort);
        }
        return PageRequest.of(pageable.page(), pageable.size());
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

    // Suporte para salvar vários cursos (usado em reordenação)
    public void saveAll(List<Curso> cursos) {
        if (cursos == null || cursos.isEmpty()) return;
        for (Curso c : cursos) {
            CursoEntity e = CursoMapper.toEntity(c);
            cursoRepository.save(e);
        }
    }
}
