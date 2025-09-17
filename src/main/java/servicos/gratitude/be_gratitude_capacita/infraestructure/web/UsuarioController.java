
package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioLoginDto;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioTokenDto;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.AtualizarSenhaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.CriarUsuarioCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.UsuarioMapper;

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
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    public UsuarioController(
            CriarUsuarioUseCase criarUsuarioUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            PesquisarPorNomeDeUsuarioUseCase pesquisarPorNomeDeUsuarioUseCase,
            DeletarUsuarioUseCase deletarUsuarioUseCase,
            AtualizarAcessoUsuarioUseCase atualizarAcessoUsuarioUseCase,
            BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
            AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase,
            AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.pesquisarPorNomeDeUsuarioUseCase = pesquisarPorNomeDeUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
        this.atualizarAcessoUsuarioUseCase = atualizarAcessoUsuarioUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.atualizarSenhaUsuarioUseCase = atualizarSenhaUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticação de Usuário", description = "Autentica um usuário e retorna um token de acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioTokenDto.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    public ResponseEntity<UsuarioTokenDto> login(
            @Parameter(description = "Credenciais do usuário", required = true) @RequestBody UsuarioLoginDto usuarioLoginDTO) {
        UsuarioTokenDto usuarioTokenDTO = autenticarUsuarioUseCase.execute(usuarioLoginDTO);
        return ResponseEntity.status(200).body(usuarioTokenDTO);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginGetNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Use POST para login.");
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastarUsuario(
            @RequestBody CriarUsuarioCommand request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarUsuarioUseCase.execute(request));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> responses = listarUsuariosUseCase.execute();

        if (responses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/pesquisa-por-nome")
    public ResponseEntity<List<Usuario>> pesquisarPorNome(
            @RequestParam String nome) {
        List<Usuario> responses = pesquisarPorNomeDeUsuarioUseCase.execute(nome);

        if (responses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity deletarUsuario(
            @PathVariable Integer idUsuario) {
        try {
            deletarUsuarioUseCase.execute(idUsuario);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/acesso/{idUsuario}")
    public ResponseEntity<Usuario> atualizarUltimoAcesso(
            @PathVariable Integer idUsuario) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarAcessoUsuarioUseCase.execute(idUsuario));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> encontrarUsuario(
            @PathVariable Integer idUsuario) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(buscarUsuarioPorIdUseCase.execute(idUsuario));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/senha/{idUsuario}")
    public ResponseEntity<Usuario> atualizarSenha(
            @RequestBody AtualizarSenhaCommand request,
            @PathVariable Integer idUsuario) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarSenhaUsuarioUseCase.execute(request, idUsuario));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
