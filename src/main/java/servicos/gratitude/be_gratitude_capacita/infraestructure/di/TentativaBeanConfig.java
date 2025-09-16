package servicos.gratitude.be_gratitude_capacita.infraestructure.di;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.CriarTentativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.EncontrarTentativaPorId;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.ListarTentativaPorMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.CriarNovaChaveCompostaTentativaUseCase;  // Adicione este import
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.TentativaAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.MatriculaAdapter;  // Adicione este import
@Configuration
public class TentativaBeanConfig {

    @Bean
    public CriarTentativaUseCase criarTentativaUseCase(TentativaAdapter tentativaAdapter){
        return new CriarTentativaUseCase(tentativaAdapter);
    }

    @Bean
    public EncontrarTentativaPorId encontrarTentativaPorId(TentativaAdapter tentativaAdapter){
        return new EncontrarTentativaPorId(tentativaAdapter);
    }

    @Bean
    public ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase(TentativaAdapter tentativaAdapter){
        return new ListarTentativaPorMatriculaUseCase(tentativaAdapter);
    }
    @Bean
    public CriarNovaChaveCompostaTentativaUseCase criarNovaChaveCompostaTentativaUseCase(MatriculaAdapter matriculaAdapter, TentativaAdapter tentativaAdapter){
        return new CriarNovaChaveCompostaTentativaUseCase(matriculaAdapter, tentativaAdapter);
    }
}
