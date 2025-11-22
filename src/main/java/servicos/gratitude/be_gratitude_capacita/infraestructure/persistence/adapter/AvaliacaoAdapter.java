package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AvaliacaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.AvaliacaoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.AvaliacaoRepository;

import java.util.Optional;

@Service

public class AvaliacaoAdapter implements AvaliacaoGateway {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoAdapter(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @Override
    public Avaliacao save(Avaliacao avaliacao) {
        AvaliacaoEntity entity = AvaliacaoMapper.toEntity(avaliacao);

        return AvaliacaoMapper.toDomain(avaliacaoRepository.save(entity));
    }

    @Override
    public Boolean existsById(Integer id) {
        return avaliacaoRepository.existsById(id);
    }

    @Override
    public Avaliacao findById(Integer id) {
        Optional<AvaliacaoEntity> entity = avaliacaoRepository.findById(id);

        return entity.map(AvaliacaoMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsByCurso(Curso curso) {
        return avaliacaoRepository.existsByFkCurso(CursoMapper.toEntity(curso));
    }

    @Override
    public java.util.List<Avaliacao> findAllByCurso(Curso curso) {
        return AvaliacaoMapper.toDomain(avaliacaoRepository.findAllByFkCurso(CursoMapper.toEntity(curso)));
    }

    @Override
    public java.util.List<Avaliacao> findAll() {
        return AvaliacaoMapper.toDomain(avaliacaoRepository.findAll());
    }

    @Override
    public Optional<Avaliacao> findByFkCursoId(Long idCurso) {
        return avaliacaoRepository.findFirstByFkCurso_IdCurso(idCurso)
                .map(AvaliacaoMapper::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        avaliacaoRepository.deleteById(id);
    }
}
