package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;

@Configuration
public class CursoBeanConfig {

    @Bean
    public CriarCursoUseCase criarCursoUseCase(CursoAdapter cursoAdapter){
        return new CriarCursoUseCase(cursoAdapter);
    }

    @Bean
    public AtualizarCursoUseCase atualizarCursoUseCase(CursoAdapter cursoAdapter){
        return new AtualizarCursoUseCase(cursoAdapter);
    }

    @Bean
    public AtualizarOcultoUseCase atualizarOcultoUseCase(CursoAdapter cursoAdapter){
        return new AtualizarOcultoUseCase(cursoAdapter);
    }

    @Bean
    public DeletarCursoUseCase deletarCursoUseCase(CursoAdapter cursoAdapter){
        return new DeletarCursoUseCase(cursoAdapter);
    }

    @Bean
    public ListarCursoUseCase listarCursoUseCase(CursoAdapter cursoAdapter){
        return new ListarCursoUseCase(cursoAdapter);
    }
}
