package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.ApostilaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.ApostilaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MaterialAlunoRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;

import java.util.List;
import java.util.Optional;

@Service
public class ApostilaAdapter implements ApostilaGateway {
    private final ApostilaRepository apostilaRepository;
    private final MaterialAlunoRepository materialAlunoRepository;

    public ApostilaAdapter(ApostilaRepository apostilaRepository, MaterialAlunoRepository materialAlunoRepository) {
        this.apostilaRepository = apostilaRepository;
        this.materialAlunoRepository = materialAlunoRepository;
    }

    @Override
    public Apostila save(Apostila apostila) {
        ApostilaEntity entity = ApostilaMapper.toEntity(apostila);

        return ApostilaMapper.toDomain(apostilaRepository.save(entity));
    }

    @Override
    public List<Apostila> findAllByCurso(Curso curso) {
        return ApostilaMapper.toDomains(apostilaRepository.findAllByFkCurso(CursoMapper.toEntity(curso)));
    }

    @Override
    public Page<Apostila> findAllByCurso(Curso curso, Pageable pageable) {
        // Precisa implementar método paginado no repository
        // Por enquanto, vou simular usando findAll com paginação
        PageRequest pageRequest = createPageRequest(pageable);
        org.springframework.data.domain.Page<ApostilaEntity> springPage = apostilaRepository
                .findAllByFkCurso(CursoMapper.toEntity(curso), pageRequest);

        List<Apostila> apostilas = ApostilaMapper.toDomains(springPage.getContent());

        return Page.of(apostilas, pageable.page(), pageable.size(), springPage.getTotalElements());
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
    public Apostila findById(Integer id) {
        Optional<ApostilaEntity> entity = apostilaRepository.findById(id);

        return entity.map(ApostilaMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(Integer id) {
        return apostilaRepository.existsById(id);
    }

    @Override
    public Boolean existsByNome(String nome) {
        return apostilaRepository.existsByNomeApostilaOriginal(nome);
    }

    @Override
    public Apostila findByNome(String nome) {
        Optional<ApostilaEntity> entity = apostilaRepository.findByNomeApostilaOriginal(nome);

        return entity.map(ApostilaMapper::toDomain).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        // Primeiro removemos registros dependentes na tabela material_aluno para evitar violação de FK
        ApostilaEntity ae = new ApostilaEntity();
        ae.setIdApostila(id);
        materialAlunoRepository.deleteAllByFkApostila(ae);
        // Agora deletamos a apostila
        apostilaRepository.deleteById(id);
    }
}
