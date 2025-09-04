package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.AtualizarSenhaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.CriarUsuarioCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final PesquisarPorNomeDeUsuarioUseCase pesquisarPorNomeDeUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final AtualizarAcessoUsuarioUseCase atualizarAcessoUsuarioUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase;

    public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase, ListarUsuariosUseCase listarUsuariosUseCase, PesquisarPorNomeDeUsuarioUseCase pesquisarPorNomeDeUsuarioUseCase, DeletarUsuarioUseCase deletarUsuarioUseCase, AtualizarAcessoUsuarioUseCase atualizarAcessoUsuarioUseCase, BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase, AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.pesquisarPorNomeDeUsuarioUseCase = pesquisarPorNomeDeUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
        this.atualizarAcessoUsuarioUseCase = atualizarAcessoUsuarioUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.atualizarSenhaUsuarioUseCase = atualizarSenhaUsuarioUseCase;
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

    @GetMapping("/pesquisa-por-nome")
    public ResponseEntity<List<Usuario>> pesquisarPorNome(
            @RequestParam String nome
    ){
        List<Usuario> responses = pesquisarPorNomeDeUsuarioUseCase.execute(nome);

        if (responses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity deletarUsuario(
            @PathVariable Integer idUsuario
    ){
        try {
            deletarUsuarioUseCase.execute(idUsuario);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @PutMapping("/acesso/{idUsuario}")
    public ResponseEntity<Usuario> atualizarUltimoAcesso(
            @PathVariable Integer idUsuario
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarAcessoUsuarioUseCase.execute(idUsuario));
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> encontrarUsuario(
            @PathVariable Integer idUsuario
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(buscarUsuarioPorIdUseCase.execute(idUsuario));
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @PutMapping("/senha/{idUsuario}")
    public ResponseEntity<Usuario> atualizarSenha(
            @RequestBody AtualizarSenhaCommand request,
            @PathVariable Integer idUsuario
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarSenhaUsuarioUseCase.execute(request, idUsuario));
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }
}
