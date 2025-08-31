package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;

@Configuration
public class CursoBeanConfig {

    @Bean
    public CriarCursoUseCase criarCursoUseCase(CursoAdapter adapter){
        return new CriarCursoUseCase(adapter);
    }

    @Bean
    public AtualizarCursoUseCase atualizarCursoUseCase(CursoAdapter adapter){
        return new AtualizarCursoUseCase(adapter);
    }

    @Bean
    public AtualizarOcultoUseCase atualizarOcultoUseCase(CursoAdapter adapter){
        return new AtualizarOcultoUseCase(adapter);
    }

    @Bean
    public DeletarCursoUseCase deletarCursoUseCase(CursoAdapter adapter){
        return new DeletarCursoUseCase(adapter);
    }

    @Bean
    public ListarCursoUseCase listarCursoUseCase(CursoAdapter adapter){
        return new ListarCursoUseCase(adapter);
    }
}
