
package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.AlternativaMapper;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.RespostaDoUsuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.RespostaDoUsuarioCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RespostaDoUsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.RespostaDoUsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.RespostaDoUsuarioMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.RespostaDoUsuarioCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.RespostaDoUsuarioRepository;

import java.util.List;

@Service
public class RespostaDoUsuarioAdapter implements RespostaDoUsuarioGateway {
    private final RespostaDoUsuarioRepository respostaDoUsuarioRepository;

    @Override
    public Boolean existsByAlternativa(servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa alternativa) {
        Integer idAlternativa = alternativa.getAlternativaChaveComposta() != null ? alternativa.getAlternativaChaveComposta().getIdAlternativa() : null;
        if (idAlternativa == null) return false;
        return respostaDoUsuarioRepository.existsByAlternativa_IdAlternativa(idAlternativa);
    }

    @Override
    public long countByExamId(Integer examId) {
        try {
            return respostaDoUsuarioRepository.countByExamId(examId);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public void deleteByExamId(Integer examId) {
        int jpqlDeleted = 0;
        try {
            jpqlDeleted = respostaDoUsuarioRepository.deleteByExamId(examId);
        } catch (Exception e) {
            System.out.println("[RespostaDoUsuarioAdapter] Erro JPQL deleteByExamId: " + e.getMessage());
        }
        try {
            respostaDoUsuarioRepository.flush();
        } catch (Exception e) {
            System.out.println("[RespostaDoUsuarioAdapter] Falha ao flush após JPQL delete: " + e.getMessage());
        }
        long remainingAfterJpql = 0L;
        try {
            remainingAfterJpql = respostaDoUsuarioRepository.countByExamId(examId);
        } catch (Exception e) {
            System.out.println("[RespostaDoUsuarioAdapter] Erro ao contar após JPQL delete: " + e.getMessage());
        }
        if (remainingAfterJpql > 0) {
            System.out.println("[RespostaDoUsuarioAdapter] Ainda restam " + remainingAfterJpql + " respostas após JPQL delete (" + jpqlDeleted + " removidas). Tentando native delete...");
            int nativeDeleted = 0;
            try {
                nativeDeleted = respostaDoUsuarioRepository.nativeDeleteByExamId(examId);
                respostaDoUsuarioRepository.flush();
            } catch (Exception e) {
                System.out.println("[RespostaDoUsuarioAdapter] Falha ao executar native deleteByExamId: " + e.getMessage());
            }
            long remainingAfterNative = 0L;
            try {
                remainingAfterNative = respostaDoUsuarioRepository.countByExamId(examId);
            } catch (Exception e) {
                System.out.println("[RespostaDoUsuarioAdapter] Erro ao contar após native delete: " + e.getMessage());
            }
            System.out.println("[RespostaDoUsuarioAdapter] Native delete removed=" + nativeDeleted + ", remaining=" + remainingAfterNative);
        } else {
            System.out.println("[RespostaDoUsuarioAdapter] Respostas removidas via JPQL: " + jpqlDeleted + ". Nenhuma restante.");
        }

        // Fallback final: se ainda existirem respostas presas por FK_alternativa (inconsistência de idAvaliacao), remover por escopo de alternativas
        long altScopeRemaining = 0L;
        try {
            altScopeRemaining = respostaDoUsuarioRepository.countByExamAlternativeScope(examId);
        } catch (Exception e) {
            System.out.println("[RespostaDoUsuarioAdapter] Erro ao contar escopo de alternativas: " + e.getMessage());
        }
        if (altScopeRemaining > 0) {
            System.out.println("[RespostaDoUsuarioAdapter] Fallback alternativas: restam " + altScopeRemaining + " respostas associadas via FK_alternativa. Executando remoção por alternativas...");
            int altScopeDeleted = 0;
            try {
                altScopeDeleted = respostaDoUsuarioRepository.nativeDeleteByExamAlternativeScope(examId);
                respostaDoUsuarioRepository.flush();
            } catch (Exception e) {
                System.out.println("[RespostaDoUsuarioAdapter] Falha ao executar nativeDeleteByExamAlternativeScope: " + e.getMessage());
            }
            long afterAltScope = 0L;
            try {
                afterAltScope = respostaDoUsuarioRepository.countByExamAlternativeScope(examId);
            } catch (Exception e) {
                System.out.println("[RespostaDoUsuarioAdapter] Erro ao contar após alt-scope delete: " + e.getMessage());
            }
            System.out.println("[RespostaDoUsuarioAdapter] Fallback alternativas removidas=" + altScopeDeleted + ", ainda restantes=" + afterAltScope);
        }
    }
    
    public RespostaDoUsuarioAdapter(RespostaDoUsuarioRepository respostaDoUsuarioRepository) {
        this.respostaDoUsuarioRepository = respostaDoUsuarioRepository;
    }


    @Override
    public RespostaDoUsuario save(RespostaDoUsuario respostaDoUsuario) {
        RespostaDoUsuarioEntity entity = RespostaDoUsuarioMapper.toEntity(respostaDoUsuario);

        return RespostaDoUsuarioMapper.toDomain(respostaDoUsuarioRepository.save(entity));
    }

    @Override
    public List<RespostaDoUsuario> findAll() {
        return RespostaDoUsuarioMapper.toDomains(respostaDoUsuarioRepository.findAll());
    }

    @Override
    public List<RespostaDoUsuario> findAllByTentativa(servicos.gratitude.be_gratitude_capacita.core.domain.Tentativa tentativa) {
        // Use composite key fields instead of entity reference to avoid lazy initialization issues
        Integer fkCurso = tentativa.getIdTentativaComposto() != null && tentativa.getIdTentativaComposto().getIdMatriculaComposto() != null 
            ? tentativa.getIdTentativaComposto().getIdMatriculaComposto().getFkCurso() 
            : null;
        Integer fkUsuario = tentativa.getIdTentativaComposto() != null && tentativa.getIdTentativaComposto().getIdMatriculaComposto() != null 
            ? tentativa.getIdTentativaComposto().getIdMatriculaComposto().getFkUsuario() 
            : null;
        Integer fkTentativa = tentativa.getIdTentativaComposto() != null 
            ? tentativa.getIdTentativaComposto().getIdTentativa() 
            : null;
        
        if (fkCurso == null || fkUsuario == null || fkTentativa == null) {
            return java.util.Collections.emptyList();
        }
        
        List<RespostaDoUsuarioEntity> entities = respostaDoUsuarioRepository.findAllByTentativaKeys(fkCurso, fkUsuario, fkTentativa);
        return RespostaDoUsuarioMapper.toDomains(entities);
    }

    @Override
    public Boolean existsById(RespostaDoUsuarioCompoundKey idComposto) {
        return respostaDoUsuarioRepository.existsById(RespostaDoUsuarioCompoundKeyMapper.toEntity(idComposto));
    }
}
