package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.CriarAvaliacaoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.DefinirAcertosMinimosCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.AtualizarAcertosMinimosAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.CriarAvaliacaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AvaliacaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.QuestaoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.AlternativaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RespostaDoUsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.AvaliacaoCompletaResponse;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.request.AvaliacaoUpdateRequest;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.Alternativa;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    private final CriarAvaliacaoUseCase criarAvaliacaoUseCase;
    private final AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase;
    private final AvaliacaoGateway avaliacaoGateway;
    private final QuestaoGateway questaoGateway;
    private final AlternativaGateway alternativaGateway;
    private final RespostaDoUsuarioGateway respostaDoUsuarioGateway;

    public AvaliacaoController(
        CriarAvaliacaoUseCase criarAvaliacaoUseCase,
        AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase,
        AvaliacaoGateway avaliacaoGateway,
        QuestaoGateway questaoGateway,
        AlternativaGateway alternativaGateway,
        RespostaDoUsuarioGateway respostaDoUsuarioGateway
    ) {
        this.criarAvaliacaoUseCase = criarAvaliacaoUseCase;
        this.atualizarAcertosMinimosAvaliacaoUseCase = atualizarAcertosMinimosAvaliacaoUseCase;
        this.avaliacaoGateway = avaliacaoGateway;
        this.questaoGateway = questaoGateway;
        this.alternativaGateway = alternativaGateway;
        this.respostaDoUsuarioGateway = respostaDoUsuarioGateway;
    }
    @GetMapping
    public ResponseEntity<?> listarAvaliacoes() {
        try {
            return ResponseEntity.ok(avaliacaoGateway.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar avaliações", e);
        }
    }

    @PostMapping
    public ResponseEntity<Avaliacao> cadastrarAvaliacao(
            @RequestBody servicos.gratitude.be_gratitude_capacita.infraestructure.web.request.CreateAvaliacaoRequest request
    ){
        try {
            System.out.println("[AvaliacaoController] fkCurso recebido: " + request.fkCurso);
            // Primeiro cria a avaliação básica (nota mínima e vínculo ao curso)
            servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.CriarAvaliacaoCommand cmd =
                    new servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.CriarAvaliacaoCommand(request.fkCurso, request.notaMinima);
            var created = criarAvaliacaoUseCase.execute(cmd);

            // Se vierem questões no payload, reaproveita o endpoint de atualização para persistir questões/alternativas
            if (request.questoes != null && !request.questoes.isEmpty()) {
                AvaliacaoUpdateRequest updateReq = new AvaliacaoUpdateRequest();
                updateReq.acertosMinimos = request.notaMinima != null ? request.notaMinima.intValue() : null;
                updateReq.questoes = request.questoes;
                // Reaproveita o método que já lida com criação/atualização/deleção de questões e alternativas
                this.atualizarAvaliacaoCompleta(created.getIdAvaliacao(), updateReq);
            }

            return ResponseEntity.status(HttpStatus.OK).body(created);
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        } catch (ConflitoException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e
            );
        }
    }

    @PutMapping("/atualizar-acertos/{idAvaliacao}")
    public ResponseEntity<Avaliacao> atualizarAcertosMinimosAvaliacao(
            @RequestBody DefinirAcertosMinimosCommand request,
            @PathVariable Integer idAvaliacao
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarAcertosMinimosAvaliacaoUseCase.execute(idAvaliacao, request));
        } catch (NaoEncontradoException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        }
    }
    // Endpoint para buscar avaliação por id do curso, incluindo questões e alternativas
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<AvaliacaoCompletaResponse> getAvaliacaoByCurso(@PathVariable Long idCurso) {
        return avaliacaoGateway.findByFkCursoId(idCurso)
            .map(avaliacao -> {
                AvaliacaoCompletaResponse resp = new AvaliacaoCompletaResponse();
                resp.idAvaliacao = avaliacao.getIdAvaliacao();
                resp.acertosMinimos = avaliacao.getAcertosMinimos();
                resp.idCurso = avaliacao.getFkCurso() != null ? Long.valueOf(avaliacao.getFkCurso().getIdCurso()) : null;
                resp.questoes = new java.util.ArrayList<>();

                var questoes = questaoGateway.findAllByAvaliacao(avaliacao);
                for (var q : questoes) {
                    AvaliacaoCompletaResponse.QuestaoResponse qResp = new AvaliacaoCompletaResponse.QuestaoResponse();
                    qResp.idQuestao = q.getIdQuestaoComposto() != null ? q.getIdQuestaoComposto().getIdQuestao() : null;
                    qResp.enunciado = q.getEnunciado();
                    qResp.numeroQuestao = q.getNumeroQuestao();
                    qResp.fkAlternativaCorreta = q.getFkAlternativaCorreta() != null && q.getFkAlternativaCorreta().getAlternativaChaveComposta() != null ? q.getFkAlternativaCorreta().getAlternativaChaveComposta().getIdAlternativa() : null;
                    qResp.alternativas = new java.util.ArrayList<>();
                    var alternativas = alternativaGateway.findAllByQuestao(q);
                    for (var alt : alternativas) {
                        AvaliacaoCompletaResponse.AlternativaResponse aResp = new AvaliacaoCompletaResponse.AlternativaResponse();
                        aResp.idAlternativa = alt.getAlternativaChaveComposta() != null ? alt.getAlternativaChaveComposta().getIdAlternativa() : null;
                        aResp.texto = alt.getTexto();
                        aResp.ordemAlternativa = alt.getOrdem();
                        qResp.alternativas.add(aResp);
                    }
                    resp.questoes.add(qResp);
                }
                return ResponseEntity.ok(resp);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Novo endpoint para atualizar avaliação completa (nota mínima, questões e alternativas)
    @PutMapping("/{idAvaliacao}")
    public ResponseEntity<?> atualizarAvaliacaoCompleta(@PathVariable Integer idAvaliacao, @RequestBody AvaliacaoUpdateRequest request) {
        System.out.println("[AvaliacaoController] PUT /avaliacoes/" + idAvaliacao);
        Avaliacao avaliacao = avaliacaoGateway.findById(idAvaliacao);
        System.out.println("[AvaliacaoController] Resultado findById(" + idAvaliacao + "): " + (avaliacao != null ? "OK" : "null"));
        if (avaliacao == null) {
            return ResponseEntity.notFound().build();
        }
        avaliacao.setAcertosMinimos(request.acertosMinimos);
        avaliacaoGateway.save(avaliacao);

        // Atualiza/Cria/Remove questões
        var questoesExistentes = questaoGateway.findAllByAvaliacao(avaliacao);
        Map<Integer, Questao> questoesPorId = new HashMap<>();
        Map<Integer, Questao> questoesPorNumero = new HashMap<>();
        for (Questao qExistente : questoesExistentes) {
            if (qExistente.getIdQuestaoComposto() != null && qExistente.getIdQuestaoComposto().getIdQuestao() != null) {
                questoesPorId.putIfAbsent(qExistente.getIdQuestaoComposto().getIdQuestao(), qExistente);
            }
            if (qExistente.getNumeroQuestao() != null) {
                questoesPorNumero.putIfAbsent(qExistente.getNumeroQuestao(), qExistente);
            }
        }
    Set<Integer> questoesMantidasIds = new HashSet<>();
    java.util.List<AvaliacaoUpdateRequest.QuestaoUpdateRequest> questoesPayload =
        request.questoes != null ? request.questoes : java.util.Collections.emptyList();

        for (AvaliacaoUpdateRequest.QuestaoUpdateRequest qReq : questoesPayload) {
        java.util.List<AvaliacaoUpdateRequest.AlternativaUpdateRequest> alternativasPayload =
            qReq.alternativas != null ? qReq.alternativas : java.util.Collections.emptyList();

            Questao questaoSelecionada = null;
            if (qReq.idQuestao != null) {
                questaoSelecionada = questoesPorId.get(qReq.idQuestao);
            }
            if (questaoSelecionada == null && qReq.numeroQuestao != null) {
                questaoSelecionada = questoesPorNumero.get(qReq.numeroQuestao);
            }

            if (questaoSelecionada == null) {
                questaoSelecionada = new Questao();
                questaoSelecionada.setAvaliacao(avaliacao);
                if (qReq.idQuestao != null || qReq.numeroQuestao != null) {
                    QuestaoCompoundKey novoKey = new QuestaoCompoundKey();
                    novoKey.setFkAvaliacao(idAvaliacao);
                    if (qReq.idQuestao != null) {
                        novoKey.setIdQuestao(qReq.idQuestao);
                    }
                    questaoSelecionada.setIdQuestaoComposto(novoKey);
                }
            } else {
                questaoSelecionada.setAvaliacao(avaliacao);
            }

            questaoSelecionada.setEnunciado(qReq.enunciado);
            questaoSelecionada.setNumeroQuestao(qReq.numeroQuestao);

            Questao savedQuestao = questaoGateway.save(questaoSelecionada);
            if (savedQuestao.getIdQuestaoComposto() == null || savedQuestao.getIdQuestaoComposto().getIdQuestao() == null) {
                throw new IllegalStateException("Questao salva sem id composto!");
            }

            Integer questaoId = savedQuestao.getIdQuestaoComposto().getIdQuestao();
            questoesMantidasIds.add(questaoId);
            questoesPorId.put(questaoId, savedQuestao);
            if (savedQuestao.getNumeroQuestao() != null) {
                questoesPorNumero.put(savedQuestao.getNumeroQuestao(), savedQuestao);
            }

            // Alternativas
            var alternativasExistentes = alternativaGateway.findAllByQuestao(savedQuestao);
            Map<Integer, Alternativa> alternativasPorId = new HashMap<>();
            Map<Integer, Alternativa> alternativasPorOrdem = new HashMap<>();
            for (Alternativa altExistente : alternativasExistentes) {
                if (altExistente.getAlternativaChaveComposta() != null && altExistente.getAlternativaChaveComposta().getIdAlternativa() != null) {
                    alternativasPorId.putIfAbsent(altExistente.getAlternativaChaveComposta().getIdAlternativa(), altExistente);
                }
                if (altExistente.getOrdem() != null) {
                    alternativasPorOrdem.putIfAbsent(altExistente.getOrdem(), altExistente);
                }
            }

            for (Alternativa altExistente : alternativasExistentes) {
                if (altExistente.getAlternativaChaveComposta() == null) {
                    continue;
                }
                Integer altId = altExistente.getAlternativaChaveComposta().getIdAlternativa();
                Integer ordem = altExistente.getOrdem();
                boolean stillExists = alternativasPayload.stream().anyMatch(a ->
                        (a.idAlternativa != null && altId != null && Objects.equals(a.idAlternativa, altId)) ||
                        (a.idAlternativa == null && a.ordemAlternativa != null && ordem != null && Objects.equals(a.ordemAlternativa, ordem))
                );
                if (!stillExists) {
                    alternativaGateway.deleteById(altExistente.getAlternativaChaveComposta());
                }
            }

            for (AvaliacaoUpdateRequest.AlternativaUpdateRequest aReq : alternativasPayload) {
                Alternativa alternativaSelecionada = null;
                if (aReq.idAlternativa != null) {
                    alternativaSelecionada = alternativasPorId.get(aReq.idAlternativa);
                }
                if (alternativaSelecionada == null && aReq.ordemAlternativa != null) {
                    alternativaSelecionada = alternativasPorOrdem.get(aReq.ordemAlternativa);
                }
                if (alternativaSelecionada == null) {
                    alternativaSelecionada = new Alternativa();
                    if (aReq.idAlternativa != null) {
                        AlternativaCompoundKey aKey = new AlternativaCompoundKey();
                        aKey.setIdAlternativa(aReq.idAlternativa);
                        aKey.setIdQuestao(savedQuestao.getIdQuestaoComposto().getIdQuestao());
                        aKey.setIdAvaliacao(idAvaliacao);
                        alternativaSelecionada.setAlternativaChaveComposta(aKey);
                    }
                }
                alternativaSelecionada.setQuestao(savedQuestao);
                alternativaSelecionada.setTexto(aReq.texto);
                alternativaSelecionada.setOrdem(aReq.ordemAlternativa);

                Alternativa savedAlternativa = alternativaGateway.save(alternativaSelecionada);
                if (savedAlternativa.getAlternativaChaveComposta() != null && savedAlternativa.getAlternativaChaveComposta().getIdAlternativa() != null) {
                    alternativasPorId.put(savedAlternativa.getAlternativaChaveComposta().getIdAlternativa(), savedAlternativa);
                }
                if (savedAlternativa.getOrdem() != null) {
                    alternativasPorOrdem.put(savedAlternativa.getOrdem(), savedAlternativa);
                }
            }

            Alternativa alternativaCorreta = null;
            if (qReq.fkAlternativaCorreta != null) {
                var alternativasAtualizadas = alternativaGateway.findAllByQuestao(savedQuestao);
                alternativaCorreta = alternativasAtualizadas.stream()
                        .filter(alt -> alt.getAlternativaChaveComposta() != null && Objects.equals(qReq.fkAlternativaCorreta, alt.getAlternativaChaveComposta().getIdAlternativa()))
                        .findFirst()
                        .orElseGet(() -> alternativasAtualizadas.stream()
                                .filter(alt -> alt.getOrdem() != null && Objects.equals(alt.getOrdem(), qReq.fkAlternativaCorreta))
                                .findFirst()
                                .orElse(null));
                if (alternativaCorreta == null) {
                    System.out.println("[AvaliacaoController] Não foi possível localizar a alternativa correta para a questão " + questaoId + " usando identificador " + qReq.fkAlternativaCorreta + ".");
                }
            }
            savedQuestao.setFkAlternativaCorreta(alternativaCorreta);
            questaoGateway.save(savedQuestao);
        }

        for (Questao qExistente : questoesExistentes) {
            if (qExistente.getIdQuestaoComposto() == null || qExistente.getIdQuestaoComposto().getIdQuestao() == null) {
                continue;
            }
            if (questoesMantidasIds.contains(qExistente.getIdQuestaoComposto().getIdQuestao())) {
                continue;
            }
            var alternativas = alternativaGateway.findAllByQuestao(qExistente);
            for (Alternativa alt : alternativas) {
                boolean hasResposta = respostaDoUsuarioGateway.existsByAlternativa(alt);
                if (hasResposta) {
                    System.out.println("[AvaliacaoController] Alternativa " + alt.getAlternativaChaveComposta().getIdAlternativa() + " não pode ser deletada pois possui respostas de usuários.");
                    continue;
                }
                alternativaGateway.deleteById(alt.getAlternativaChaveComposta());
            }
            boolean allAltsDeletadas = alternativaGateway.findAllByQuestao(qExistente).isEmpty();
            if (allAltsDeletadas) {
                questaoGateway.deleteById(qExistente.getIdQuestaoComposto());
            } else {
                System.out.println("[AvaliacaoController] Questão " + qExistente.getIdQuestaoComposto().getIdQuestao() + " não pode ser deletada pois possui alternativas com respostas de usuários.");
            }
        }
        return ResponseEntity.ok().build();
    }
}
