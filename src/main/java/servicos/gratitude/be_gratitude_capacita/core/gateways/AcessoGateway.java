package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Acesso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

import java.util.List;

public interface AcessoGateway {
    Acesso save(Acesso acesso);
    List<Acesso> findAllByUsuario(Usuario usuario);
}
