package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.acessos.CriarAcessoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.acessos.CriarAcessoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.acessos.ListarAcessosPorAlunoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Acesso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

import java.util.List;

@RestController
@RequestMapping("/acessos")
public class AcessoController {
    private final CriarAcessoUseCase criarAcessoUseCase;
    private final ListarAcessosPorAlunoUseCase listarAcessosPorAlunoUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    public AcessoController(CriarAcessoUseCase criarAcessoUseCase, ListarAcessosPorAlunoUseCase listarAcessosPorAlunoUseCase, BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase) {
        this.criarAcessoUseCase = criarAcessoUseCase;
        this.listarAcessosPorAlunoUseCase = listarAcessosPorAlunoUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
    }

    // Endpoint auxiliar para logar acesso simples por usuário (sem amarrar a curso);
    // mantém compatibilidade e simplifica o front quando quiser apenas registrar "entrou".
    @PostMapping("/usuario/{fkUsuario}")
    public ResponseEntity<Acesso> criarAcessoPorUsuario(
            @PathVariable Integer fkUsuario
    ){
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(fkUsuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(criarAcessoUseCase.execute(usuario, new CriarAcessoCommand(fkUsuario)));
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @PostMapping
    public ResponseEntity<Acesso> criarAcesso(
            @RequestBody CriarAcessoCommand request
    ){
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(request.fkUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(criarAcessoUseCase.execute(usuario, request));
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping("/{fkUsuario}")
    public ResponseEntity<List<Acesso>> listarAcessosPorUsuario(
            @PathVariable Integer fkUsuario
    ){
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(fkUsuario);
            return ResponseEntity.status(HttpStatus.OK).body(listarAcessosPorAlunoUseCase.execute(usuario));
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }
}
