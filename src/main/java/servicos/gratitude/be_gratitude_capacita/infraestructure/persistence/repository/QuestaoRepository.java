package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AvaliacaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.QuestaoCompoundKey;

import java.util.List;

public interface QuestaoRepository extends JpaRepository<QuestaoEntity, QuestaoCompoundKey> {
    List<QuestaoEntity> findAllByAvaliacao(AvaliacaoEntity avaliacao);
}
