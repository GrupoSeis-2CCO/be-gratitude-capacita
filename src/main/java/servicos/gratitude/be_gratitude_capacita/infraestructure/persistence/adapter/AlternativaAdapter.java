package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AlternativaAdapter implements AlternativaGateway {
    private static final Logger log = LoggerFactory.getLogger(AlternativaAdapter.class);
    private final AlternativaRepository alternativaRepository;
    private final servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.QuestaoRepository questaoRepository;

    public AlternativaAdapter(AlternativaRepository alternativaRepository,
                              servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.QuestaoRepository questaoRepository) {
        this.alternativaRepository = alternativaRepository;
        this.questaoRepository = questaoRepository;
    }

    @Override
    public Alternativa save(Alternativa alternativa) {
        AlternativaEntity entity = AlternativaMapper.toEntity(alternativa);
        ensureQuestaoAssociation(entity, alternativa);

        Integer fkQuestao = entity.getFkQuestao();
        Integer fkAvaliacao = entity.getFkAvaliacao();

        if (fkQuestao == null || fkAvaliacao == null) {
            log.error("Não foi possível determinar fkQuestao/fkAvaliacao para salvar alternativa. fkQuestao={}, fkAvaliacao={}, alternativa={}", fkQuestao, fkAvaliacao, alternativa);
            throw new IllegalStateException("Não foi possível determinar fkQuestao/fkAvaliacao para gerar idAlternativa.");
        }

        if (entity.getIdAlternativa() == null) {
            Integer maxId = alternativaRepository.findMaxIdAlternativaByFkQuestaoAndFkAvaliacao(fkQuestao, fkAvaliacao);
            int newId = (maxId != null ? maxId : 0) + 1;
            entity.setIdAlternativa(newId);
            log.debug("Gerado idAlternativa={} para questao={} avaliacao={}", newId, fkQuestao, fkAvaliacao);
        }

        // Sempre setar a chave composta depois de todos os valores conhecidos
        var chave = entity.getAlternativaChaveComposta();
        if (chave == null) {
            chave = new servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey();
        }
        chave.setIdAlternativa(entity.getIdAlternativa());
        chave.setIdQuestao(fkQuestao);
        chave.setIdAvaliacao(fkAvaliacao);
        entity.setAlternativaChaveComposta(chave);

        return AlternativaMapper.toDomain(alternativaRepository.save(entity));
    }

    private void ensureQuestaoAssociation(AlternativaEntity entity, Alternativa alternativa) {
        Integer fkQuestao = null;
        Integer fkAvaliacao = null;
        Integer numeroQuestao = null;

        if (entity.getQuestao() != null) {
            var questaoEntity = entity.getQuestao();
            if (questaoEntity.getIdQuestaoComposto() != null) {
                fkQuestao = questaoEntity.getIdQuestaoComposto().getIdQuestao();
                fkAvaliacao = questaoEntity.getIdQuestaoComposto().getFkAvaliacao();
            }
            if (fkAvaliacao == null && questaoEntity.getAvaliacaoEntity() != null) {
                fkAvaliacao = questaoEntity.getAvaliacaoEntity().getIdAvaliacao();
            }
            numeroQuestao = questaoEntity.getNumeroQuestao();
        }

        if (entity.getFkQuestao() != null && fkQuestao == null) {
            fkQuestao = entity.getFkQuestao();
        }
        if (entity.getFkAvaliacao() != null && fkAvaliacao == null) {
            fkAvaliacao = entity.getFkAvaliacao();
        }

        Questao questaoDomain = alternativa != null ? alternativa.getQuestao() : null;
        if (questaoDomain != null) {
            if (numeroQuestao == null) {
                numeroQuestao = questaoDomain.getNumeroQuestao();
            }
            if (questaoDomain.getIdQuestaoComposto() != null) {
                if (fkQuestao == null) {
                    fkQuestao = questaoDomain.getIdQuestaoComposto().getIdQuestao();
                }
                if (fkAvaliacao == null) {
                    fkAvaliacao = questaoDomain.getIdQuestaoComposto().getFkAvaliacao();
                }
            }
            if (fkAvaliacao == null && questaoDomain.getAvaliacao() != null && questaoDomain.getAvaliacao().getIdAvaliacao() != null) {
                fkAvaliacao = questaoDomain.getAvaliacao().getIdAvaliacao();
            }
        }

        if ((entity.getQuestao() == null || fkQuestao == null) && fkAvaliacao != null) {
            if (numeroQuestao != null) {
                var questoes = questaoRepository.findAllByIdQuestaoComposto_FkAvaliacaoAndNumeroQuestao(fkAvaliacao, numeroQuestao);
                if (!questoes.isEmpty()) {
                    if (questoes.size() > 1) {
                        log.warn("Foram encontradas {} questões com número {} na avaliação {}. Será utilizada a primeira correspondente.", questoes.size(), numeroQuestao, fkAvaliacao);
                    }

                    final Integer fkQuestaoHint = fkQuestao;
                    var questaoEntity = questoes.stream()
                            .filter(q -> q.getIdQuestaoComposto() != null && fkQuestaoHint != null && fkQuestaoHint.equals(q.getIdQuestaoComposto().getIdQuestao()))
                            .findFirst()
                            .orElseGet(() -> questoes.stream()
                                    .filter(q -> q.getIdQuestaoComposto() != null)
                                    .min(Comparator.comparing(q -> q.getIdQuestaoComposto().getIdQuestao()))
                                    .orElse(questoes.get(0)));

                    entity.setQuestao(questaoEntity);
                    if (questaoEntity.getIdQuestaoComposto() != null) {
                        entity.setFkQuestao(questaoEntity.getIdQuestaoComposto().getIdQuestao());
                    }
                } else {
                    log.warn("Questão não encontrada para número {} da avaliação {} ao salvar alternativa {}.", numeroQuestao, fkAvaliacao, entity.getIdAlternativa());
                }
                fkQuestao = entity.getFkQuestao();
            } else if (fkQuestao != null) {
                var chave = new servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.QuestaoEntityCompoundKey();
                chave.setIdQuestao(fkQuestao);
                chave.setFkAvaliacao(fkAvaliacao);
                var questaoOpt = questaoRepository.findById(chave);
                if (questaoOpt.isPresent()) {
                    entity.setQuestao(questaoOpt.get());
                } else {
                    log.warn("Questão com id {} e avaliação {} não encontrada ao salvar alternativa {}.", fkQuestao, fkAvaliacao, entity.getIdAlternativa());
                }
            }
        }

        if (fkQuestao == null && entity.getQuestao() != null && entity.getQuestao().getIdQuestaoComposto() != null) {
            fkQuestao = entity.getQuestao().getIdQuestaoComposto().getIdQuestao();
        }

        if (fkQuestao == null || fkAvaliacao == null) {
            log.warn("Chaves da questão ainda estão ausentes ao salvar alternativa {}. fkQuestao={}, fkAvaliacao={}, numeroQuestao={}", entity.getIdAlternativa(), fkQuestao, fkAvaliacao, numeroQuestao);
        }

        entity.setFkQuestao(fkQuestao);
        entity.setFkAvaliacao(fkAvaliacao);

        log.debug("Questão resolvida para alternativa {} -> fkQuestao={}, fkAvaliacao={}, numeroQuestao={}", entity.getIdAlternativa(), fkQuestao, fkAvaliacao, numeroQuestao);
    }

    @Override
    public List<Alternativa> findAllByQuestao(Questao questao) {
        // Use only the composite key to avoid NPE when questao has partial data
        return AlternativaMapper.toDomains(
                alternativaRepository.findAllByQuestao(QuestaoMapper.toEntityKeyOnly(questao)),
                false // do not include questao to avoid circular mapping and unnecessary loads
        );
    }

    @Override
    public Alternativa findById(AlternativaCompoundKey idComposto) {
        Optional<AlternativaEntity> entity = alternativaRepository.findById(idComposto.getIdAlternativa());

        return entity.map(AlternativaMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(AlternativaCompoundKey idComposto) {
        return alternativaRepository.existsById(idComposto.getIdAlternativa());
    }

    @Override
    public void deleteById(AlternativaCompoundKey idComposto) {
        // Buscar a AlternativaEntity
        Optional<AlternativaEntity> altOpt = alternativaRepository.findById(idComposto.getIdAlternativa());
        if (altOpt.isPresent()) {
            AlternativaEntity altEntity = altOpt.get();
            // Remover referência em questões que usam como fkAlternativaCorreta
            var questoes = questaoRepository.findByFkAlternativaCorreta(altEntity);
            for (var questao : questoes) {
                questao.setFkAlternativaCorreta(null);
                questaoRepository.save(questao);
            }
            alternativaRepository.deleteById(idComposto.getIdAlternativa());
        }
    }
}
