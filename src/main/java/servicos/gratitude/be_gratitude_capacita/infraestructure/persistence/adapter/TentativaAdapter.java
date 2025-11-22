package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.MatriculaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.QuestaoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.TentativaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.TentativaCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.TentativaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TentativaAdapter implements TentativaGateway {
    private final TentativaRepository tentativaRepository;

    public TentativaAdapter(TentativaRepository tentativaRepository) {
        this.tentativaRepository = tentativaRepository;
    }


    @Override
    public Tentativa save(Tentativa tentativa) {
        TentativaEntity entity = TentativaMapper.toEntity(tentativa);

        return TentativaMapper.toDomain(tentativaRepository.save(entity));
    }

    @Override
    public List<Tentativa> findAllByMatricula(Matricula matricula) {
        return TentativaMapper.toDomains(tentativaRepository.findAllByMatricula(MatriculaMapper.toEntity(matricula)));
    }

    @Override
    public List<Tentativa> findAllByUsuario(Integer fkUsuario) {
        try {
            java.util.List<servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity> results = tentativaRepository.findAllByUsuario(fkUsuario);
            if (results == null) return new java.util.ArrayList<>();
            return TentativaMapper.toDomains(results);
        } catch (Exception e) {
            // log and return empty list so controller can respond with 204 instead of 500
            org.slf4j.LoggerFactory.getLogger(TentativaAdapter.class).error("Erro ao buscar tentativas por usuario {}", fkUsuario, e);
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public Tentativa findById(TentativaCompoundKey idComposto) {
        Optional<TentativaEntity> entity = tentativaRepository.findById(TentativaCompoundKeyMapper.toEntity(idComposto));

        return entity.map(TentativaMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(TentativaCompoundKey idComposto) {
        return tentativaRepository.existsById(TentativaCompoundKeyMapper.toEntity(idComposto));
    }

    @Override
    public long countByAvaliacaoId(Integer idAvaliacao) {
        try {
            return tentativaRepository.countByAvaliacaoId(idAvaliacao);
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(TentativaAdapter.class).error("Erro ao contar tentativas por avaliacao {}", idAvaliacao, e);
            return 0L;
        }
    }

    @Override
    public int deleteByAvaliacaoId(Integer idAvaliacao) {
        int deleted = 0;
        try {
            deleted = tentativaRepository.deleteByAvaliacaoId(idAvaliacao);
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(TentativaAdapter.class).error("Erro ao deletar tentativas por avaliacao {}", idAvaliacao, e);
        }
        try {
            tentativaRepository.flush();
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(TentativaAdapter.class).warn("Falha ao flush após deletar tentativas avaliacao {}: {}", idAvaliacao, e.getMessage());
        }
        return deleted;
    }
}
