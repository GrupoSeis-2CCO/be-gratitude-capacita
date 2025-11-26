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
import servicos.gratitude.be_gratitude_capacita.S3.S3Service;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.AtualizarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.CriarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.ListarApostilaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso.*;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.ListarMatriculaPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.ListarQuestoesPorAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.ListarVideoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.infraestructure.service.CascadeDeletionService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CursoController")
class CursoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CriarCursoUseCase criarCursoUseCase;

    @Mock
    private ListarCursoUseCase listarCursoUseCase;

    @Mock
    private ListarCursoPaginadoUseCase listarCursoPaginadoUseCase;

    @Mock
    private AtualizarCursoUseCase atualizarCursoUseCase;

    @Mock
    private AtualizarOcultoUseCase atualizarOcultoUseCase;

    @Mock
    private DeletarCursoUseCase deletarCursoUseCase;

    @Mock
    private CascadeDeletionService cascadeDeletionService;

    @Mock
    private ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;

    @Mock
    private ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;

    @Mock
    private ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase;

    @Mock
    private ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;

    @Mock
    private ListarMatriculaPorCursoUseCase listarMatriculaPorCursoUseCase;

    @Mock
    private EncontrarCursoPorIdUseCase encontrarCursoPorIdUseCase;

    @Mock
    private PublicarCursoUseCase publicarCursoUseCase;

    @Mock
    private ReordenarCursosUseCase reordenarCursosUseCase;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private CursoController cursoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== TESTES POST /cursos ====================

    @Test
    @DisplayName("POST /cursos - Deve criar curso com sucesso e retornar 201")
    void CriarCurso_Sucesso() throws Exception {
        // Arrange
        CriarCursoCommand command = new CriarCursoCommand(
                "Java para Iniciantes",
                "Curso básico de Java",
                "http://imagem.com/java.png",
                40
        );

        Curso cursoCriado = new Curso();
        cursoCriado.setIdCurso(1);
        cursoCriado.setTituloCurso("Java para Iniciantes");
        cursoCriado.setDescricao("Curso básico de Java");
        cursoCriado.setImagem("http://imagem.com/java.png");
        cursoCriado.setDuracaoEstimada(40);
        cursoCriado.setOcultado(true);

        when(criarCursoUseCase.execute(any(CriarCursoCommand.class))).thenReturn(cursoCriado);

        // Act & Assert
        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.tituloCurso").value("Java para Iniciantes"))
                .andExpect(jsonPath("$.descricao").value("Curso básico de Java"));

        verify(criarCursoUseCase, times(1)).execute(any(CriarCursoCommand.class));
    }

    @Test
    @DisplayName("POST /cursos - Deve retornar 409 quando curso duplicado")
    void CriarCurso_TituloDuplicado_409() throws Exception {
        // Arrange
        CriarCursoCommand command = new CriarCursoCommand(
                "Java para Iniciantes",
                "Curso básico de Java",
                null,
                40
        );

        when(criarCursoUseCase.execute(any(CriarCursoCommand.class)))
                .thenThrow(new ConflitoException("Um curso com este titulo já foi cadastrado"));

        // Act & Assert
        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict());

        verify(criarCursoUseCase, times(1)).execute(any(CriarCursoCommand.class));
    }

    @Test
    @DisplayName("POST /cursos - Deve retornar 400 quando título inválido")
    void CriarCurso_TituloInvalido_400() throws Exception {
        // Arrange
        CriarCursoCommand command = new CriarCursoCommand(
                "",
                "Descrição",
                null,
                40
        );

        when(criarCursoUseCase.execute(any(CriarCursoCommand.class)))
                .thenThrow(new IllegalArgumentException("Título do curso é obrigatório."));

        // Act & Assert
        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(criarCursoUseCase, times(1)).execute(any(CriarCursoCommand.class));
    }

    // ==================== TESTES GET /cursos ====================

    @Test
    @DisplayName("GET /cursos - Deve listar cursos com sucesso e retornar 200")
    void ListarCursos_Sucesso() throws Exception {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setIdCurso(1);
        curso1.setTituloCurso("Java Básico");
        curso1.setDescricao("Curso de Java");
        curso1.setOcultado(false);

        Curso curso2 = new Curso();
        curso2.setIdCurso(2);
        curso2.setTituloCurso("Python Avançado");
        curso2.setDescricao("Curso de Python");
        curso2.setOcultado(false);

        List<Curso> cursos = Arrays.asList(curso1, curso2);

        when(listarCursoUseCase.execute()).thenReturn(cursos);
        when(listarApostilaPorCursoUseCase.execute(any())).thenReturn(Collections.emptyList());
        when(listarVideoPorCursoUseCase.execute(any())).thenReturn(Collections.emptyList());
        when(listarAvaliacaoPorCursoUseCase.execute(any())).thenReturn(Collections.emptyList());
        when(listarMatriculaPorCursoUseCase.execute(any())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/cursos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].tituloCurso").value("Java Básico"))
                .andExpect(jsonPath("$[1].tituloCurso").value("Python Avançado"));

        verify(listarCursoUseCase, times(1)).execute();
    }

    @Test
    @DisplayName("GET /cursos - Deve retornar 204 quando não houver cursos")
    void ListarCursos_ListaVazia_204() throws Exception {
        // Arrange
        when(listarCursoUseCase.execute()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/cursos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listarCursoUseCase, times(1)).execute();
    }

    // ==================== TESTES PUT /cursos/{id} ====================

    @Test
    @DisplayName("PUT /cursos/{id} - Deve atualizar curso com sucesso")
    void AtualizarCurso_Sucesso() throws Exception {
        // Arrange
        Integer idCurso = 1;
        AtualizarCursoCommand command = new AtualizarCursoCommand(
                "Java Avançado",
                "Curso avançado de Java",
                "http://imagem.com/java2.png",
                60
        );

        Curso cursoAtualizado = new Curso();
        cursoAtualizado.setIdCurso(idCurso);
        cursoAtualizado.setTituloCurso("Java Avançado");
        cursoAtualizado.setDescricao("Curso avançado de Java");
        cursoAtualizado.setImagem("http://imagem.com/java2.png");
        cursoAtualizado.setDuracaoEstimada(60);

        when(atualizarCursoUseCase.execute(any(AtualizarCursoCommand.class), eq(idCurso)))
                .thenReturn(cursoAtualizado);

        // Act & Assert
        mockMvc.perform(put("/cursos/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(idCurso))
                .andExpect(jsonPath("$.tituloCurso").value("Java Avançado"));

        verify(atualizarCursoUseCase, times(1)).execute(any(AtualizarCursoCommand.class), eq(idCurso));
    }

    @Test
    @DisplayName("PUT /cursos/{id} - Deve retornar 404 quando curso não encontrado")
    void AtualizarCurso_NaoEncontrado_404() throws Exception {
        // Arrange
        Integer idCurso = 999;
        AtualizarCursoCommand command = new AtualizarCursoCommand(
                "Novo Título",
                "Nova Descrição",
                null,
                30
        );

        when(atualizarCursoUseCase.execute(any(AtualizarCursoCommand.class), eq(idCurso)))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/cursos/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(atualizarCursoUseCase, times(1)).execute(any(AtualizarCursoCommand.class), eq(idCurso));
    }

    // ==================== TESTES DELETE /cursos/{id} ====================

    @Test
    @DisplayName("DELETE /cursos/{id} - Deve deletar curso com sucesso")
    void DeletarCurso_Sucesso() throws Exception {
        // Arrange
        Integer idCurso = 1;
        doNothing().when(cascadeDeletionService).deleteCursoComVinculos(idCurso);

        // Act & Assert
        mockMvc.perform(delete("/cursos/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cascadeDeletionService, times(1)).deleteCursoComVinculos(idCurso);
    }

    @Test
    @DisplayName("DELETE /cursos/{id} - Deve retornar 404 quando curso não existe")
    void DeletarCurso_NaoEncontrado_404() throws Exception {
        // Arrange
        Integer idCurso = 999;
        doThrow(new NaoEncontradoException("Curso não encontrado"))
                .when(cascadeDeletionService).deleteCursoComVinculos(idCurso);

        // Act & Assert
        mockMvc.perform(delete("/cursos/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cascadeDeletionService, times(1)).deleteCursoComVinculos(idCurso);
    }

    // ==================== TESTES PUT /cursos/atualizarOculto/{id} ====================

    @Test
    @DisplayName("PUT /cursos/atualizarOculto/{id} - Deve alternar visibilidade do curso")
    void AlternarVisibilidadeCurso_Sucesso() throws Exception {
        // Arrange
        Integer idCurso = 1;
        Curso cursoComOcultoAlterado = new Curso();
        cursoComOcultoAlterado.setIdCurso(idCurso);
        cursoComOcultoAlterado.setTituloCurso("Java Básico");
        cursoComOcultoAlterado.setOcultado(false);

        when(atualizarOcultoUseCase.execute(idCurso)).thenReturn(cursoComOcultoAlterado);

        // Act & Assert
        mockMvc.perform(put("/cursos/atualizarOculto/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(idCurso))
                .andExpect(jsonPath("$.ocultado").value(false));

        verify(atualizarOcultoUseCase, times(1)).execute(idCurso);
    }

    // ==================== TESTES GET /cursos/{id}/detalhes ====================

    @Test
    @DisplayName("GET /cursos/{id}/detalhes - Deve retornar detalhes do curso")
    void BuscarDetalhesCurso_Sucesso() throws Exception {
        // Arrange
        Integer idCurso = 1;
        Curso curso = new Curso();
        curso.setIdCurso(idCurso);
        curso.setTituloCurso("Java Completo");
        curso.setDescricao("Curso completo de Java");
        curso.setOcultado(false);
        curso.setDuracaoEstimada(100);

        when(encontrarCursoPorIdUseCase.execute(idCurso)).thenReturn(curso);
        when(listarApostilaPorCursoUseCase.execute(idCurso)).thenReturn(Collections.emptyList());
        when(listarVideoPorCursoUseCase.execute(idCurso)).thenReturn(Collections.emptyList());
        when(listarAvaliacaoPorCursoUseCase.execute(idCurso)).thenReturn(Collections.emptyList());
        when(listarMatriculaPorCursoUseCase.execute(any())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/cursos/{idCurso}/detalhes", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(idCurso))
                .andExpect(jsonPath("$.tituloCurso").value("Java Completo"))
                .andExpect(jsonPath("$.duracaoEstimada").value(100));

        verify(encontrarCursoPorIdUseCase, times(1)).execute(idCurso);
    }
}
