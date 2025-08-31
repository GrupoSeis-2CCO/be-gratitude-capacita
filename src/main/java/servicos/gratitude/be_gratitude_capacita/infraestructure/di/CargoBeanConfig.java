package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.cargo.ListarCargosUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.CriarCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CargoAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;

@Configuration
public class CargoBeanConfig {

    @Bean
    public ListarCargosUseCase listarCargosUseCase(CargoAdapter adapter){
        return new ListarCargosUseCase(adapter);
    }
}
