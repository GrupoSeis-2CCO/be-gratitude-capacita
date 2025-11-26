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
import servicos.gratitude.be_gratitude_capacita.core.application.command.matricula.CriarMatriculaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.ListarApostilaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.EncontrarCursoPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.ListarFeedbacksPorCurso;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno.ListarMaterialPorMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.*;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.ListarQuestoesPorAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.ListarRespostasDoUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.ListarTentativaPorMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.ListarVideoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

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
@DisplayName("Testes do MatriculaController")
class MatriculaControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CriarMatriculaUseCase criarMatriculaUseCase;

    @Mock
    private AtualizarUltimoAcessoMatriculaUseCase atualizarUltimoAcessoMatriculaUseCase;

    @Mock
    private CompletarMatriculaUseCase completarMatriculaUseCase;

    @Mock
    private DeletarMatriculaUseCase deletarMatriculaUseCase;

    @Mock
    private ListarMatriculaPorUsuarioUseCase listarMatriculaPorUsuarioUseCase;

    @Mock
    private ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase;

    @Mock
    private EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase;

    @Mock
    private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    @Mock
    private ListarMaterialPorMatriculaUseCase listarMaterialPorMatriculaUseCase;

    @Mock
    private ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase;

    @Mock
    private ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;

    @Mock
    private ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;

    @Mock
    private ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase;

    @Mock
    private ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;

    @Mock
    private ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase;

    @Mock
    private ListarFeedbacksPorCurso listarFeedbacksPorCurso;

    @InjectMocks
    private MatriculaController matriculaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(matriculaController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== TESTES POST /matriculas ====================

    @Test
    @DisplayName("POST /matriculas - Deve criar matrícula com sucesso")
    void CriarMatricula_Sucesso() throws Exception {
        // Arrange
        CriarMatriculaCommand command = new CriarMatriculaCommand(1, 1);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNome("João");

        Curso curso = new Curso();
        curso.setIdCurso(1);
        curso.setTituloCurso("Java Básico");

        MatriculaCompoundKey chave = new MatriculaCompoundKey();
        chave.setFkUsuario(1);
        chave.setFkCurso(1);

        Matricula matricula = new Matricula();
        matricula.setIdMatriculaComposto(chave);
        matricula.setUsuario(usuario);
        matricula.setCurso(curso);
        matricula.setDtInscricao(LocalDateTime.now());
        matricula.setCompleto(false);

        when(buscarUsuarioPorIdUseCase.execute(1)).thenReturn(usuario);
        when(encontrarCursoPorIdUseCase.execute(1)).thenReturn(curso);
        when(criarMatriculaUseCase.execute(any(), any(), any(), any())).thenReturn(matricula);

        // Act & Assert
        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated());

        verify(criarMatriculaUseCase, times(1)).execute(any(), any(), any(), any());
    }

    @Test
    @DisplayName("POST /matriculas - Deve retornar 404 quando usuário não existe")
    void CriarMatricula_UsuarioNaoEncontrado_404() throws Exception {
        // Arrange
        CriarMatriculaCommand command = new CriarMatriculaCommand(999, 1);

        when(buscarUsuarioPorIdUseCase.execute(999))
                .thenThrow(new NaoEncontradoException("Usuário não encontrado"));

        // Act & Assert
        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(buscarUsuarioPorIdUseCase, times(1)).execute(999);
    }

    @Test
    @DisplayName("POST /matriculas - Deve retornar 404 quando curso não existe")
    void CriarMatricula_CursoNaoEncontrado_404() throws Exception {
        // Arrange
        CriarMatriculaCommand command = new CriarMatriculaCommand(1, 999);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        when(buscarUsuarioPorIdUseCase.execute(1)).thenReturn(usuario);
        when(encontrarCursoPorIdUseCase.execute(999))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(encontrarCursoPorIdUseCase, times(1)).execute(999);
    }

    @Test
    @DisplayName("POST /matriculas - Deve retornar 409 quando matrícula já existe")
    void CriarMatricula_MatriculaDuplicada_409() throws Exception {
        // Arrange
        CriarMatriculaCommand command = new CriarMatriculaCommand(1, 1);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        Curso curso = new Curso();
        curso.setIdCurso(1);

        when(buscarUsuarioPorIdUseCase.execute(1)).thenReturn(usuario);
        when(encontrarCursoPorIdUseCase.execute(1)).thenReturn(curso);
        when(criarMatriculaUseCase.execute(any(), any(), any(), any()))
                .thenThrow(new ConflitoException("Matrícula já existe"));

        // Act & Assert
        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict());

        verify(criarMatriculaUseCase, times(1)).execute(any(), any(), any(), any());
    }

    // ==================== TESTES GET /matriculas/usuario/{fkUsuario} ====================

    @Test
    @DisplayName("GET /matriculas/usuario/{id} - Deve listar matrículas por usuário")
    void ListarMatriculasPorUsuario_Sucesso() throws Exception {
        // Arrange
        Integer fkUsuario = 1;

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(fkUsuario);
        usuario.setNome("João");

        Curso curso1 = new Curso();
        curso1.setIdCurso(1);
        curso1.setTituloCurso("Java");

        Curso curso2 = new Curso();
        curso2.setIdCurso(2);
        curso2.setTituloCurso("Python");

        Matricula matricula1 = new Matricula();
        matricula1.setUsuario(usuario);
        matricula1.setCurso(curso1);
        matricula1.setCompleto(false);

        Matricula matricula2 = new Matricula();
        matricula2.setUsuario(usuario);
        matricula2.setCurso(curso2);
        matricula2.setCompleto(true);

        List<Matricula> matriculas = Arrays.asList(matricula1, matricula2);

        when(buscarUsuarioPorIdUseCase.execute(fkUsuario)).thenReturn(usuario);
        when(listarMatriculaPorUsuarioUseCase.execute(usuario)).thenReturn(matriculas);

        // Act & Assert
        mockMvc.perform(get("/matriculas/usuario/{fkUsuario}", fkUsuario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(listarMatriculaPorUsuarioUseCase, times(1)).execute(usuario);
    }

    @Test
    @DisplayName("GET /matriculas/usuario/{id} - Deve retornar 404 quando usuário não existe")
    void ListarMatriculasPorUsuario_UsuarioNaoEncontrado_404() throws Exception {
        // Arrange
        Integer fkUsuario = 999;

        when(buscarUsuarioPorIdUseCase.execute(fkUsuario))
                .thenThrow(new NaoEncontradoException("Usuário não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/matriculas/usuario/{fkUsuario}", fkUsuario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(buscarUsuarioPorIdUseCase, times(1)).execute(fkUsuario);
    }

    // ==================== TESTES GET /matriculas/curso/{fkCurso} ====================

    @Test
    @DisplayName("GET /matriculas/curso/{id} - Deve listar matrículas por curso")
    void ListarMatriculasPorCurso_Sucesso() throws Exception {
        // Arrange
        Integer fkCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(fkCurso);
        curso.setTituloCurso("Java");

        Usuario usuario1 = new Usuario();
        usuario1.setIdUsuario(1);
        usuario1.setNome("João");

        Usuario usuario2 = new Usuario();
        usuario2.setIdUsuario(2);
        usuario2.setNome("Maria");

        Matricula matricula1 = new Matricula();
        matricula1.setUsuario(usuario1);
        matricula1.setCurso(curso);

        Matricula matricula2 = new Matricula();
        matricula2.setUsuario(usuario2);
        matricula2.setCurso(curso);

        List<Matricula> matriculas = Arrays.asList(matricula1, matricula2);

        when(encontrarCursoPorIdUseCase.execute(fkCurso)).thenReturn(curso);
        when(listarMatriculaPorCursoUseCase.execute(curso)).thenReturn(matriculas);

        // Act & Assert
        mockMvc.perform(get("/matriculas/curso/{fkCurso}", fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(listarMatriculaPorCursoUseCase, times(1)).execute(curso);
    }

    @Test
    @DisplayName("GET /matriculas/curso/{id} - Deve retornar 404 quando curso não existe")
    void ListarMatriculasPorCurso_CursoNaoEncontrado_404() throws Exception {
        // Arrange
        Integer fkCurso = 999;

        when(encontrarCursoPorIdUseCase.execute(fkCurso))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/matriculas/curso/{fkCurso}", fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(encontrarCursoPorIdUseCase, times(1)).execute(fkCurso);
    }

    // ==================== TESTES PUT /matriculas/completar/{fkUsuario}/{fkCurso} ====================

    @Test
    @DisplayName("PUT /matriculas/completar/{fkUsuario}/{fkCurso} - Deve completar matrícula")
    void CompletarMatricula_Sucesso() throws Exception {
        // Arrange
        Integer fkUsuario = 1;
        Integer fkCurso = 1;

        MatriculaCompoundKey chave = new MatriculaCompoundKey();
        chave.setFkUsuario(fkUsuario);
        chave.setFkCurso(fkCurso);

        Matricula matriculaCompleta = new Matricula();
        matriculaCompleta.setIdMatriculaComposto(chave);
        matriculaCompleta.setCompleto(true);
        matriculaCompleta.setDataFinalizacao(LocalDateTime.now());

        when(completarMatriculaUseCase.execute(any(MatriculaCompoundKey.class)))
                .thenReturn(matriculaCompleta);

        // Act & Assert
        mockMvc.perform(put("/matriculas/completar/{fkUsuario}/{fkCurso}", fkUsuario, fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(completarMatriculaUseCase, times(1)).execute(any(MatriculaCompoundKey.class));
    }

    @Test
    @DisplayName("PUT /matriculas/completar/{fkUsuario}/{fkCurso} - Deve retornar 404 quando matrícula não existe")
    void CompletarMatricula_NaoEncontrada_404() throws Exception {
        // Arrange
        Integer fkUsuario = 1;
        Integer fkCurso = 999;

        when(completarMatriculaUseCase.execute(any(MatriculaCompoundKey.class)))
                .thenThrow(new NaoEncontradoException("Matrícula não encontrada"));

        // Act & Assert
        mockMvc.perform(put("/matriculas/completar/{fkUsuario}/{fkCurso}", fkUsuario, fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(completarMatriculaUseCase, times(1)).execute(any(MatriculaCompoundKey.class));
    }

    // ==================== TESTES PUT /matriculas/atualizar-ultimo-acesso/{fkUsuario}/{fkCurso} ====================

    @Test
    @DisplayName("PUT /matriculas/atualizar-ultimo-acesso - Deve atualizar último acesso")
    void AtualizarUltimoAcessoMatricula_Sucesso() throws Exception {
        // Arrange
        Integer fkUsuario = 1;
        Integer fkCurso = 1;

        Matricula matriculaAtualizada = new Matricula();
        matriculaAtualizada.setUltimoAcesso(LocalDateTime.now());

        when(atualizarUltimoAcessoMatriculaUseCase.execute(any(MatriculaCompoundKey.class)))
                .thenReturn(matriculaAtualizada);

        // Act & Assert
        mockMvc.perform(put("/matriculas/atualizar-ultimo-acesso/{fkUsuario}/{fkCurso}", fkUsuario, fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(atualizarUltimoAcessoMatriculaUseCase, times(1)).execute(any(MatriculaCompoundKey.class));
    }

    // ==================== TESTES DELETE /matriculas/{fkUsuario}/{fkCurso} ====================

    @Test
    @DisplayName("DELETE /matriculas/{fkUsuario}/{fkCurso} - Deve deletar matrícula")
    void DeletarMatricula_Sucesso() throws Exception {
        // Arrange
        Integer fkUsuario = 1;
        Integer fkCurso = 1;

        doNothing().when(deletarMatriculaUseCase).execute(any(MatriculaCompoundKey.class));

        // Act & Assert
        mockMvc.perform(delete("/matriculas/{fkUsuario}/{fkCurso}", fkUsuario, fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(deletarMatriculaUseCase, times(1)).execute(any(MatriculaCompoundKey.class));
    }

    @Test
    @DisplayName("DELETE /matriculas/{fkUsuario}/{fkCurso} - Deve retornar 404 quando matrícula não existe")
    void DeletarMatricula_NaoEncontrada_404() throws Exception {
        // Arrange
        Integer fkUsuario = 1;
        Integer fkCurso = 999;

        doThrow(new NaoEncontradoException("Matrícula não encontrada"))
                .when(deletarMatriculaUseCase).execute(any(MatriculaCompoundKey.class));

        // Act & Assert
        mockMvc.perform(delete("/matriculas/{fkUsuario}/{fkCurso}", fkUsuario, fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deletarMatriculaUseCase, times(1)).execute(any(MatriculaCompoundKey.class));
    }

    // ==================== TESTES GET /matriculas/curso/{fkCurso}/participantes ====================

    @Test
    @DisplayName("GET /matriculas/curso/{id}/participantes - Deve listar participantes do curso")
    void ListarParticipantesCurso_Sucesso() throws Exception {
        // Arrange
        Integer fkCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(fkCurso);
        curso.setTituloCurso("Java");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNome("João Silva");

        Matricula matricula = new Matricula();
        matricula.setUsuario(usuario);
        matricula.setCurso(curso);
        matricula.setUltimoAcesso(LocalDateTime.now());

        when(encontrarCursoPorIdUseCase.execute(fkCurso)).thenReturn(curso);
        when(listarMatriculaPorCursoUseCase.execute(curso)).thenReturn(Arrays.asList(matricula));
        when(listarFeedbacksPorCurso.execute(curso)).thenReturn(Collections.emptyList());
        when(listarApostilaPorCursoUseCase.execute(fkCurso)).thenReturn(Collections.emptyList());
        when(listarVideoPorCursoUseCase.execute(fkCurso)).thenReturn(Collections.emptyList());
        when(listarAvaliacaoPorCursoUseCase.execute(fkCurso)).thenReturn(Collections.emptyList());
        when(listarMaterialPorMatriculaUseCase.execute(any())).thenReturn(Collections.emptyList());
        when(listarTentativaPorMatriculaUseCase.execute(any())).thenReturn(Collections.emptyList());
        when(listarRespostasDoUsuarioUseCase.execute(any())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/matriculas/curso/{fkCurso}/participantes", fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

        verify(listarMatriculaPorCursoUseCase, times(1)).execute(curso);
    }

    @Test
    @DisplayName("GET /matriculas/curso/{id}/participantes - Deve retornar 204 quando não há participantes")
    void ListarParticipantesCurso_SemParticipantes_204() throws Exception {
        // Arrange
        Integer fkCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(fkCurso);
        curso.setTituloCurso("Curso Vazio");

        when(encontrarCursoPorIdUseCase.execute(fkCurso)).thenReturn(curso);
        when(listarMatriculaPorCursoUseCase.execute(curso)).thenReturn(Collections.emptyList());
        when(listarFeedbacksPorCurso.execute(curso)).thenReturn(Collections.emptyList());
        when(listarApostilaPorCursoUseCase.execute(fkCurso)).thenReturn(Collections.emptyList());
        when(listarVideoPorCursoUseCase.execute(fkCurso)).thenReturn(Collections.emptyList());
        when(listarAvaliacaoPorCursoUseCase.execute(fkCurso)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/matriculas/curso/{fkCurso}/participantes", fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listarMatriculaPorCursoUseCase, times(1)).execute(curso);
    }
}
