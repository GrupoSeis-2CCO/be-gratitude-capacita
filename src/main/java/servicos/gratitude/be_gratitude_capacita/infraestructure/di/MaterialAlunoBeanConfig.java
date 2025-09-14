package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.MaterialAlunoAdapter;

@Configuration
public class MaterialAlunoBeanConfig {

    @Bean
    public CriarMaterialAlunoUseCase criarMaterialAlunoUseCase(MaterialAlunoAdapter materialAlunoAdapter){
        return new CriarMaterialAlunoUseCase(materialAlunoAdapter);
    }

    @Bean
    public AtualizarUltimoAcessoMaterialUseCase atualizarUltimoAcessoMaterialUseCase(MaterialAlunoAdapter materialAlunoAdapter){
        return new AtualizarUltimoAcessoMaterialUseCase(materialAlunoAdapter);
    }

    @Bean
    public DeletarMaterialUseCase deletarMaterialUseCase(MaterialAlunoAdapter materialAlunoAdapter){
        return new DeletarMaterialUseCase(materialAlunoAdapter);
    }

    @Bean
    public FinalizarMaterialUseCase finalizarMaterialUseCase(MaterialAlunoAdapter materialAlunoAdapter){
        return new FinalizarMaterialUseCase(materialAlunoAdapter);
    }

    @Bean
    public ListarMaterialPorMatriculaUseCase materialPorMatriculaUseCase(MaterialAlunoAdapter materialAlunoAdapter){
        return new ListarMaterialPorMatriculaUseCase(materialAlunoAdapter);
    }
}
