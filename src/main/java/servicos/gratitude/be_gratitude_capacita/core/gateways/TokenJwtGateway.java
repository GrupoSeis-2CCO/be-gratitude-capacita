package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

public interface TokenJwtGateway {
    String gerarToken(Usuario usuario);
}
