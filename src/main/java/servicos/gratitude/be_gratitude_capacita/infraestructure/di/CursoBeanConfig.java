package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.MatriculaAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.UsuarioAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.producer.NotificacaoProducer;

@Configuration
public class CursoBeanConfig {

    @Bean
    public CriarCursoUseCase criarCursoUseCase(CursoAdapter cursoAdapter) {
        return new CriarCursoUseCase(cursoAdapter);
    }

    @Bean
    public AtualizarCursoUseCase atualizarCursoUseCase(CursoAdapter cursoAdapter) {
        return new AtualizarCursoUseCase(cursoAdapter);
    }

    @Bean
    public AtualizarOcultoUseCase atualizarOcultoUseCase(CursoAdapter cursoAdapter) {
        return new AtualizarOcultoUseCase(cursoAdapter);
    }

    @Bean
    public DeletarCursoUseCase deletarCursoUseCase(CursoAdapter cursoAdapter) {
        return new DeletarCursoUseCase(cursoAdapter);
    }

    @Bean
    public ListarCursoUseCase listarCursoUseCase(CursoAdapter cursoAdapter) {
        return new ListarCursoUseCase(cursoAdapter);
    }

    @Bean
    public ListarCursoPaginadoUseCase listarCursoPaginadoUseCase(CursoAdapter cursoAdapter) {
        return new ListarCursoPaginadoUseCase(cursoAdapter);
    }

    @Bean
    public EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase(CursoAdapter cursoAdapter) {
        return new EncontrarCursoPorIdUseCase(cursoAdapter);
    }

    @Bean
    public PublicarCursoUseCase publicarCursoUseCase(
            CursoAdapter cursoAdapter,
            MatriculaAdapter matriculaAdapter,
            UsuarioAdapter usuarioAdapter,
            NotificacaoProducer notificacaoProducer) {
        return new PublicarCursoUseCase(
            cursoAdapter, 
            matriculaAdapter, 
            usuarioAdapter, 
            notificacaoProducer
        );
    }
}