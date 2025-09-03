package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CargoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.UsuarioAdapter;

@Configuration
public class UsuarioBeanConfig {

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioAdapter usuarioAdapter, CargoAdapter cargoAdapter){
        return new CriarUsuarioUseCase(usuarioAdapter, cargoAdapter);
    }

    @Bean
    public AtualizarAcessoUseCase atualizarAcessoUseCase(UsuarioAdapter usuarioAdapter){
        return new AtualizarAcessoUseCase(usuarioAdapter);
    }

    @Bean
    public AtualizarSenhaUseCase atualizarSenhaUseCase(UsuarioAdapter usuarioAdapter){
        return new AtualizarSenhaUseCase(usuarioAdapter);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioAdapter usuarioAdapter){
        return new ListarUsuariosUseCase(usuarioAdapter);
    }

    @Bean
    public BuscarUsuarioPorIdUseCase buscarUsuariosUseCase(UsuarioAdapter usuarioAdapter){
        return new BuscarUsuarioPorIdUseCase(usuarioAdapter);
    }

    @Bean
    public DeletarUsuarioUseCase deletarUsuariosUseCase(UsuarioAdapter usuarioAdapter){
        return new DeletarUsuarioUseCase(usuarioAdapter);
    }

    @Bean
    public PesquisarPorNomeUseCase pesquisarPorNomeUseCase(UsuarioAdapter usuarioAdapter){
        return new PesquisarPorNomeUseCase(usuarioAdapter);
    }
}
