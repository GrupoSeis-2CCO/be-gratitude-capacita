package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.ApostilaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.ApostilaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ApostilaAdapter implements ApostilaGateway {
    private final ApostilaRepository apostilaRepository;

    public ApostilaAdapter(ApostilaRepository apostilaRepository) {
        this.apostilaRepository = apostilaRepository;
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
    public void deleteById(Integer id) {
        deleteById(id);
    }
}
