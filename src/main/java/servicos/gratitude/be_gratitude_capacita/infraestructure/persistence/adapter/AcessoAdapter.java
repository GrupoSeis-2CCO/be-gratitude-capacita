package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Acesso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AcessoGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.AcessoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.AcessoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.UsuarioMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.AcessoRepository;

import java.util.List;

@Service
public class AcessoAdapter implements AcessoGateway {
    private final AcessoRepository acessoRepository;

    public AcessoAdapter(AcessoRepository acessoRepository) {
        this.acessoRepository = acessoRepository;
    }

    @Override
    public Acesso save(Acesso acesso) {
        AcessoEntity entity = AcessoMapper.toEntity(acesso);

        return AcessoMapper.toDomain(acessoRepository.save(entity));
    }

    @Override
    public List<Acesso> findAllByUsuario(Usuario usuario) {
        return AcessoMapper.toDomains(acessoRepository.findAllByFkUsuario(UsuarioMapper.toEntity(usuario)));
    }
}
