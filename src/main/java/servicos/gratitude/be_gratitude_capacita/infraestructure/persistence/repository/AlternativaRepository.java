package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.compoundKeys.AlternativaCompoundKey;

import java.util.List;

public interface AlternativaRepository extends JpaRepository<AlternativaEntity, AlternativaCompoundKey> {
    List<AlternativaEntity> findAllByQuestao(QuestaoEntity questao);
}
