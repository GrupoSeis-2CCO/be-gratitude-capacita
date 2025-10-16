package servicos.gratitude.be_gratitude_capacita.infraestructure.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CascadeDeletionService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void deleteCursoComVinculos(Integer idCurso) {
        if (idCurso == null) return;

        // Ordem de exclusão para respeitar FKs
        // 1) Respostas do usuário (tem FK_curso)
        em.createNativeQuery("DELETE FROM resposta_do_usuario WHERE FK_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 2) Material do aluno (tem FK_curso)
        em.createNativeQuery("DELETE FROM material_aluno WHERE FK_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 3) Tentativas (tem fk_curso)
        em.createNativeQuery("DELETE FROM tentativa WHERE fk_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 4) Alternativas vinculadas às avaliações do curso
        em.createNativeQuery("DELETE FROM alternativa WHERE FK_avaliacao IN (SELECT id_avaliacao FROM avaliacao WHERE FK_curso = :id)")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 5) Questões vinculadas às avaliações do curso
        em.createNativeQuery("DELETE FROM questao WHERE fk_avaliacao IN (SELECT id_avaliacao FROM avaliacao WHERE FK_curso = :id)")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 6) Avaliações do curso
        em.createNativeQuery("DELETE FROM avaliacao WHERE FK_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 7) Apostilas e Vídeos do curso
        em.createNativeQuery("DELETE FROM apostila WHERE FK_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();
        em.createNativeQuery("DELETE FROM video WHERE FK_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 8) Matrículas e Feedbacks do curso
        em.createNativeQuery("DELETE FROM matricula WHERE FK_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();
        em.createNativeQuery("DELETE FROM feedback WHERE FK_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();

        // 9) Finalmente, o curso
        em.createNativeQuery("DELETE FROM curso WHERE id_curso = :id")
                .setParameter("id", idCurso)
                .executeUpdate();
    }
}
