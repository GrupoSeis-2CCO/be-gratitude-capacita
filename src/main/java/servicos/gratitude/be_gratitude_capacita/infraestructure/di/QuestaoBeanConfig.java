package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.AvaliacaoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.QuestaoAdapter;

@Configuration
public class QuestaoBeanConfig {

    @Bean
    public AtualizarQuestaoUseCase atualizarQuestaoUseCase(QuestaoAdapter questaoAdapter, AvaliacaoAdapter avaliacaoAdapter){
        return new AtualizarQuestaoUseCase(questaoAdapter, avaliacaoAdapter);
    }

    @Bean
    public CriarQuestaoUseCase criarQuestaoUseCase(QuestaoAdapter questaoAdapter, AvaliacaoAdapter avaliacaoAdapter){
        return new CriarQuestaoUseCase(questaoAdapter, avaliacaoAdapter);
    }

    @Bean
    public DefinirRespostaUseCase definirRespostaUseCase(QuestaoAdapter questaoAdapter, AlternativaAdapter alternativaAdapter){
        return new DefinirRespostaUseCase(questaoAdapter, alternativaAdapter);
    }

    @Bean
    public DeletarQuestaoUseCase deletarQuestaoUseCase(QuestaoAdapter questaoAdapter){
        return new DeletarQuestaoUseCase(questaoAdapter);
    }

    @Bean
    public ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase(QuestaoAdapter questaoAdapter, AvaliacaoAdapter avaliacaoAdapter){
        return new ListarQuestoesPorAvaliacaoUseCase(questaoAdapter, avaliacaoAdapter);
    }
}
