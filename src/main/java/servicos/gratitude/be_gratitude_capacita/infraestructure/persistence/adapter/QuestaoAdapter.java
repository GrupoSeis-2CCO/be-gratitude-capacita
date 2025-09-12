package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.AvaliacaoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.QuestaoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.QuestaoCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.QuestaoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuestaoAdapter implements QuestaoGateway {
    private final QuestaoRepository questaoRepository;

    public QuestaoAdapter(QuestaoRepository questaoRepository) {
        this.questaoRepository = questaoRepository;
    }

    @Override
    public Questao save(Questao questao) {
        QuestaoEntity entity = QuestaoMapper.toEntity(questao);

        return QuestaoMapper.toDomain(questaoRepository.save(entity));
    }

    @Override
    public List<Questao> findAllByAvaliacao(Avaliacao avaliacao) {
        return QuestaoMapper.toDomains(questaoRepository.findAllByAvaliacao(AvaliacaoMapper.toEntity(avaliacao)));
    }

    @Override
    public Questao findById(QuestaoCompoundKey idComposto) {
        Optional<QuestaoEntity> entity = questaoRepository.findById(QuestaoCompoundKeyMapper.toEntity(idComposto));

            return entity.map(QuestaoMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(QuestaoCompoundKey idComposto) {
        return questaoRepository.existsById(QuestaoCompoundKeyMapper.toEntity(idComposto));
    }

    @Override
    public void deleteById(QuestaoCompoundKey idComposto) {
        questaoRepository.deleteById(QuestaoCompoundKeyMapper.toEntity(idComposto));
    }
}
