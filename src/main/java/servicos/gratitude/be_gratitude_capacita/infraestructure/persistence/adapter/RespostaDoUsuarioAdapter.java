
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
