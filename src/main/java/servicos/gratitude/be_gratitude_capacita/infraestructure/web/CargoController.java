package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.cargo.ListarCargosUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;

import java.util.List;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    private final ListarCargosUseCase listarCargosUseCase;

    public CargoController(ListarCargosUseCase listarCargosUseCase) {
        this.listarCargosUseCase = listarCargosUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Cargo>> listarCargos(){
        List<Cargo> cargos = listarCargosUseCase.execute();

        if (cargos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(cargos);
    }
}
