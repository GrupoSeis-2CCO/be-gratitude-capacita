package servicos.gratitude.be_gratitude_capacita.infraestructure.di;

import org.springframework.context.annotation.Configuration;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.ApostilaAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.CursoAdapter;

@Configuration
public class ApostilaBeanConfig {
    public CriarApostilaUseCase criarApostilaUseCase (ApostilaAdapter apostilaAdapter, CursoAdapter cursoAdapter){
        return new CriarApostilaUseCase(apostilaAdapter, cursoAdapter);
    }

    public AtualizarDadosApostilaUseCase atualizarDadosApostilaUseCase(ApostilaAdapter apostilaAdapter){
        return new AtualizarDadosApostilaUseCase(apostilaAdapter);
    }

    public AtualizarOcultoApostilaUseCase atualizarOcultoApostilaUseCase(ApostilaAdapter apostilaAdapter){
        return new AtualizarOcultoApostilaUseCase(apostilaAdapter);
    }

    public DeletarApostilaUseCase deletarApostilaUseCase(ApostilaAdapter apostilaAdapter){
        return new DeletarApostilaUseCase(apostilaAdapter);
    }

    public ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase(ApostilaAdapter apostilaAdapter, CursoAdapter cursoAdapter){
        return new ListarApostilaPorCursoUseCase(apostilaAdapter, cursoAdapter);
    }
}
