
package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.RespostaDoUsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import java.util.List;

public interface RespostaDoUsuarioRepository extends JpaRepository<RespostaDoUsuarioEntity, RespostaDoUsuarioEntityCompoundKey> {
	boolean existsByAlternativa_IdAlternativa(Integer idAlternativa);

	@Query("SELECT COUNT(r) FROM RespostaDoUsuarioEntity r WHERE r.respostaDoUsuarioEntityCompoundKey.idAvaliacao = :examId")
	long countByExamId(@Param("examId") Integer examId);

	@Modifying
	@Query("DELETE FROM RespostaDoUsuarioEntity r WHERE r.respostaDoUsuarioEntityCompoundKey.idAvaliacao = :examId")
	int deleteByExamId(@Param("examId") Integer examId);

	@Modifying
	@Query(value = "DELETE FROM resposta_do_usuario WHERE FK_avaliacao = :examId", nativeQuery = true)
	int nativeDeleteByExamId(@Param("examId") Integer examId);

	// Fallback: remove respostas que ainda referenciam alternativas desta avaliação
	@Query(value = "SELECT COUNT(*) FROM resposta_do_usuario r WHERE r.FK_alternativa IN (SELECT a.id_alternativa FROM alternativa a JOIN questao q ON a.fk_questao = q.id_questao WHERE q.fk_avaliacao = :examId)", nativeQuery = true)
	long countByExamAlternativeScope(@Param("examId") Integer examId);

	@Modifying
	@Query(value = "DELETE FROM resposta_do_usuario WHERE FK_alternativa IN (SELECT a.id_alternativa FROM alternativa a JOIN questao q ON a.fk_questao = q.id_questao WHERE q.fk_avaliacao = :examId)", nativeQuery = true)
	int nativeDeleteByExamAlternativeScope(@Param("examId") Integer examId);
	@Query("SELECT r FROM RespostaDoUsuarioEntity r " +
		   "JOIN FETCH r.alternativa a " +
		   "JOIN FETCH a.questao q " +
		   "LEFT JOIN FETCH q.fkAlternativaCorreta " +
		   "WHERE r.respostaDoUsuarioEntityCompoundKey.idTentativaComposto.idMatriculaComposto.fkCurso = :fkCurso " +
		   "AND r.respostaDoUsuarioEntityCompoundKey.idTentativaComposto.idMatriculaComposto.fkUsuario = :fkUsuario " +
		   "AND r.respostaDoUsuarioEntityCompoundKey.idTentativaComposto.idTentativa = :fkTentativa")
	List<RespostaDoUsuarioEntity> findAllByTentativaKeys(
		@Param("fkCurso") Integer fkCurso,
		@Param("fkUsuario") Integer fkUsuario,
		@Param("fkTentativa") Integer fkTentativa
	);
    
	@Query("SELECT r FROM RespostaDoUsuarioEntity r " +
		   "JOIN FETCH r.alternativa a " +
		   "JOIN FETCH a.questao q " +
		   "LEFT JOIN FETCH q.fkAlternativaCorreta " +
		   "WHERE r.tentativa = :tentativa")
	List<RespostaDoUsuarioEntity> findAllByTentativa(@Param("tentativa") TentativaEntity tentativa);
    
	@Query("SELECT r FROM RespostaDoUsuarioEntity r " +
		   "WHERE r.respostaDoUsuarioEntityCompoundKey.idTentativaComposto.idMatriculaComposto.fkUsuario = :userId " +
		   "AND r.respostaDoUsuarioEntityCompoundKey.idAvaliacao = :examId")
	List<RespostaDoUsuarioEntity> findByUserIdAndExamId(
		@Param("userId") Integer userId,
		@Param("examId") Integer examId
	);
}
