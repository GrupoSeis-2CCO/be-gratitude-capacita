package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.AvaliacaoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;

@Configuration
public class AvaliacaoBeanConfig {

    @Bean
    public CriarAvaliacaoUseCase criarAvaliacaoUseCase(AvaliacaoAdapter avaliacaoAdapter, CursoAdapter cursoAdapter){
        return new CriarAvaliacaoUseCase(avaliacaoAdapter, cursoAdapter);
    }

    @Bean
    public AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase(AvaliacaoAdapter avaliacaoAdapter){
        return new AtualizarAcertosMinimosAvaliacaoUseCase(avaliacaoAdapter);
    }
    @Bean
    public EncontrarAvaliacaoPorIdUseCase encontrarAvaliacaoPorIdUseCase(AvaliacaoAdapter avaliacaoAdapter){
        return new EncontrarAvaliacaoPorIdUseCase(avaliacaoAdapter);
    }

    @Bean
    public servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase(AvaliacaoAdapter avaliacaoAdapter, servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter cursoAdapter){
        return new servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase(avaliacaoAdapter, cursoAdapter);
    }
}
