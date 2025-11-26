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
import servicos.gratitude.be_gratitude_capacita.core.application.command.feedback.CriarFeedbackCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.EncontrarCursoPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.CriarFeedbackUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.feedback.ListarFeedbacksPorCurso;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Feedback;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do FeedbackController")
class FeedbackControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CriarFeedbackUseCase criarFeedbackUseCase;

    @Mock
    private ListarFeedbacksPorCurso listarFeedbacksPorCurso;

    @Mock
    private EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase;

    @Mock
    private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    @InjectMocks
    private FeedbackController feedbackController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== TESTES POST /feedbacks ====================

    @Test
    @DisplayName("POST /feedbacks - Deve criar feedback com sucesso")
    void CriarFeedback_Sucesso() throws Exception {
        // Arrange
        CriarFeedbackCommand command = new CriarFeedbackCommand(
                1,    // idCurso
                5,    // estrelas
                "Excelente curso!",
                1,    // fkUsuario
                false // anonimo
        );

        Curso curso = new Curso();
        curso.setIdCurso(1);
        curso.setTituloCurso("Java Básico");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNome("João Silva");

        Feedback feedbackCriado = new Feedback();
        feedbackCriado.setFkCurso(1);
        feedbackCriado.setCurso(curso);
        feedbackCriado.setFkUsuario(usuario);
        feedbackCriado.setEstrelas(5);
        feedbackCriado.setMotivo("Excelente curso!");
        feedbackCriado.setAnonimo(false);

        when(encontrarCursoPorIdUseCase.execute(1)).thenReturn(curso);
        when(criarFeedbackUseCase.execute(any(CriarFeedbackCommand.class), eq(curso))).thenReturn(feedbackCriado);

        // Act & Assert
        mockMvc.perform(post("/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estrelas").value(5))
                .andExpect(jsonPath("$.motivo").value("Excelente curso!"));

        verify(criarFeedbackUseCase, times(1)).execute(any(CriarFeedbackCommand.class), eq(curso));
    }

    @Test
    @DisplayName("POST /feedbacks - Deve criar feedback anônimo com sucesso")
    void CriarFeedbackAnonimo_Sucesso() throws Exception {
        // Arrange
        CriarFeedbackCommand command = new CriarFeedbackCommand(
                1,
                4,
                "Bom curso, mas poderia melhorar.",
                1,
                true // anônimo
        );

        Curso curso = new Curso();
        curso.setIdCurso(1);
        curso.setTituloCurso("Python Avançado");

        Feedback feedbackCriado = new Feedback();
        feedbackCriado.setFkCurso(1);
        feedbackCriado.setCurso(curso);
        feedbackCriado.setEstrelas(4);
        feedbackCriado.setMotivo("Bom curso, mas poderia melhorar.");
        feedbackCriado.setAnonimo(true);

        when(encontrarCursoPorIdUseCase.execute(1)).thenReturn(curso);
        when(criarFeedbackUseCase.execute(any(CriarFeedbackCommand.class), eq(curso))).thenReturn(feedbackCriado);

        // Act & Assert
        mockMvc.perform(post("/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.anonimo").value(true));

        verify(criarFeedbackUseCase, times(1)).execute(any(CriarFeedbackCommand.class), eq(curso));
    }

    @Test
    @DisplayName("POST /feedbacks - Deve retornar 404 quando curso não existe")
    void CriarFeedback_CursoNaoEncontrado_404() throws Exception {
        // Arrange
        CriarFeedbackCommand command = new CriarFeedbackCommand(
                999,
                5,
                "Feedback",
                1,
                false
        );

        when(encontrarCursoPorIdUseCase.execute(999))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(post("/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(encontrarCursoPorIdUseCase, times(1)).execute(999);
    }

    @Test
    @DisplayName("POST /feedbacks - Deve retornar 400 quando estrelas inválidas")
    void CriarFeedback_EstrelasInvalidas_400() throws Exception {
        // Arrange
        CriarFeedbackCommand command = new CriarFeedbackCommand(
                1,
                6, // estrelas inválidas (>5)
                "Feedback",
                1,
                false
        );

        Curso curso = new Curso();
        curso.setIdCurso(1);

        when(encontrarCursoPorIdUseCase.execute(1)).thenReturn(curso);
        when(criarFeedbackUseCase.execute(any(CriarFeedbackCommand.class), eq(curso)))
                .thenThrow(new ValorInvalidoException("Estrelas deve ser entre 1 e 5"));

        // Act & Assert
        mockMvc.perform(post("/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(criarFeedbackUseCase, times(1)).execute(any(CriarFeedbackCommand.class), eq(curso));
    }

    // ==================== TESTES GET /feedbacks/curso/{idCurso} ====================

    @Test
    @DisplayName("GET /feedbacks/curso/{idCurso} - Deve listar feedbacks por curso")
    void ListarFeedbacksPorCurso_Sucesso() throws Exception {
        // Arrange
        Integer idCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(idCurso);
        curso.setTituloCurso("Java Básico");

        Usuario usuario1 = new Usuario();
        usuario1.setIdUsuario(1);
        usuario1.setNome("João");

        Usuario usuario2 = new Usuario();
        usuario2.setIdUsuario(2);
        usuario2.setNome("Maria");

        Feedback feedback1 = new Feedback();
        feedback1.setFkCurso(idCurso);
        feedback1.setCurso(curso);
        feedback1.setFkUsuario(usuario1);
        feedback1.setEstrelas(5);
        feedback1.setMotivo("Ótimo!");
        feedback1.setAnonimo(false);

        Feedback feedback2 = new Feedback();
        feedback2.setFkCurso(idCurso);
        feedback2.setCurso(curso);
        feedback2.setFkUsuario(usuario2);
        feedback2.setEstrelas(4);
        feedback2.setMotivo("Muito bom!");
        feedback2.setAnonimo(false);

        List<Feedback> feedbacks = Arrays.asList(feedback1, feedback2);

        when(encontrarCursoPorIdUseCase.execute(idCurso)).thenReturn(curso);
        when(listarFeedbacksPorCurso.execute(curso)).thenReturn(feedbacks);
        when(buscarUsuarioPorIdUseCase.execute(1)).thenReturn(usuario1);
        when(buscarUsuarioPorIdUseCase.execute(2)).thenReturn(usuario2);

        // Act & Assert
        mockMvc.perform(get("/feedbacks/curso/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].estrelas").value(5))
                .andExpect(jsonPath("$[1].estrelas").value(4));

        verify(listarFeedbacksPorCurso, times(1)).execute(curso);
    }

    @Test
    @DisplayName("GET /feedbacks/curso/{idCurso} - Deve retornar 204 quando não há feedbacks")
    void ListarFeedbacksPorCurso_SemFeedbacks_204() throws Exception {
        // Arrange
        Integer idCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(idCurso);

        when(encontrarCursoPorIdUseCase.execute(idCurso)).thenReturn(curso);
        when(listarFeedbacksPorCurso.execute(curso)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/feedbacks/curso/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listarFeedbacksPorCurso, times(1)).execute(curso);
    }

    @Test
    @DisplayName("GET /feedbacks/curso/{idCurso} - Deve retornar 404 quando curso não existe")
    void ListarFeedbacksPorCurso_CursoNaoEncontrado_404() throws Exception {
        // Arrange
        Integer idCurso = 999;

        when(encontrarCursoPorIdUseCase.execute(idCurso))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/feedbacks/curso/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(encontrarCursoPorIdUseCase, times(1)).execute(idCurso);
    }

    // ==================== TESTES GET /feedbacks/curso/{idCurso}/paginated ====================

    @Test
    @DisplayName("GET /feedbacks/curso/{idCurso}/paginated - Deve retornar feedbacks paginados")
    void ListarFeedbacksPaginados_Sucesso() throws Exception {
        // Arrange
        Integer idCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(idCurso);
        curso.setTituloCurso("Java Básico");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNome("João");

        Feedback feedback1 = new Feedback();
        feedback1.setFkCurso(idCurso);
        feedback1.setCurso(curso);
        feedback1.setFkUsuario(usuario);
        feedback1.setEstrelas(5);
        feedback1.setMotivo("Ótimo curso!");
        feedback1.setAnonimo(false);

        List<Feedback> feedbacks = Arrays.asList(feedback1);

        when(encontrarCursoPorIdUseCase.execute(idCurso)).thenReturn(curso);
        when(listarFeedbacksPorCurso.execute(curso)).thenReturn(feedbacks);
        when(buscarUsuarioPorIdUseCase.execute(1)).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(get("/feedbacks/curso/{idCurso}/paginated", idCurso)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.page").value(0));

        verify(listarFeedbacksPorCurso, times(1)).execute(curso);
    }

    @Test
    @DisplayName("GET /feedbacks/curso/{idCurso}/paginated - Deve suportar offset e limit")
    void ListarFeedbacksPaginados_OffsetELimit_Sucesso() throws Exception {
        // Arrange
        Integer idCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(idCurso);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNome("João");

        // Criar 5 feedbacks
        List<Feedback> feedbacks = Arrays.asList(
                criarFeedback(idCurso, curso, usuario, 5, "Feedback 1"),
                criarFeedback(idCurso, curso, usuario, 4, "Feedback 2"),
                criarFeedback(idCurso, curso, usuario, 3, "Feedback 3"),
                criarFeedback(idCurso, curso, usuario, 5, "Feedback 4"),
                criarFeedback(idCurso, curso, usuario, 4, "Feedback 5")
        );

        when(encontrarCursoPorIdUseCase.execute(idCurso)).thenReturn(curso);
        when(listarFeedbacksPorCurso.execute(curso)).thenReturn(feedbacks);
        when(buscarUsuarioPorIdUseCase.execute(1)).thenReturn(usuario);

        // Act & Assert - pegar apenas 2 feedbacks a partir do offset 2
        mockMvc.perform(get("/feedbacks/curso/{idCurso}/paginated", idCurso)
                        .param("offset", "2")
                        .param("limit", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(5));

        verify(listarFeedbacksPorCurso, times(1)).execute(curso);
    }

    @Test
    @DisplayName("GET /feedbacks/curso/{idCurso}/paginated - Deve retornar 204 quando não há feedbacks")
    void ListarFeedbacksPaginados_SemFeedbacks_204() throws Exception {
        // Arrange
        Integer idCurso = 1;

        Curso curso = new Curso();
        curso.setIdCurso(idCurso);

        when(encontrarCursoPorIdUseCase.execute(idCurso)).thenReturn(curso);
        when(listarFeedbacksPorCurso.execute(curso)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/feedbacks/curso/{idCurso}/paginated", idCurso)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listarFeedbacksPorCurso, times(1)).execute(curso);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Feedback criarFeedback(Integer cursoId, Curso curso, Usuario usuario, Integer estrelas, String motivo) {
        Feedback feedback = new Feedback();
        feedback.setFkCurso(cursoId);
        feedback.setCurso(curso);
        feedback.setFkUsuario(usuario);
        feedback.setEstrelas(estrelas);
        feedback.setMotivo(motivo);
        feedback.setAnonimo(false);
        return feedback;
    }
}
