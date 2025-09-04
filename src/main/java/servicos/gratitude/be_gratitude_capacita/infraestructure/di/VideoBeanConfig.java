package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.VideoAdapter;

@Configuration
public class VideoBeanConfig {

    @Bean
    public CriarVideoUseCase criarVideoUseCase(VideoAdapter videoAdapter, CursoAdapter cursoAdapter){
        return new CriarVideoUseCase(videoAdapter, cursoAdapter);
    }

    @Bean
    public AtualizarDadosVideoUseCase atualizarVideoUseCase(VideoAdapter videoAdapter){
        return new AtualizarDadosVideoUseCase(videoAdapter);
    }

    @Bean
    public AtualizarOcultoVideoUseCase atualizarOcultoVideoUseCase(VideoAdapter videoAdapter){
        return new AtualizarOcultoVideoUseCase(videoAdapter);
    }

    @Bean
    public DeletarVideoUseCase deletarVideoUseCase(VideoAdapter videoAdapter){
        return new DeletarVideoUseCase(videoAdapter);
    }

    @Bean
    public ListarVideoPorCursoUseCase listarVideoPorCursoUseCase(VideoAdapter videoAdapter, CursoAdapter cursoAdapter){
        return new ListarVideoPorCursoUseCase(videoAdapter, cursoAdapter);
    }
}
