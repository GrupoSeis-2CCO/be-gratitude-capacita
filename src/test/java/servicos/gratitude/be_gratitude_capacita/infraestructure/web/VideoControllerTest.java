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
import servicos.gratitude.be_gratitude_capacita.core.application.command.video.AtualizarVideoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.video.CriarVideoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do VideoController")
class VideoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AtualizarDadosVideoUseCase atualizarDadosVideoUseCase;

    @Mock
    private AtualizarOcultoVideoUseCase atualizarOcultoVideoUseCase;

    @Mock
    private CriarVideoUseCase criarVideoUseCase;

    @Mock
    private DeletarVideoUseCase deletarVideoUseCase;

    @Mock
    private ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;

    @InjectMocks
    private VideoController videoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(videoController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== TESTES POST /videos ====================

    @Test
    @DisplayName("POST /videos - Deve criar vídeo com sucesso")
    void CriarVideo_Sucesso() throws Exception {
        // Arrange
        CriarVideoCommand command = new CriarVideoCommand(
                1, // fkCurso
                "Introdução ao Java",
                "Vídeo introdutório sobre Java",
                "https://youtube.com/watch?v=abc123"
        );

        Curso curso = new Curso();
        curso.setIdCurso(1);
        curso.setTituloCurso("Java Básico");

        Video videoCriado = new Video();
        videoCriado.setIdVideo(1);
        videoCriado.setNomeVideo("Introdução ao Java");
        videoCriado.setDescricaoVideo("Vídeo introdutório sobre Java");
        videoCriado.setUrlVideo("https://youtube.com/watch?v=abc123");
        videoCriado.setFkCurso(curso);
        videoCriado.setOrdemVideo(1);
        videoCriado.setVideoOculto(false);

        when(criarVideoUseCase.execute(any(CriarVideoCommand.class))).thenReturn(videoCriado);

        // Act & Assert
        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVideo").value(1))
                .andExpect(jsonPath("$.nomeVideo").value("Introdução ao Java"));

        verify(criarVideoUseCase, times(1)).execute(any(CriarVideoCommand.class));
    }

    @Test
    @DisplayName("POST /videos - Deve retornar 404 quando curso não existe")
    void CriarVideo_CursoNaoEncontrado_404() throws Exception {
        // Arrange
        CriarVideoCommand command = new CriarVideoCommand(
                999,
                "Vídeo Teste",
                "Descrição",
                "https://youtube.com/watch?v=test"
        );

        when(criarVideoUseCase.execute(any(CriarVideoCommand.class)))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(criarVideoUseCase, times(1)).execute(any(CriarVideoCommand.class));
    }

    @Test
    @DisplayName("POST /videos - Deve retornar 409 quando vídeo com mesmo nome já existe no curso")
    void CriarVideo_NomeDuplicado_409() throws Exception {
        // Arrange
        CriarVideoCommand command = new CriarVideoCommand(
                1,
                "Vídeo Existente",
                "Descrição",
                "https://youtube.com/watch?v=existing"
        );

        when(criarVideoUseCase.execute(any(CriarVideoCommand.class)))
                .thenThrow(new ConflitoException("Já existe um vídeo com este nome no curso"));

        // Act & Assert
        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict());

        verify(criarVideoUseCase, times(1)).execute(any(CriarVideoCommand.class));
    }

    // ==================== TESTES GET /videos/{fkCurso} ====================

    @Test
    @DisplayName("GET /videos/{fkCurso} - Deve listar vídeos por curso")
    void ListarVideosPorCurso_Sucesso() throws Exception {
        // Arrange
        Integer fkCurso = 1;

        Video video1 = new Video();
        video1.setIdVideo(1);
        video1.setNomeVideo("Aula 1");
        video1.setDescricaoVideo("Primeira aula");
        video1.setUrlVideo("https://youtube.com/watch?v=1");
        video1.setOrdemVideo(1);

        Video video2 = new Video();
        video2.setIdVideo(2);
        video2.setNomeVideo("Aula 2");
        video2.setDescricaoVideo("Segunda aula");
        video2.setUrlVideo("https://youtube.com/watch?v=2");
        video2.setOrdemVideo(2);

        List<Video> videos = Arrays.asList(video1, video2);

        when(listarVideoPorCursoUseCase.execute(fkCurso)).thenReturn(videos);

        // Act & Assert
        mockMvc.perform(get("/videos/{fkCurso}", fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeVideo").value("Aula 1"))
                .andExpect(jsonPath("$[1].nomeVideo").value("Aula 2"));

        verify(listarVideoPorCursoUseCase, times(1)).execute(fkCurso);
    }

    @Test
    @DisplayName("GET /videos/{fkCurso} - Deve retornar 204 quando não há vídeos no curso")
    void ListarVideosPorCurso_SemVideos_204() throws Exception {
        // Arrange
        Integer fkCurso = 1;

        when(listarVideoPorCursoUseCase.execute(fkCurso)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/videos/{fkCurso}", fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listarVideoPorCursoUseCase, times(1)).execute(fkCurso);
    }

    @Test
    @DisplayName("GET /videos/{fkCurso} - Deve retornar 404 quando curso não existe")
    void ListarVideosPorCurso_CursoNaoEncontrado_404() throws Exception {
        // Arrange
        Integer fkCurso = 999;

        when(listarVideoPorCursoUseCase.execute(fkCurso))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/videos/{fkCurso}", fkCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(listarVideoPorCursoUseCase, times(1)).execute(fkCurso);
    }

    // ==================== TESTES PUT /videos/atualizar-dados/{idVideo} ====================

    @Test
    @DisplayName("PUT /videos/atualizar-dados/{id} - Deve atualizar dados do vídeo")
    void AtualizarDadosVideo_Sucesso() throws Exception {
        // Arrange
        Integer idVideo = 1;
        AtualizarVideoCommand command = new AtualizarVideoCommand(
                "Aula Atualizada",
                "Descrição atualizada",
                "https://youtube.com/watch?v=updated",
                2
        );

        Video videoAtualizado = new Video();
        videoAtualizado.setIdVideo(idVideo);
        videoAtualizado.setNomeVideo("Aula Atualizada");
        videoAtualizado.setDescricaoVideo("Descrição atualizada");
        videoAtualizado.setUrlVideo("https://youtube.com/watch?v=updated");
        videoAtualizado.setOrdemVideo(2);

        when(atualizarDadosVideoUseCase.execute(any(AtualizarVideoCommand.class), eq(idVideo)))
                .thenReturn(videoAtualizado);

        // Act & Assert
        mockMvc.perform(put("/videos/atualizar-dados/{idVideo}", idVideo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVideo").value(idVideo))
                .andExpect(jsonPath("$.nomeVideo").value("Aula Atualizada"));

        verify(atualizarDadosVideoUseCase, times(1)).execute(any(AtualizarVideoCommand.class), eq(idVideo));
    }

    @Test
    @DisplayName("PUT /videos/atualizar-dados/{id} - Deve retornar 404 quando vídeo não existe")
    void AtualizarDadosVideo_NaoEncontrado_404() throws Exception {
        // Arrange
        Integer idVideo = 999;
        AtualizarVideoCommand command = new AtualizarVideoCommand(
                "Título",
                "Descrição",
                "https://youtube.com/watch?v=test",
                1
        );

        when(atualizarDadosVideoUseCase.execute(any(AtualizarVideoCommand.class), eq(idVideo)))
                .thenThrow(new NaoEncontradoException("Vídeo não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/videos/atualizar-dados/{idVideo}", idVideo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(atualizarDadosVideoUseCase, times(1)).execute(any(AtualizarVideoCommand.class), eq(idVideo));
    }

    // ==================== TESTES PUT /videos/atualizar-oculto/{idVideo} ====================

    @Test
    @DisplayName("PUT /videos/atualizar-oculto/{id} - Deve alternar visibilidade do vídeo")
    void AlternarVisibilidadeVideo_Sucesso() throws Exception {
        // Arrange
        Integer idVideo = 1;

        Video videoAtualizado = new Video();
        videoAtualizado.setIdVideo(idVideo);
        videoAtualizado.setNomeVideo("Aula 1");
        videoAtualizado.setVideoOculto(true);

        when(atualizarOcultoVideoUseCase.execute(idVideo)).thenReturn(videoAtualizado);

        // Act & Assert
        mockMvc.perform(put("/videos/atualizar-oculto/{idVideo}", idVideo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVideo").value(idVideo));

        verify(atualizarOcultoVideoUseCase, times(1)).execute(idVideo);
    }

    @Test
    @DisplayName("PUT /videos/atualizar-oculto/{id} - Deve retornar 404 quando vídeo não existe")
    void AlternarVisibilidadeVideo_NaoEncontrado_404() throws Exception {
        // Arrange
        Integer idVideo = 999;

        when(atualizarOcultoVideoUseCase.execute(idVideo))
                .thenThrow(new NaoEncontradoException("Vídeo não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/videos/atualizar-oculto/{idVideo}", idVideo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(atualizarOcultoVideoUseCase, times(1)).execute(idVideo);
    }

    // ==================== TESTES DELETE /videos/{idVideo} ====================

    @Test
    @DisplayName("DELETE /videos/{id} - Deve deletar vídeo com sucesso")
    void DeletarVideo_Sucesso() throws Exception {
        // Arrange
        Integer idVideo = 1;

        doNothing().when(deletarVideoUseCase).execute(idVideo);

        // Act & Assert
        mockMvc.perform(delete("/videos/{idVideo}", idVideo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(deletarVideoUseCase, times(1)).execute(idVideo);
    }

    @Test
    @DisplayName("DELETE /videos/{id} - Deve retornar 404 quando vídeo não existe")
    void DeletarVideo_NaoEncontrado_404() throws Exception {
        // Arrange
        Integer idVideo = 999;

        doThrow(new NaoEncontradoException("Vídeo não encontrado"))
                .when(deletarVideoUseCase).execute(idVideo);

        // Act & Assert
        mockMvc.perform(delete("/videos/{idVideo}", idVideo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deletarVideoUseCase, times(1)).execute(idVideo);
    }
}
