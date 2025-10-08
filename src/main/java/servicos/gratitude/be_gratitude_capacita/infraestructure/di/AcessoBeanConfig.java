package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.acessos.CriarAcessoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.acessos.ListarAcessosPorAlunoUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.AcessoAdapter;

@Configuration
public class AcessoBeanConfig {

    @Bean
    public CriarAcessoUseCase criarAcessoUseCase(AcessoAdapter acessoAdapter){
        return new CriarAcessoUseCase(acessoAdapter);
    }

    @Bean
    public ListarAcessosPorAlunoUseCase listarAcessosPorAlunoUseCase(AcessoAdapter acessoAdapter){
        return new ListarAcessosPorAlunoUseCase(acessoAdapter);
    }
}
