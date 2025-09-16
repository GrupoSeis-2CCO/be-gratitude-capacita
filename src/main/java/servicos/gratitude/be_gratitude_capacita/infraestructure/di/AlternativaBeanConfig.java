package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.AtualizarAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.CriarAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.DeletarAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.EncontrarAlternativaPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.ListarAlternativasPorQuestaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.CriarNovaChaveCompostaAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.AlternativaAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.QuestaoAdapter;

@Configuration
public class AlternativaBeanConfig {

    @Bean
    public AtualizarAlternativaUseCase atualizarAlternativaUseCase(AlternativaAdapter alternativaAdapter){
        return new AtualizarAlternativaUseCase(alternativaAdapter);
    }

    @Bean
    public CriarAlternativaUseCase criarAlternativaUseCase(AlternativaAdapter alternativaAdapter, QuestaoAdapter questaoAdapter){
        return new CriarAlternativaUseCase(alternativaAdapter, questaoAdapter);
    }

    @Bean
    public DeletarAlternativaUseCase deletarAlternativaUseCase(AlternativaAdapter alternativaAdapter){
        return new DeletarAlternativaUseCase(alternativaAdapter);
    }

    @Bean
    public ListarAlternativasPorQuestaoUseCase listarAlternativasPorQuestaoUseCase(AlternativaAdapter alternativaAdapter){
        return new ListarAlternativasPorQuestaoUseCase(alternativaAdapter);
    }

    @Bean
    public CriarNovaChaveCompostaAlternativaUseCase criarNovaChaveCompostaAlternativaUseCase(AlternativaAdapter alternativaAdapter, QuestaoAdapter questaoAdapter){
        return new CriarNovaChaveCompostaAlternativaUseCase(alternativaAdapter, questaoAdapter);
    }

    @Bean
    public EncontrarAlternativaPorIdUseCase encontrarAlternativaPorIdUseCase(AlternativaAdapter alternativaAdapter){
        return new EncontrarAlternativaPorIdUseCase(alternativaAdapter);
    }
}