package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.AlternativaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.QuestaoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.AlternativaCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.AlternativaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlternativaAdapter implements AlternativaGateway {
    private final AlternativaRepository alternativaRepository;

    public AlternativaAdapter(AlternativaRepository alternativaRepository) {
        this.alternativaRepository = alternativaRepository;
    }

    @Override
    public Alternativa save(Alternativa alternativa) {
        AlternativaEntity entity = AlternativaMapper.toEntity(alternativa);

        return AlternativaMapper.toDomain(alternativaRepository.save(entity));
    }

    @Override
    public List<Alternativa> findAllByQuestao(Questao questao) {
        return AlternativaMapper.toDomains(alternativaRepository.findAllByQuestao(QuestaoMapper.toEntity(questao)));
    }

    @Override
    public Alternativa findById(AlternativaCompoundKey idComposto) {
        Optional<AlternativaEntity> entity = alternativaRepository.findById(AlternativaCompoundKeyMapper.toEntity(idComposto));

        return entity.map(AlternativaMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(AlternativaCompoundKey idComposto) {
        return alternativaRepository.existsById(AlternativaCompoundKeyMapper.toEntity(idComposto));
    }

    @Override
    public void deleteById(AlternativaCompoundKey idComposto) {
        alternativaRepository.deleteById(AlternativaCompoundKeyMapper.toEntity(idComposto));
    }
}
