package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AlternativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.QuestaoEntity;

import java.util.List;

public interface AlternativaRepository extends JpaRepository<AlternativaEntity, Integer> {
    List<AlternativaEntity> findAllByQuestao(QuestaoEntity questao);

    // Find max idAlternativa for a given questao and avaliacao
    @Query("SELECT COALESCE(MAX(a.idAlternativa), 0) FROM AlternativaEntity a WHERE a.fkQuestao = ?1 AND a.fkAvaliacao = ?2")
    Integer findMaxIdAlternativaByFkQuestaoAndFkAvaliacao(Integer fkQuestao, Integer fkAvaliacao);
}
