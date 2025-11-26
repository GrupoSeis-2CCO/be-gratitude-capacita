package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioLoginDto;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao.UsuarioTokenDto;
import servicos.gratitude.be_gratitude_capacita.core.application.command.usuario.AtualizarSenhaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Cargo;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.dto.UsuarioCadastroDTO;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter.TokenJwtAdapter;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UsuarioController")
class UsuarioControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CriarUsuarioUseCase criarUsuarioUseCase;

    @Mock
    private ListarUsuariosUseCase listarUsuariosUseCase;

    @Mock
    private PesquisarPorNomeDeUsuarioUseCase pesquisarPorNomeDeUsuarioUseCase;

    @Mock
    private DeletarUsuarioUseCase deletarUsuarioUseCase;

    @Mock
    private AtualizarAcessoUsuarioUseCase atualizarAcessoUsuarioUseCase;

    @Mock
    private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    @Mock
    private AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase;

    @Mock
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private MaterialAlunoRepository materialAlunoRepository;

    @Mock
    private TentativaRepository tentativaRepository;

    @Mock
    private RespostaDoUsuarioRepository respostaDoUsuarioRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private TokenJwtAdapter tokenJwtAdapter;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== TESTES POST /usuarios/login ====================

    @Test
    @DisplayName("POST /usuarios/login - Deve autenticar usuário com sucesso")
    void AutenticarUsuario_Sucesso() throws Exception {
        // Arrange
        UsuarioLoginDto loginDto = new UsuarioLoginDto("usuario@teste.com", "senha123");

        UsuarioTokenDto tokenDto = new UsuarioTokenDto();
        tokenDto.setToken("jwt-token-valido");
        tokenDto.setNome("João Silva");
        tokenDto.setEmail("usuario@teste.com");

        when(autenticarUsuarioUseCase.execute(any(UsuarioLoginDto.class))).thenReturn(tokenDto);

        // Act & Assert
        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-valido"))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("usuario@teste.com"));

        verify(autenticarUsuarioUseCase, times(1)).execute(any(UsuarioLoginDto.class));
    }

    @Test
    @DisplayName("GET /usuarios/login - Deve retornar 405 Method Not Allowed")
    void Login_MetodoGet_405() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    // ==================== TESTES POST /usuarios ====================

    @Test
    @DisplayName("POST /usuarios - Deve cadastrar usuário com sucesso")
    void CadastrarUsuario_Sucesso() throws Exception {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(
                "Maria Santos",
                "maria@teste.com",
                "12345678901",
                1
        );

        Cargo cargo = new Cargo();
        cargo.setIdCargo(1);
        cargo.setNomeCargo("Funcionário");

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setIdUsuario(1);
        usuarioCriado.setNome("Maria Santos");
        usuarioCriado.setEmail("maria@teste.com");
        usuarioCriado.setCpf("12345678901");
        usuarioCriado.setFkCargo(cargo);
        usuarioCriado.setDataEntrada(LocalDateTime.now());

        when(criarUsuarioUseCase.execute(any())).thenReturn(usuarioCriado);
        when(usuarioRepository.save(any())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(criarUsuarioUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("POST /usuarios - Deve retornar 409 quando email já existe")
    void CadastrarUsuario_EmailDuplicado_409() throws Exception {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(
                "Maria Santos",
                "maria@teste.com",
                "12345678901",
                1
        );

        when(criarUsuarioUseCase.execute(any()))
                .thenThrow(new ConflitoException("Já existe um usuário com o email informado"));

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

        verify(criarUsuarioUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("POST /usuarios - Deve retornar 409 quando CPF já existe")
    void CadastrarUsuario_CpfDuplicado_409() throws Exception {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(
                "Pedro Silva",
                "pedro@teste.com",
                "98765432101",
                1
        );

        when(criarUsuarioUseCase.execute(any()))
                .thenThrow(new ConflitoException("Já existe um usuário com o CPF informado"));

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

        verify(criarUsuarioUseCase, times(1)).execute(any());
    }

    // ==================== TESTES GET /usuarios ====================

    @Test
    @DisplayName("GET /usuarios - Deve listar todos os usuários")
    void ListarUsuarios_Sucesso() throws Exception {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setIdUsuario(1);
        usuario1.setNome("João");
        usuario1.setEmail("joao@teste.com");

        Usuario usuario2 = new Usuario();
        usuario2.setIdUsuario(2);
        usuario2.setNome("Maria");
        usuario2.setEmail("maria@teste.com");

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(listarUsuariosUseCase.execute()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[1].nome").value("Maria"));

        verify(listarUsuariosUseCase, times(1)).execute();
    }

    @Test
    @DisplayName("GET /usuarios - Deve retornar 204 quando não houver usuários")
    void ListarUsuarios_ListaVazia_204() throws Exception {
        // Arrange
        when(listarUsuariosUseCase.execute()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listarUsuariosUseCase, times(1)).execute();
    }

    // ==================== TESTES GET /usuarios/{id} ====================

    @Test
    @DisplayName("GET /usuarios/{id} - Deve retornar usuário por ID")
    void BuscarUsuarioPorId_Sucesso() throws Exception {
        // Arrange
        Integer idUsuario = 1;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@teste.com");
        usuario.setCpf("11122233344");

        when(buscarUsuarioPorIdUseCase.execute(idUsuario)).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(get("/usuarios/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario))
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@teste.com"));

        verify(buscarUsuarioPorIdUseCase, times(1)).execute(idUsuario);
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Deve retornar 404 quando usuário não encontrado")
    void BuscarUsuarioPorId_NaoEncontrado_404() throws Exception {
        // Arrange
        Integer idUsuario = 999;

        when(buscarUsuarioPorIdUseCase.execute(idUsuario))
                .thenThrow(new NaoEncontradoException("Usuário não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/usuarios/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(buscarUsuarioPorIdUseCase, times(1)).execute(idUsuario);
    }

    // ==================== TESTES GET /usuarios/pesquisa-por-nome ====================

    @Test
    @DisplayName("GET /usuarios/pesquisa-por-nome - Deve encontrar usuários por nome")
    void PesquisarPorNome_Sucesso() throws Exception {
        // Arrange
        String nome = "João";
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");

        when(pesquisarPorNomeDeUsuarioUseCase.execute(nome)).thenReturn(Arrays.asList(usuario));

        // Act & Assert
        mockMvc.perform(get("/usuarios/pesquisa-por-nome")
                        .param("nome", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

        verify(pesquisarPorNomeDeUsuarioUseCase, times(1)).execute(nome);
    }

    @Test
    @DisplayName("GET /usuarios/pesquisa-por-nome - Deve retornar 204 quando nenhum usuário encontrado")
    void PesquisarPorNome_NenhumEncontrado_204() throws Exception {
        // Arrange
        String nome = "NomeInexistente";

        when(pesquisarPorNomeDeUsuarioUseCase.execute(nome)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/usuarios/pesquisa-por-nome")
                        .param("nome", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pesquisarPorNomeDeUsuarioUseCase, times(1)).execute(nome);
    }

    // ==================== TESTES PUT /usuarios/senha/{id} ====================

    @Test
    @DisplayName("PUT /usuarios/senha/{id} - Deve atualizar senha com sucesso")
    void AtualizarSenha_Sucesso() throws Exception {
        // Arrange
        Integer idUsuario = 1;
        AtualizarSenhaCommand command = new AtualizarSenhaCommand("novaSenha123");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setIdUsuario(idUsuario);
        usuarioAtualizado.setNome("João");
        usuarioAtualizado.setSenha("hashDaNovaSenha");

        when(atualizarSenhaUsuarioUseCase.execute(any(AtualizarSenhaCommand.class), eq(idUsuario)))
                .thenReturn(usuarioAtualizado);

        // Act & Assert
        mockMvc.perform(put("/usuarios/senha/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario));

        verify(atualizarSenhaUsuarioUseCase, times(1)).execute(any(AtualizarSenhaCommand.class), eq(idUsuario));
    }

    @Test
    @DisplayName("PUT /usuarios/senha/{id} - Deve retornar 404 quando usuário não existe")
    void AtualizarSenha_UsuarioNaoEncontrado_404() throws Exception {
        // Arrange
        Integer idUsuario = 999;
        AtualizarSenhaCommand command = new AtualizarSenhaCommand("novaSenha123");

        when(atualizarSenhaUsuarioUseCase.execute(any(AtualizarSenhaCommand.class), eq(idUsuario)))
                .thenThrow(new NaoEncontradoException("Usuário não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/usuarios/senha/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(atualizarSenhaUsuarioUseCase, times(1)).execute(any(AtualizarSenhaCommand.class), eq(idUsuario));
    }

    // ==================== TESTES PUT /usuarios/acesso/{id} ====================

    @Test
    @DisplayName("PUT /usuarios/acesso/{id} - Deve atualizar último acesso")
    void AtualizarUltimoAcesso_Sucesso() throws Exception {
        // Arrange
        Integer idUsuario = 1;
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setIdUsuario(idUsuario);
        usuarioAtualizado.setNome("João");
        usuarioAtualizado.setUltimoAcesso(LocalDateTime.now());

        when(atualizarAcessoUsuarioUseCase.execute(idUsuario)).thenReturn(usuarioAtualizado);

        // Act & Assert
        mockMvc.perform(put("/usuarios/acesso/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario));

        verify(atualizarAcessoUsuarioUseCase, times(1)).execute(idUsuario);
    }

    @Test
    @DisplayName("PUT /usuarios/acesso/{id} - Deve retornar 404 para usuário inexistente")
    void AtualizarUltimoAcesso_UsuarioNaoEncontrado_404() throws Exception {
        // Arrange
        Integer idUsuario = 999;

        when(atualizarAcessoUsuarioUseCase.execute(idUsuario))
                .thenThrow(new NaoEncontradoException("Usuário não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/usuarios/acesso/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(atualizarAcessoUsuarioUseCase, times(1)).execute(idUsuario);
    }
}
