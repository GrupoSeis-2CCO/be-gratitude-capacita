package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.ApostilaAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;

@Configuration
public class ApostilaBeanConfig {

    @Bean
    public CriarApostilaUseCase criarApostilaUseCase(ApostilaAdapter apostilaAdapter, CursoAdapter cursoAdapter) {
        return new CriarApostilaUseCase(apostilaAdapter, cursoAdapter);
    }

    @Bean
    public AtualizarDadosApostilaUseCase atualizarDadosApostilaUseCase(ApostilaAdapter apostilaAdapter) {
        return new AtualizarDadosApostilaUseCase(apostilaAdapter);
    }

    @Bean
    public AtualizarOcultoApostilaUseCase atualizarOcultoApostilaUseCase(ApostilaAdapter apostilaAdapter) {
        return new AtualizarOcultoApostilaUseCase(apostilaAdapter);
    }

    @Bean
    public DeletarApostilaUseCase deletarApostilaUseCase(ApostilaAdapter apostilaAdapter) {
        return new DeletarApostilaUseCase(apostilaAdapter);
    }

    @Bean
    public ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase(ApostilaAdapter apostilaAdapter,
            CursoAdapter cursoAdapter) {
        return new ListarApostilaPorCursoUseCase(apostilaAdapter, cursoAdapter);
    }

    @Bean
    public ListarApostilaPorCursoPaginadoUseCase listarApostilaPorCursoPaginadoUseCase(ApostilaAdapter apostilaAdapter,
            CursoAdapter cursoAdapter) {
        return new ListarApostilaPorCursoPaginadoUseCase(apostilaAdapter, cursoAdapter);
    }
}