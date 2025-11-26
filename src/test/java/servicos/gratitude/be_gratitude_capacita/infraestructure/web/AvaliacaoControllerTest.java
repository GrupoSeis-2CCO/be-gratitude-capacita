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
import servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.DefinirAcertosMinimosCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.AtualizarAcertosMinimosAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.CriarAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RespostaDoUsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.request.AvaliacaoUpdateRequest;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.request.CreateAvaliacaoRequest;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AvaliacaoController")
class AvaliacaoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CriarAvaliacaoUseCase criarAvaliacaoUseCase;

    @Mock
    private AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase;

    @Mock
    private AvaliacaoGateway avaliacaoGateway;

    @Mock
    private QuestaoGateway questaoGateway;

    @Mock
    private AlternativaGateway alternativaGateway;

    @Mock
    private RespostaDoUsuarioGateway respostaDoUsuarioGateway;

    @Mock
    private TentativaGateway tentativaGateway;

    @InjectMocks
    private AvaliacaoController avaliacaoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(avaliacaoController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== TESTES GET /avaliacoes ====================

    @Test
    @DisplayName("GET /avaliacoes - Deve listar todas as avaliações")
    void ListarTodasAvaliacoes_Sucesso() throws Exception {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setIdCurso(1);
        curso1.setTituloCurso("Java");

        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setIdAvaliacao(1);
        avaliacao1.setFkCurso(curso1);
        avaliacao1.setAcertosMinimos(7);

        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setIdAvaliacao(2);
        avaliacao2.setFkCurso(curso1);
        avaliacao2.setAcertosMinimos(5);

        List<Avaliacao> avaliacoes = Arrays.asList(avaliacao1, avaliacao2);

        when(avaliacaoGateway.findAll()).thenReturn(avaliacoes);

        // Act & Assert
        mockMvc.perform(get("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(avaliacaoGateway, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /avaliacoes - Deve retornar lista vazia quando não há avaliações")
    void ListarAvaliacoes_ListaVazia() throws Exception {
        // Arrange
        when(avaliacaoGateway.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(avaliacaoGateway, times(1)).findAll();
    }

    // ==================== TESTES POST /avaliacoes ====================

    @Test
    @DisplayName("POST /avaliacoes - Deve criar avaliação com sucesso")
    void CriarAvaliacao_Sucesso() throws Exception {
        // Arrange
        CreateAvaliacaoRequest request = new CreateAvaliacaoRequest();
        request.fkCurso = 1;
        request.notaMinima = 7.0;
        request.questoes = new ArrayList<>();

        Curso curso = new Curso();
        curso.setIdCurso(1);
        curso.setTituloCurso("Java");

        Avaliacao avaliacaoCriada = new Avaliacao();
        avaliacaoCriada.setIdAvaliacao(1);
        avaliacaoCriada.setFkCurso(curso);
        avaliacaoCriada.setAcertosMinimos(7);

        when(criarAvaliacaoUseCase.execute(any())).thenReturn(avaliacaoCriada);

        // Act & Assert
        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAvaliacao").value(1))
                .andExpect(jsonPath("$.acertosMinimos").value(7));

        verify(criarAvaliacaoUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("POST /avaliacoes - Deve retornar 404 quando curso não existe")
    void CriarAvaliacao_CursoNaoEncontrado_404() throws Exception {
        // Arrange
        CreateAvaliacaoRequest request = new CreateAvaliacaoRequest();
        request.fkCurso = 999;
        request.notaMinima = 7.0;
        request.questoes = new ArrayList<>();

        when(criarAvaliacaoUseCase.execute(any()))
                .thenThrow(new NaoEncontradoException("Curso não encontrado"));

        // Act & Assert
        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(criarAvaliacaoUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("POST /avaliacoes - Deve retornar 400 quando nota mínima maior que quantidade de questões")
    void CriarAvaliacao_NotaMinimaExcedeQuestoes_400() throws Exception {
        // Arrange
        CreateAvaliacaoRequest request = new CreateAvaliacaoRequest();
        request.fkCurso = 1;
        request.notaMinima = 10.0;
        
        // Criando uma lista com apenas 5 questões mas nota mínima 10
        request.questoes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AvaliacaoUpdateRequest.QuestaoUpdateRequest q = new AvaliacaoUpdateRequest.QuestaoUpdateRequest();
            q.enunciado = "Questão " + i;
            q.alternativas = new ArrayList<>();
            request.questoes.add(q);
        }

        // Act & Assert - a validação ocorre antes do use case
        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ==================== TESTES GET /avaliacoes/{id} ====================

    @Test
    @DisplayName("GET /avaliacoes/{id} - Deve retornar avaliação por ID")
    void BuscarAvaliacaoPorId_Sucesso() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;

        Curso curso = new Curso();
        curso.setIdCurso(1);

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(idAvaliacao);
        avaliacao.setFkCurso(curso);
        avaliacao.setAcertosMinimos(7);

        QuestaoCompoundKey questaoKey = new QuestaoCompoundKey();
        questaoKey.setIdQuestao(1);
        questaoKey.setFkAvaliacao(idAvaliacao);

        Questao questao = new Questao();
        questao.setIdQuestaoComposto(questaoKey);
        questao.setEnunciado("Qual é a resposta correta?");
        questao.setNumeroQuestao(1);
        questao.setAvaliacao(avaliacao);

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(avaliacao);
        when(questaoGateway.findAllByAvaliacao(avaliacao)).thenReturn(Arrays.asList(questao));
        when(alternativaGateway.findAllByQuestao(questao)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/avaliacoes/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAvaliacao").value(idAvaliacao))
                .andExpect(jsonPath("$.acertosMinimos").value(7))
                .andExpect(jsonPath("$.questoes.length()").value(1));

        verify(avaliacaoGateway, times(1)).findById(idAvaliacao);
    }

    @Test
    @DisplayName("GET /avaliacoes/{id} - Deve retornar 404 quando avaliação não existe")
    void BuscarAvaliacaoPorId_NaoEncontrada_404() throws Exception {
        // Arrange
        Integer idAvaliacao = 999;

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/avaliacoes/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(avaliacaoGateway, times(1)).findById(idAvaliacao);
    }

    // ==================== TESTES GET /avaliacoes/curso/{idCurso} ====================

    @Test
    @DisplayName("GET /avaliacoes/curso/{idCurso} - Deve retornar avaliação por curso")
    void BuscarAvaliacaoPorCurso_Sucesso() throws Exception {
        // Arrange
        Long idCurso = 1L;

        Curso curso = new Curso();
        curso.setIdCurso(1);

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(1);
        avaliacao.setFkCurso(curso);
        avaliacao.setAcertosMinimos(7);

        when(avaliacaoGateway.findByFkCursoId(idCurso)).thenReturn(Optional.of(avaliacao));
        when(questaoGateway.findAllByAvaliacao(avaliacao)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/avaliacoes/curso/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAvaliacao").value(1))
                .andExpect(jsonPath("$.acertosMinimos").value(7));

        verify(avaliacaoGateway, times(1)).findByFkCursoId(idCurso);
    }

    @Test
    @DisplayName("GET /avaliacoes/curso/{idCurso} - Deve retornar 404 quando não há avaliação para o curso")
    void BuscarAvaliacaoPorCurso_NaoEncontrada_404() throws Exception {
        // Arrange
        Long idCurso = 999L;

        when(avaliacaoGateway.findByFkCursoId(idCurso)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/avaliacoes/curso/{idCurso}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(avaliacaoGateway, times(1)).findByFkCursoId(idCurso);
    }

    // ==================== TESTES PUT /avaliacoes/atualizar-acertos/{id} ====================

    @Test
    @DisplayName("PUT /avaliacoes/atualizar-acertos/{id} - Deve atualizar acertos mínimos")
    void AtualizarAcertosMinimos_Sucesso() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;
        DefinirAcertosMinimosCommand command = new DefinirAcertosMinimosCommand(8);

        Curso curso = new Curso();
        curso.setIdCurso(1);

        Avaliacao avaliacaoAtualizada = new Avaliacao();
        avaliacaoAtualizada.setIdAvaliacao(idAvaliacao);
        avaliacaoAtualizada.setFkCurso(curso);
        avaliacaoAtualizada.setAcertosMinimos(8);

        when(atualizarAcertosMinimosAvaliacaoUseCase.execute(eq(idAvaliacao), any(DefinirAcertosMinimosCommand.class)))
                .thenReturn(avaliacaoAtualizada);

        // Act & Assert
        mockMvc.perform(put("/avaliacoes/atualizar-acertos/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acertosMinimos").value(8));

        verify(atualizarAcertosMinimosAvaliacaoUseCase, times(1)).execute(eq(idAvaliacao), any(DefinirAcertosMinimosCommand.class));
    }

    @Test
    @DisplayName("PUT /avaliacoes/atualizar-acertos/{id} - Deve retornar 404 quando avaliação não existe")
    void AtualizarAcertosMinimos_NaoEncontrada_404() throws Exception {
        // Arrange
        Integer idAvaliacao = 999;
        DefinirAcertosMinimosCommand command = new DefinirAcertosMinimosCommand(8);

        when(atualizarAcertosMinimosAvaliacaoUseCase.execute(eq(idAvaliacao), any(DefinirAcertosMinimosCommand.class)))
                .thenThrow(new NaoEncontradoException("Avaliação não encontrada"));

        // Act & Assert
        mockMvc.perform(put("/avaliacoes/atualizar-acertos/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());

        verify(atualizarAcertosMinimosAvaliacaoUseCase, times(1)).execute(eq(idAvaliacao), any(DefinirAcertosMinimosCommand.class));
    }

    @Test
    @DisplayName("PUT /avaliacoes/atualizar-acertos/{id} - Deve retornar 400 quando valor inválido")
    void AtualizarAcertosMinimos_ValorInvalido_400() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;
        DefinirAcertosMinimosCommand command = new DefinirAcertosMinimosCommand(-1);

        when(atualizarAcertosMinimosAvaliacaoUseCase.execute(eq(idAvaliacao), any(DefinirAcertosMinimosCommand.class)))
                .thenThrow(new ValorInvalidoException("Acertos mínimos deve ser maior que zero"));

        // Act & Assert
        mockMvc.perform(put("/avaliacoes/atualizar-acertos/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(atualizarAcertosMinimosAvaliacaoUseCase, times(1)).execute(eq(idAvaliacao), any(DefinirAcertosMinimosCommand.class));
    }

    // ==================== TESTES GET /avaliacoes/{id}/respostas/existe ====================

    @Test
    @DisplayName("GET /avaliacoes/{id}/respostas/existe - Deve verificar se existem respostas")
    void VerificarExisteRespostas_ComRespostas() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(idAvaliacao);

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(avaliacao);
        when(respostaDoUsuarioGateway.countByExamId(idAvaliacao)).thenReturn(5L);

        // Act & Assert
        mockMvc.perform(get("/avaliacoes/{idAvaliacao}/respostas/existe", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasResponses").value(true))
                .andExpect(jsonPath("$.respostasCount").value(5));

        verify(respostaDoUsuarioGateway, times(1)).countByExamId(idAvaliacao);
    }

    @Test
    @DisplayName("GET /avaliacoes/{id}/respostas/existe - Deve retornar false quando não há respostas")
    void VerificarExisteRespostas_SemRespostas() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(idAvaliacao);

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(avaliacao);
        when(respostaDoUsuarioGateway.countByExamId(idAvaliacao)).thenReturn(0L);

        // Act & Assert
        mockMvc.perform(get("/avaliacoes/{idAvaliacao}/respostas/existe", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasResponses").value(false))
                .andExpect(jsonPath("$.respostasCount").value(0));

        verify(respostaDoUsuarioGateway, times(1)).countByExamId(idAvaliacao);
    }

    // ==================== TESTES DELETE /avaliacoes/{id} ====================

    @Test
    @DisplayName("DELETE /avaliacoes/{id} - Deve deletar avaliação sem respostas")
    void DeletarAvaliacao_SemRespostas_Sucesso() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(idAvaliacao);

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(avaliacao);
        when(respostaDoUsuarioGateway.countByExamId(idAvaliacao)).thenReturn(0L);
        when(questaoGateway.findAllByAvaliacao(avaliacao)).thenReturn(Collections.emptyList());
        doNothing().when(avaliacaoGateway).deleteById(idAvaliacao);

        // Act & Assert
        mockMvc.perform(delete("/avaliacoes/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(avaliacaoGateway, times(1)).deleteById(idAvaliacao);
    }

    @Test
    @DisplayName("DELETE /avaliacoes/{id} - Deve retornar 409 quando há respostas e não é forçado")
    void DeletarAvaliacao_ComRespostas_409() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(idAvaliacao);

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(avaliacao);
        when(respostaDoUsuarioGateway.countByExamId(idAvaliacao)).thenReturn(10L);
        when(questaoGateway.findAllByAvaliacao(avaliacao)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(delete("/avaliacoes/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.respostasCount").value(10));

        verify(avaliacaoGateway, never()).deleteById(idAvaliacao);
    }

    @Test
    @DisplayName("DELETE /avaliacoes/{id} - Deve retornar 404 quando avaliação não existe")
    void DeletarAvaliacao_NaoEncontrada_404() throws Exception {
        // Arrange
        Integer idAvaliacao = 999;

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/avaliacoes/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(avaliacaoGateway, times(1)).findById(idAvaliacao);
        verify(avaliacaoGateway, never()).deleteById(idAvaliacao);
    }

    // ==================== TESTES PATCH /avaliacoes/{id} ====================

    @Test
    @DisplayName("PATCH /avaliacoes/{id} - Deve atualizar parcialmente a avaliação")
    void AtualizarParcialAvaliacao_Sucesso() throws Exception {
        // Arrange
        Integer idAvaliacao = 1;

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(idAvaliacao);
        avaliacao.setAcertosMinimos(5);

        Map<String, Object> patch = new HashMap<>();
        patch.put("acertosMinimos", 8);

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(avaliacao);
        when(avaliacaoGateway.save(any())).thenReturn(avaliacao);

        // Act & Assert
        mockMvc.perform(patch("/avaliacoes/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk());

        verify(avaliacaoGateway, times(1)).save(any());
    }

    @Test
    @DisplayName("PATCH /avaliacoes/{id} - Deve retornar 404 quando avaliação não existe")
    void AtualizarParcialAvaliacao_NaoEncontrada_404() throws Exception {
        // Arrange
        Integer idAvaliacao = 999;

        Map<String, Object> patch = new HashMap<>();
        patch.put("acertosMinimos", 8);

        when(avaliacaoGateway.findById(idAvaliacao)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(patch("/avaliacoes/{idAvaliacao}", idAvaliacao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());

        verify(avaliacaoGateway, times(1)).findById(idAvaliacao);
    }
}
