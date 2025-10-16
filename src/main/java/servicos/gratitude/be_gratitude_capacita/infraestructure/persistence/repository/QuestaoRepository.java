
package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AvaliacaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.QuestaoEntityCompoundKey;

import java.util.List;

public interface QuestaoRepository extends JpaRepository<QuestaoEntity, QuestaoEntityCompoundKey> {
    List<QuestaoEntity> findAllByAvaliacao(AvaliacaoEntity avaliacao);
    List<QuestaoEntity> findByFkAlternativaCorreta(AlternativaEntity alternativa);
    List<QuestaoEntity> findAllByIdQuestaoComposto_FkAvaliacaoAndNumeroQuestao(Integer fkAvaliacao, Integer numeroQuestao);
}
