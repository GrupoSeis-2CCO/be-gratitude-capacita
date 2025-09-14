package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.CriarRespostaDoUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.ListarRespostasDoUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.RespostaDoUsuarioAdapter;

@Configuration
public class RespostaDoUsuarioBeanConfig {

    @Bean
    public CriarRespostaDoUsuarioUseCase criarRespostaDoUsuarioUseCase(RespostaDoUsuarioAdapter respostaDoUsuarioAdapter){
        return new CriarRespostaDoUsuarioUseCase(respostaDoUsuarioAdapter);
    }

    @Bean
    public ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase(RespostaDoUsuarioAdapter respostaDoUsuarioAdapter){
        return new ListarRespostasDoUsuarioUseCase(respostaDoUsuarioAdapter);
    }
}
