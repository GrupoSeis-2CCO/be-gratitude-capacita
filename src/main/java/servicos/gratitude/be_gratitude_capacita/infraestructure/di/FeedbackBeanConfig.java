package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.CriarFeedbackUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.ListarFeedbacksPorCurso;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.FeedbackAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.UsuarioAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.MatriculaAdapter;

@Configuration
public class FeedbackBeanConfig {

    @Bean
    public CriarFeedbackUseCase criarFeedbackUseCase(FeedbackAdapter feedbackAdapter, UsuarioAdapter usuarioAdapter, MatriculaAdapter matriculaAdapter){
        return new CriarFeedbackUseCase(feedbackAdapter, usuarioAdapter, matriculaAdapter);
    }

    @Bean
    public ListarFeedbacksPorCurso listarFeedbacksPorCurso(FeedbackAdapter feedbackAdapter){
        return new ListarFeedbacksPorCurso(feedbackAdapter);
    }
}
