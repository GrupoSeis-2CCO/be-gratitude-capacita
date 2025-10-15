package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.relatorio.ObterEngajamentoDiarioPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RelatorioGateway;

@Configuration
public class RelatorioBeanConfig {
    @Bean
    public ObterEngajamentoDiarioPorCursoUseCase obterEngajamentoDiarioPorCursoUseCase(RelatorioGateway relatorioGateway) {
        return new ObterEngajamentoDiarioPorCursoUseCase(relatorioGateway);
    }
}
