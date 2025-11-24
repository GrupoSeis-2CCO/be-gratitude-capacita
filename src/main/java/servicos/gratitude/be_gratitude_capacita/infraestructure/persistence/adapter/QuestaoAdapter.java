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

        // If the composite id has no idQuestao, assign a new id based on max(id_questao)+1
        if (entity.getIdQuestaoComposto() != null && entity.getIdQuestaoComposto().getIdQuestao() == null) {
            int next = Optional.ofNullable(questaoRepository.findMaxIdQuestao()).orElse(0) + 1;
            entity.getIdQuestaoComposto().setIdQuestao(next);
        }

        QuestaoEntity saved = questaoRepository.save(entity);
        try {
            questaoRepository.flush();
        } catch (Exception e) {
            // não falhar em produção: apenas logar
            System.out.println("[QuestaoAdapter] Falha ao flush após save: " + e.getMessage());
        }
        try {
            if (saved.getIdQuestaoComposto() != null) {
                System.out.println("[QuestaoAdapter] Questao salva: idQuestao=" + saved.getIdQuestaoComposto().getIdQuestao() + ", fkAvaliacao=" + saved.getIdQuestaoComposto().getFkAvaliacao());
            } else {
                System.out.println("[QuestaoAdapter] Questao salva sem chave composta visível imediatamente");
            }
        } catch (Exception e) {
            System.out.println("[QuestaoAdapter] Erro ao logar questao salva: " + e.getMessage());
        }
        return QuestaoMapper.toDomain(saved);
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
