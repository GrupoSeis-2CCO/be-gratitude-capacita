package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.*;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaQuestaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaTentativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.CriarNovaChaveCompostaRespostaDoUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.AvaliacaoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.QuestaoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.AlternativaAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.MatriculaAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.UsuarioAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.TentativaAdapter;

@Configuration
public class QuestaoBeanConfig {

    @Bean
    public AtualizarQuestaoUseCase atualizarQuestaoUseCase(QuestaoAdapter questaoAdapter,
            AvaliacaoAdapter avaliacaoAdapter) {
        return new AtualizarQuestaoUseCase(questaoAdapter, avaliacaoAdapter);
    }

    @Bean
    public CriarQuestaoUseCase criarQuestaoUseCase(QuestaoAdapter questaoAdapter, AvaliacaoAdapter avaliacaoAdapter) {
        return new CriarQuestaoUseCase(questaoAdapter, avaliacaoAdapter);
    }

    @Bean
    public DefinirRespostaUseCase definirRespostaUseCase(QuestaoAdapter questaoAdapter,
            AlternativaAdapter alternativaAdapter) {
        return new DefinirRespostaUseCase(questaoAdapter, alternativaAdapter);
    }

    @Bean
    public DeletarQuestaoUseCase deletarQuestaoUseCase(QuestaoAdapter questaoAdapter) {
        return new DeletarQuestaoUseCase(questaoAdapter);
    }

    @Bean
    public ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase(QuestaoAdapter questaoAdapter,
            AvaliacaoAdapter avaliacaoAdapter) {
        return new ListarQuestoesPorAvaliacaoUseCase(questaoAdapter, avaliacaoAdapter);
    }

    @Bean
    public MontarChaveCompostaQuestaoUseCase montarChaveCompostaQuestaoUseCase(QuestaoAdapter questaoAdapter,
            AvaliacaoAdapter avaliacaoAdapter) {
        return new MontarChaveCompostaQuestaoUseCase(questaoAdapter, avaliacaoAdapter);
    }

    @Bean
    public EncontrarQuestaoPorIdUseCase encontrarQuestaoPorIdUseCase(QuestaoAdapter questaoAdapter) {
        return new EncontrarQuestaoPorIdUseCase(questaoAdapter);
    }

    @Bean
    public MontarChaveCompostaAlternativaUseCase montarChaveCompostaAlternativaUseCase(QuestaoAdapter questaoAdapter) {
        return new MontarChaveCompostaAlternativaUseCase(questaoAdapter);
    }

    @Bean
    public MontarChaveCompostaTentativaUseCase montarChaveCompostaTentativaUseCase(MatriculaAdapter matriculaAdapter) {
        return new MontarChaveCompostaTentativaUseCase(matriculaAdapter);
    }

    @Bean
    public MontarChaveCompostaMatriculaUseCase montarChaveCompostaMatriculaUseCase(CursoAdapter cursoAdapter,
            UsuarioAdapter usuarioAdapter) {
        return new MontarChaveCompostaMatriculaUseCase(cursoAdapter, usuarioAdapter);
    }

    @Bean
    public CriarNovaChaveCompostaRespostaDoUsuarioUseCase criarNovaChaveCompostaRespostaDoUsuarioUseCase(
            TentativaAdapter tentativaAdapter, AlternativaAdapter alternativaAdapter) {
        return new CriarNovaChaveCompostaRespostaDoUsuarioUseCase(tentativaAdapter, alternativaAdapter);
    }
}