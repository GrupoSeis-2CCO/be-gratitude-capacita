package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

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
    public Boolean existsById(RespostaDoUsuarioCompoundKey idComposto) {
        return respostaDoUsuarioRepository.existsById(RespostaDoUsuarioCompoundKeyMapper.toEntity(idComposto));
    }
}
