package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.RespostaDoUsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import java.util.List;

public interface RespostaDoUsuarioRepository extends JpaRepository<RespostaDoUsuarioEntity, RespostaDoUsuarioEntityCompoundKey> {
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
}
