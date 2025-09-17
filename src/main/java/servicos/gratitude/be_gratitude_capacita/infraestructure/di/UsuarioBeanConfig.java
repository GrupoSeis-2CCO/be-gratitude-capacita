package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.TokenJwtAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CargoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.UsuarioAdapter;

@Configuration
public class UsuarioBeanConfig {

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioAdapter usuarioAdapter, CargoAdapter cargoAdapter) {
        return new CriarUsuarioUseCase(usuarioAdapter, cargoAdapter);
    }

    @Bean
    public AtualizarAcessoUsuarioUseCase atualizarAcessoUseCase(UsuarioAdapter usuarioAdapter) {
        return new AtualizarAcessoUsuarioUseCase(usuarioAdapter);
    }

    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase(UsuarioAdapter usuarioAdapter,
            TokenJwtAdapter tokenJwtAdapter) {
        return new AutenticarUsuarioUseCase(usuarioAdapter, tokenJwtAdapter);
    }

    @Bean
    public AtualizarSenhaUsuarioUseCase atualizarSenhaUseCase(UsuarioAdapter usuarioAdapter) {
        return new AtualizarSenhaUsuarioUseCase(usuarioAdapter);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioAdapter usuarioAdapter) {
        return new ListarUsuariosUseCase(usuarioAdapter);
    }

    @Bean
    public BuscarUsuarioPorIdUseCase buscarUsuariosUseCase(UsuarioAdapter usuarioAdapter) {
        return new BuscarUsuarioPorIdUseCase(usuarioAdapter);
    }

    @Bean
    public DeletarUsuarioUseCase deletarUsuariosUseCase(UsuarioAdapter usuarioAdapter) {
        return new DeletarUsuarioUseCase(usuarioAdapter);
    }

    @Bean
    public PesquisarPorNomeDeUsuarioUseCase pesquisarPorNomeUseCase(UsuarioAdapter usuarioAdapter) {
        return new PesquisarPorNomeDeUsuarioUseCase(usuarioAdapter);
    }
}
