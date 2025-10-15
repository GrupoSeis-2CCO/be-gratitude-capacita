package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MaterialAlunoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.MaterialAlunoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.MatriculaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MaterialAlunoCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MaterialAlunoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialAlunoAdapter implements MaterialAlunoGateway {
    private static final Logger logger = LoggerFactory.getLogger(MaterialAlunoAdapter.class);

    private final MaterialAlunoRepository materialAlunoRepository;

    public MaterialAlunoAdapter(MaterialAlunoRepository materialAlunoRepository) {
        this.materialAlunoRepository = materialAlunoRepository;
    }

    @Override
    public MaterialAluno save(MaterialAluno materialAluno) {
        MaterialAlunoEntity entity = MaterialAlunoMapper.toEntity(materialAluno);

        return MaterialAlunoMapper.toDomain(materialAlunoRepository.save(entity));
    }

    @Override
    public List<MaterialAluno> findAllByMatricula(Matricula matricula) {
        try {
            List<servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MaterialAlunoEntity> entities = materialAlunoRepository.findAllByMatricula(MatriculaMapper.toEntity(matricula));
            List<MaterialAluno> domains = MaterialAlunoMapper.toDomains(entities);
            logger.info("MaterialAlunoAdapter.findAllByMatricula: matricula={} returned {} materials", matricula != null ? matricula.getIdMatriculaComposto() : null, domains.size());
            return domains;
        } catch (Exception e) {
            logger.error("Error fetching MaterialAluno for matricula {}", matricula != null ? matricula.getIdMatriculaComposto() : null, e);
            throw e;
        }
    }

    @Override
    public MaterialAluno findById(MaterialAlunoCompoundKey idComposto) {
        Optional<MaterialAlunoEntity> entity = materialAlunoRepository.findById(MaterialAlunoCompoundKeyMapper.toEntity(idComposto));

        return entity.map(MaterialAlunoMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(MaterialAlunoCompoundKey idComposto) {
        return materialAlunoRepository.existsById(MaterialAlunoCompoundKeyMapper.toEntity(idComposto));
    }

    @Override
    public void deleteById(MaterialAlunoCompoundKey idComposto) {
        materialAlunoRepository.deleteById(MaterialAlunoCompoundKeyMapper.toEntity(idComposto));
    }
}
