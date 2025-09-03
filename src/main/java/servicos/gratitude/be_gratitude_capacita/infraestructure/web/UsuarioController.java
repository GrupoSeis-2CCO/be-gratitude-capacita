package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.CriarUsuarioCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.CriarUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.ListarUsuariosUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.PesquisarPorNomeUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;

    public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase, ListarUsuariosUseCase listarUsuariosUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastarUsuario(
            @RequestBody CriarUsuarioCommand request
    ){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarUsuarioUseCase.execute(request));
        } catch (ConflitoException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> responses = listarUsuariosUseCase.execute();

        if (responses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
