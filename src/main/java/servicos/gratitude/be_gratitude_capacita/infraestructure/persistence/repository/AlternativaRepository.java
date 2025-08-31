package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.AlternativaEntityCompoundKey;

import java.util.List;

public interface AlternativaRepository extends JpaRepository<AlternativaEntity, AlternativaEntityCompoundKey> {
    List<AlternativaEntity> findAllByQuestao(QuestaoEntity questao);
}
