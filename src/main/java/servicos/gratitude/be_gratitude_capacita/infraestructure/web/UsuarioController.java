
package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioLoginDto;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioTokenDto;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.AtualizarSenhaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.CriarUsuarioCommand;
import servicos.gratitude.be_gratitude_capacita.core.dto.UsuarioCadastroDTO;
import jakarta.validation.Valid;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.UsuarioMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.TokenJwtAdapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private static final Logger LOG = LoggerFactory.getLogger(UsuarioController.class);

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final PesquisarPorNomeDeUsuarioUseCase pesquisarPorNomeDeUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final AtualizarAcessoUsuarioUseCase atualizarAcessoUsuarioUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.UsuarioRepository usuarioRepository;
    private final servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MatriculaRepository matriculaRepository;
    private final TokenJwtAdapter tokenJwtAdapter;

    public UsuarioController(
            CriarUsuarioUseCase criarUsuarioUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            PesquisarPorNomeDeUsuarioUseCase pesquisarPorNomeDeUsuarioUseCase,
            DeletarUsuarioUseCase deletarUsuarioUseCase,
            AtualizarAcessoUsuarioUseCase atualizarAcessoUsuarioUseCase,
            BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
            AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase,
            AutenticarUsuarioUseCase autenticarUsuarioUseCase,
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.UsuarioRepository usuarioRepository,
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MatriculaRepository matriculaRepository,
            TokenJwtAdapter tokenJwtAdapter) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.pesquisarPorNomeDeUsuarioUseCase = pesquisarPorNomeDeUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
        this.atualizarAcessoUsuarioUseCase = atualizarAcessoUsuarioUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.atualizarSenhaUsuarioUseCase = atualizarSenhaUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.usuarioRepository = usuarioRepository;
        this.matriculaRepository = matriculaRepository;
        this.tokenJwtAdapter = tokenJwtAdapter;
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
    public ResponseEntity<Usuario> cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO dto) {
        try {
            // Map DTO -> Command (mantém lógica existente)
                CriarUsuarioCommand cmd = new CriarUsuarioCommand(
                    dto.nome(),
                    dto.cpf(),
                    dto.email(),
                    dto.idCargo()
                );
            Usuario created = criarUsuarioUseCase.execute(cmd);
            // Persistir
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity entity = UsuarioMapper.toEntity(created);
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity saved = usuarioRepository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDomain(saved));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
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

    // Novo: Atualizar dados de perfil (nome, email, telefone)
    @PatchMapping("/{idUsuario}")
    public ResponseEntity<?> atualizarPerfil(
            @PathVariable Integer idUsuario,
            @RequestBody Map<String, Object> body) {
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(idUsuario);
            if (usuario == null) throw new NaoEncontradoException("Usuário não encontrado");

            final String emailAntes = usuario.getEmail();
            LOG.debug("[Perfil] Atualizando usuário {} emailAntes={} bodyKeys={}", idUsuario, emailAntes, body.keySet());

            if (body.containsKey("nome")) usuario.setNome((String) body.get("nome"));
            if (body.containsKey("email")) usuario.setEmail((String) body.get("email"));
            if (body.containsKey("telefone")) usuario.setTelefone((String) body.get("telefone"));
            if (body.containsKey("departamento")) usuario.setDepartamento((String) body.get("departamento"));

            // salvar via gateway (reaproveitando use case de acesso pois ele persiste) -> criamos um salvar direto usando adapter via repository
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity saved = usuarioRepository.save(entity);

            Usuario usuarioAtualizado = UsuarioMapper.toDomain(saved);

            // Se o e-mail (subject do JWT) foi alterado, emitimos um novo token para evitar invalidar a sessão
            if (emailAntes != null && usuarioAtualizado.getEmail() != null && !emailAntes.equalsIgnoreCase(usuarioAtualizado.getEmail())) {
                LOG.info("[Perfil] Email alterado de {} para {}. Emitindo novo token.", emailAntes, usuarioAtualizado.getEmail());
                String novoToken = tokenJwtAdapter.gerarToken(usuarioAtualizado);
                // Retorna no corpo tanto o usuário quanto o novo token
                return ResponseEntity.ok()
                        .body(new java.util.LinkedHashMap<String, Object>() {{
                            put("usuario", usuarioAtualizado);
                            put("token", novoToken);
                        }});
            }

            return ResponseEntity.ok(usuarioAtualizado);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("[Perfil] Falha ao atualizar perfil do usuário {}: {}", idUsuario, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    // Novo: Atualizar senha exigindo senha atual
    public static record AtualizarSenhaSeguraRequest(String senhaAtual, String novaSenha) {}

    @PutMapping("/{idUsuario}/senha-segura")
    public ResponseEntity<?> atualizarSenhaSegura(
            @PathVariable Integer idUsuario,
            @RequestBody AtualizarSenhaSeguraRequest req) {
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(idUsuario);
            if (usuario == null) throw new NaoEncontradoException("Usuário não encontrado");

            String hash = usuario.getSenha();
            boolean ok;
            if (hash != null && hash.startsWith("$2")) {
                ok = BCrypt.checkpw(req.senhaAtual(), hash);
            } else {
                ok = hash != null && hash.equals(req.senhaAtual());
            }
            if (!ok) {
                LOG.warn("[Senha] Senha atual incorreta para usuário {}", idUsuario);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha atual incorreta");
            }

            // define nova senha usando use case existente
            AtualizarSenhaCommand cmd = new AtualizarSenhaCommand(req.novaSenha());
            Usuario atualizado = atualizarSenhaUsuarioUseCase.execute(cmd, idUsuario);
            // persistir
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity entity = UsuarioMapper.toEntity(atualizado);
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity saved = usuarioRepository.save(entity);
            LOG.info("[Senha] Senha atualizada com sucesso para usuário {}", idUsuario);
            return ResponseEntity.ok(UsuarioMapper.toDomain(saved));
        } catch (NaoEncontradoException e) {
            LOG.warn("[Senha] Usuário {} não encontrado ao tentar atualizar senha", idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // Novo: Upload de avatar (foto de perfil) – recebe arquivo e salva localmente via FileUploadController; atualiza foto_url
    @PostMapping("/{idUsuario}/avatar")
    public ResponseEntity<?> uploadAvatar(
            @PathVariable Integer idUsuario,
            @RequestParam("file") MultipartFile file) {
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(idUsuario);
            if (usuario == null) throw new NaoEncontradoException("Usuário não encontrado");

            // Reutiliza lógica de uploads locais: salva em uploads/ e retorna URL relativa
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) originalFilename = "avatar";
            String filename = originalFilename.replaceAll("^.*[\\\\/]", "");
            filename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFilename = System.currentTimeMillis() + "_" + filename;

            java.nio.file.Path uploadsDir = java.nio.file.Paths.get("uploads").toAbsolutePath().normalize();
            java.nio.file.Files.createDirectories(uploadsDir);
            java.nio.file.Path dest = uploadsDir.resolve(uniqueFilename).toAbsolutePath().normalize();
            if (dest.getParent() != null) java.nio.file.Files.createDirectories(dest.getParent());
            file.transferTo(dest.toFile());

            String url = "/uploads/" + uniqueFilename;
            usuario.setFotoUrl(url);
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity saved = usuarioRepository.save(entity);
            return ResponseEntity.ok(Map.of("fotoUrl", url));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    // Novo: Estatísticas do usuário (cursos concluídos, cursos faltando, horas de estudo, última atividade)
    @GetMapping("/{idUsuario}/estatisticas")
    public ResponseEntity<?> estatisticas(@PathVariable Integer idUsuario) {
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(idUsuario);
            if (usuario == null) throw new NaoEncontradoException("Usuário não encontrado");

            // Obtém matrículas do usuário
            servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuario).orElse(null);
            if (usuarioEntity == null) throw new NaoEncontradoException("Usuário não encontrado");
            java.util.List<servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity> matriculas = matriculaRepository.findAllByUsuario(usuarioEntity);

            long concluidos = matriculas.stream().filter(m -> Boolean.TRUE.equals(m.getCompleto())).count();
            long total = matriculas.size();
            long faltando = total - concluidos;

            // Horas de estudo: somatório das durações estimadas dos cursos concluídos (aproximação)
            int horas = matriculas.stream()
                    .filter(m -> m.getCurso() != null && m.getCurso().getDuracaoEstimada() != null)
                    .map(m -> m.getCurso().getDuracaoEstimada())
                    .reduce(0, Integer::sum);

            java.time.LocalDateTime ultimaAtividade = matriculas.stream()
                    .map(m -> m.getUltimoAcesso())
                    .filter(java.util.Objects::nonNull)
                    .max(java.time.LocalDateTime::compareTo)
                    .orElse(usuario.getUltimoAcesso());

            return ResponseEntity.ok(Map.of(
                    "cursosConcluidos", concluidos,
                    "cursosFaltantes", Math.max(faltando, 0),
                    "horasEstudo", horas,
                    "ultimaAtividade", ultimaAtividade
            ));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
