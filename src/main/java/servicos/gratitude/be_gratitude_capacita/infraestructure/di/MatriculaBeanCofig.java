package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.MatriculaAdapter;

@Configuration
public class MatriculaBeanCofig {

    @Bean
    public CriarMatriculaUseCase criarMatriculaUseCase(MatriculaAdapter matriculaAdapter){
        return new CriarMatriculaUseCase(matriculaAdapter);
    }

    @Bean
    public AtualizarUltimoAcessoMatriculaUseCase atualizarUltimoAcessoMatriculaUseCase(MatriculaAdapter matriculaAdapter){
        return new AtualizarUltimoAcessoMatriculaUseCase(matriculaAdapter);
    }

    @Bean
    public CompletarMatriculaUseCase completarMatriculaUseCase(MatriculaAdapter matriculaAdapter){
        return new CompletarMatriculaUseCase(matriculaAdapter);
    }

    @Bean
    public DeletarMatriculaUseCase deletarMatriculaUseCase(MatriculaAdapter matriculaAdapter){
        return new DeletarMatriculaUseCase(matriculaAdapter);
    }

    @Bean
    public ListarMatriculaPorUsuarioUseCase listarMatriculaPorUsuarioUseCase(MatriculaAdapter matriculaAdapter){
        return new ListarMatriculaPorUsuarioUseCase(matriculaAdapter);
    }

    @Bean
    public ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase(MatriculaAdapter matriculaAdapter){
        return new ListarMatriculaPorCursoUseCase(matriculaAdapter);
    }
}
