package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
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
import servicos.gratitude.be_gratitude_capacita.core.gateways.TentativaGateway;
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

import org.springframework.cache.annotation.CacheEvict;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    private final CriarAvaliacaoUseCase criarAvaliacaoUseCase;
    private final AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase;
    private final AvaliacaoGateway avaliacaoGateway;
    private final QuestaoGateway questaoGateway;
    private final AlternativaGateway alternativaGateway;
    private final RespostaDoUsuarioGateway respostaDoUsuarioGateway;
    private final TentativaGateway tentativaGateway;

    public AvaliacaoController(
        CriarAvaliacaoUseCase criarAvaliacaoUseCase,
        AtualizarAcertosMinimosAvaliacaoUseCase atualizarAcertosMinimosAvaliacaoUseCase,
        AvaliacaoGateway avaliacaoGateway,
        QuestaoGateway questaoGateway,
        AlternativaGateway alternativaGateway,
        RespostaDoUsuarioGateway respostaDoUsuarioGateway,
        TentativaGateway tentativaGateway
    ) {
        this.criarAvaliacaoUseCase = criarAvaliacaoUseCase;
        this.atualizarAcertosMinimosAvaliacaoUseCase = atualizarAcertosMinimosAvaliacaoUseCase;
        this.avaliacaoGateway = avaliacaoGateway;
        this.questaoGateway = questaoGateway;
        this.alternativaGateway = alternativaGateway;
        this.respostaDoUsuarioGateway = respostaDoUsuarioGateway;
        this.tentativaGateway = tentativaGateway;
    }
    @GetMapping
    public ResponseEntity<?> listarAvaliacoes() {
        try {
            return ResponseEntity.ok(avaliacaoGateway.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar avaliações", e);
        }
    }

    @CacheEvict(cacheNames = "cursos", allEntries = true)
    @PostMapping
    public ResponseEntity<Avaliacao> cadastrarAvaliacao(
            @RequestBody servicos.gratitude.be_gratitude_capacita.infraestructure.web.request.CreateAvaliacaoRequest request
    ){
        try {
            System.out.println("[AvaliacaoController] fkCurso recebido: " + request.fkCurso);
            try {
                String payload = new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);
                System.out.println("[AvaliacaoController] Payload recebido (cadastrar): " + payload);
            } catch (Exception e) {
                System.out.println("[AvaliacaoController] Não foi possível serializar payload do request: " + e.getMessage());
            }
            servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.CriarAvaliacaoCommand cmd =
                    new servicos.gratitude.be_gratitude_capacita.core.application.command.avaliacao.CriarAvaliacaoCommand(request.fkCurso, request.notaMinima);
            if (request.questoes != null && !request.questoes.isEmpty() && request.notaMinima != null) {
                int qtdQuestoes = request.questoes.size();
                if (request.notaMinima.intValue() > qtdQuestoes) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null);
                }
            }
            var created = criarAvaliacaoUseCase.execute(cmd);

            if (request.questoes != null && !request.questoes.isEmpty()) {
                AvaliacaoUpdateRequest updateReq = new AvaliacaoUpdateRequest();
                updateReq.acertosMinimos = request.notaMinima != null ? request.notaMinima.intValue() : null;
                updateReq.questoes = request.questoes;
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

    @CacheEvict(cacheNames = "cursos", allEntries = true)
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
                    try {
                        System.out.println("[AvaliacaoController] getAvaliacaoByCurso - Questao id=" + (q.getIdQuestaoComposto()!=null?q.getIdQuestaoComposto().getIdQuestao():"null") + ", numero=" + q.getNumeroQuestao());
                    } catch (Exception e) {
                        System.out.println("[AvaliacaoController] Erro ao logar questao resumo (curso): " + e.getMessage());
                    }
                    AvaliacaoCompletaResponse.QuestaoResponse qResp = new AvaliacaoCompletaResponse.QuestaoResponse();
                    qResp.idQuestao = q.getIdQuestaoComposto() != null ? q.getIdQuestaoComposto().getIdQuestao() : null;
                    qResp.enunciado = q.getEnunciado();
                    qResp.numeroQuestao = q.getNumeroQuestao();
                    qResp.fkAlternativaCorreta = q.getFkAlternativaCorreta() != null && q.getFkAlternativaCorreta().getAlternativaChaveComposta() != null ? q.getFkAlternativaCorreta().getAlternativaChaveComposta().getIdAlternativa() : null;
                    qResp.alternativas = new java.util.ArrayList<>();
                    var alternativas = alternativaGateway.findAllByQuestao(q);
                    System.out.println("[AvaliacaoController] getAvaliacaoByCurso - Questao id=" + qResp.idQuestao + " encontrou alternativas=" + (alternativas!=null?alternativas.size():0));
                    for (var alt : alternativas) {
                        AvaliacaoCompletaResponse.AlternativaResponse aResp = new AvaliacaoCompletaResponse.AlternativaResponse();
                        aResp.idAlternativa = alt.getAlternativaChaveComposta() != null ? alt.getAlternativaChaveComposta().getIdAlternativa() : null;
                        aResp.texto = alt.getTexto();
                        aResp.ordemAlternativa = alt.getOrdem();
                        System.out.println("[AvaliacaoController] getAvaliacaoByCurso - Alternativa id=" + aResp.idAlternativa + " texto='" + aResp.texto + "' ordem=" + aResp.ordemAlternativa);
                        qResp.alternativas.add(aResp);
                    }
                    resp.questoes.add(qResp);
                }
                return ResponseEntity.ok(resp);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{idAvaliacao}")
    public ResponseEntity<AvaliacaoCompletaResponse> getAvaliacaoById(@PathVariable Integer idAvaliacao) {
        Avaliacao avaliacao = avaliacaoGateway.findById(idAvaliacao);
        if (avaliacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        AvaliacaoCompletaResponse resp = new AvaliacaoCompletaResponse();
        resp.idAvaliacao = avaliacao.getIdAvaliacao();
        resp.acertosMinimos = avaliacao.getAcertosMinimos();
        resp.idCurso = avaliacao.getFkCurso() != null ? Long.valueOf(avaliacao.getFkCurso().getIdCurso()) : null;
        resp.questoes = new java.util.ArrayList<>();

        var questoes = questaoGateway.findAllByAvaliacao(avaliacao);
        for (var q : questoes) {
            try {
                System.out.println("[AvaliacaoController] getAvaliacaoById - Questao id=" + (q.getIdQuestaoComposto()!=null?q.getIdQuestaoComposto().getIdQuestao():"null") + ", numero=" + q.getNumeroQuestao());
            } catch (Exception e) {
                System.out.println("[AvaliacaoController] Erro ao logar questao resumo (id): " + e.getMessage());
            }
            AvaliacaoCompletaResponse.QuestaoResponse qResp = new AvaliacaoCompletaResponse.QuestaoResponse();
            qResp.idQuestao = q.getIdQuestaoComposto() != null ? q.getIdQuestaoComposto().getIdQuestao() : null;
            qResp.enunciado = q.getEnunciado();
            qResp.numeroQuestao = q.getNumeroQuestao();
            qResp.fkAlternativaCorreta = q.getFkAlternativaCorreta() != null && q.getFkAlternativaCorreta().getAlternativaChaveComposta() != null ? q.getFkAlternativaCorreta().getAlternativaChaveComposta().getIdAlternativa() : null;
            qResp.alternativas = new java.util.ArrayList<>();
            var alternativas = alternativaGateway.findAllByQuestao(q);
            System.out.println("[AvaliacaoController] getAvaliacaoById - Questao id=" + qResp.idQuestao + " encontrou alternativas=" + (alternativas!=null?alternativas.size():0));
            for (var alt : alternativas) {
                AvaliacaoCompletaResponse.AlternativaResponse aResp = new AvaliacaoCompletaResponse.AlternativaResponse();
                aResp.idAlternativa = alt.getAlternativaChaveComposta() != null ? alt.getAlternativaChaveComposta().getIdAlternativa() : null;
                aResp.texto = alt.getTexto();
                aResp.ordemAlternativa = alt.getOrdem();
                System.out.println("[AvaliacaoController] getAvaliacaoById - Alternativa id=" + aResp.idAlternativa + " texto='" + aResp.texto + "' ordem=" + aResp.ordemAlternativa);
                qResp.alternativas.add(aResp);
            }
            resp.questoes.add(qResp);
        }
        return ResponseEntity.ok(resp);
    }

        @GetMapping("/{idAvaliacao}/respostas/existe")
        public ResponseEntity<?> checkExamHasResponses(@PathVariable Integer idAvaliacao) {
            Avaliacao avaliacao = avaliacaoGateway.findById(idAvaliacao);
            if (avaliacao == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("hasResponses", false));
            long count = respostaDoUsuarioGateway.countByExamId(idAvaliacao);
            return ResponseEntity.ok(java.util.Map.of("hasResponses", count > 0, "respostasCount", count));
        }

    @CacheEvict(cacheNames = "cursos", allEntries = true)
    @PutMapping("/{idAvaliacao}")
    public ResponseEntity<?> atualizarAvaliacaoCompleta(@PathVariable Integer idAvaliacao, @RequestBody AvaliacaoUpdateRequest request) {
        System.out.println("[AvaliacaoController] PUT /avaliacoes/" + idAvaliacao);
        try {
            String payloadSummary = "questoes=" + (request.questoes != null ? request.questoes.size() : 0) + ", acertosMinimos=" + request.acertosMinimos;
            System.out.println("[AvaliacaoController] Payload recebido (atualizar): " + payloadSummary);
            try {
                String full = new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);
                System.out.println("[AvaliacaoController] Payload completo (atualizar): " + full);
            } catch (Exception e) {
                System.out.println("[AvaliacaoController] Não foi possível serializar payload (atualizar): " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("[AvaliacaoController] Erro ao logar payload (atualizar): " + e.getMessage());
        }
        Avaliacao avaliacao = avaliacaoGateway.findById(idAvaliacao);
        System.out.println("[AvaliacaoController] Resultado findById(" + idAvaliacao + "): " + (avaliacao != null ? "OK" : "null"));
        if (avaliacao == null) {
            return ResponseEntity.notFound().build();
        }
        if (request.acertosMinimos != null && request.questoes != null && !request.questoes.isEmpty()) {
            int qtdQuestoes = request.questoes.size();
            if (request.acertosMinimos > qtdQuestoes) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Acertos mínimos não pode ser maior que número de questões (" + qtdQuestoes + ")");
            }
        }
        avaliacao.setAcertosMinimos(request.acertosMinimos);
        avaliacaoGateway.save(avaliacao);

        var questoesExistentesPatch = questaoGateway.findAllByAvaliacao(avaliacao);
        int qtdQuestoesPatch = questoesExistentesPatch != null ? questoesExistentesPatch.size() : 0;
        if (qtdQuestoesPatch > 0 && request.acertosMinimos > qtdQuestoesPatch) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Acertos mínimos não pode ser maior que número de questões (" + qtdQuestoesPatch + ")");
        }
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

    @PatchMapping("/{idAvaliacao}")
    public ResponseEntity<?> patchAvaliacao(@PathVariable Integer idAvaliacao, @RequestBody AvaliacaoUpdateRequest request) {
        System.out.println("[AvaliacaoController] PATCH /avaliacoes/" + idAvaliacao);
        Avaliacao avaliacao = avaliacaoGateway.findById(idAvaliacao);
        if (avaliacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean updated = false;
        if (request.acertosMinimos != null) {
            avaliacao.setAcertosMinimos(request.acertosMinimos);
            avaliacaoGateway.save(avaliacao);
            updated = true;
        }
        if (request.questoes == null || request.questoes.isEmpty()) {
            return updated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

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

        for (AvaliacaoUpdateRequest.QuestaoUpdateRequest qReq : request.questoes) {
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
                    if (qReq.idQuestao != null) novoKey.setIdQuestao(qReq.idQuestao);
                    questaoSelecionada.setIdQuestaoComposto(novoKey);
                }
            } else {
                questaoSelecionada.setAvaliacao(avaliacao);
            }
            questaoSelecionada.setEnunciado(qReq.enunciado);
            questaoSelecionada.setNumeroQuestao(qReq.numeroQuestao);
            Questao savedQuestao = questaoGateway.save(questaoSelecionada);
            Integer questaoId = savedQuestao.getIdQuestaoComposto().getIdQuestao();
            questoesPorId.put(questaoId, savedQuestao);
            if (savedQuestao.getNumeroQuestao() != null) questoesPorNumero.put(savedQuestao.getNumeroQuestao(), savedQuestao);

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
            for (AvaliacaoUpdateRequest.AlternativaUpdateRequest aReq : alternativasPayload) {
                Alternativa alternativaSelecionada = null;
                if (aReq.idAlternativa != null) alternativaSelecionada = alternativasPorId.get(aReq.idAlternativa);
                if (alternativaSelecionada == null && aReq.ordemAlternativa != null) alternativaSelecionada = alternativasPorOrdem.get(aReq.ordemAlternativa);
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

            if (qReq.fkAlternativaCorreta != null) {
                var alternativasAtualizadas = alternativaGateway.findAllByQuestao(savedQuestao);
                Alternativa alternativaCorreta = alternativasAtualizadas.stream()
                    .filter(alt -> alt.getAlternativaChaveComposta() != null && Objects.equals(qReq.fkAlternativaCorreta, alt.getAlternativaChaveComposta().getIdAlternativa()))
                    .findFirst()
                    .orElseGet(() -> alternativasAtualizadas.stream()
                        .filter(alt -> alt.getOrdem() != null && Objects.equals(alt.getOrdem(), qReq.fkAlternativaCorreta))
                        .findFirst()
                        .orElse(null));
                savedQuestao.setFkAlternativaCorreta(alternativaCorreta);
                questaoGateway.save(savedQuestao);
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Exclui completamente uma avaliação se não houver respostas de usuários associadas
     * a nenhuma alternativa das suas questões. Caso existam respostas, retorna 409 e não exclui.
     */
    @DeleteMapping("/{idAvaliacao}")
    @Transactional
    public ResponseEntity<?> deletarAvaliacao(@PathVariable Integer idAvaliacao, @RequestParam(name="force", required=false, defaultValue="false") Boolean force) {
        java.util.UUID requestId = java.util.UUID.randomUUID();
        System.out.println("[AvaliacaoController]["+requestId+"] DELETE /avaliacoes/" + idAvaliacao + " force=" + force);
        Avaliacao avaliacao = avaliacaoGateway.findById(idAvaliacao);
        if (avaliacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avaliação não encontrada");
        }

        var questoes = questaoGateway.findAllByAvaliacao(avaliacao);
        long respostasCount = respostaDoUsuarioGateway.countByExamId(idAvaliacao);
        if (respostasCount > 0 && !force) {
            java.util.Map<String,Object> body = new java.util.HashMap<>();
            body.put("message", "Não é possível excluir: já existem respostas registradas.");
            body.put("respostasCount", respostasCount);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        if (force) {
            if (respostasCount > 0) {
                System.out.println("[AvaliacaoController]["+requestId+"] Iniciando remoção de respostas: total=" + respostasCount);
                try {
                    respostaDoUsuarioGateway.deleteByExamId(idAvaliacao);
                    long remaining = respostaDoUsuarioGateway.countByExamId(idAvaliacao);
                    System.out.println("[AvaliacaoController]["+requestId+"] Após remoção de respostas remaining=" + remaining);
                    if (remaining > 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover todas as respostas (restantes=" + remaining + ")");
                    }
                } catch (Exception e) {
                    System.out.println("[AvaliacaoController]["+requestId+"] Exceção ao remover respostas: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover respostas associadas; operação abortada.");
                }
            }
            long tentativasCount = tentativaGateway.countByAvaliacaoId(idAvaliacao);
            System.out.println("[AvaliacaoController]["+requestId+"] Tentativas associadas encontradas=" + tentativasCount);
            if (tentativasCount > 0) {
                int deletadas = tentativaGateway.deleteByAvaliacaoId(idAvaliacao);
                long tentativasRestantes = tentativaGateway.countByAvaliacaoId(idAvaliacao);
                System.out.println("[AvaliacaoController]["+requestId+"] Tentativas deletadas=" + deletadas + ", restantes=" + tentativasRestantes);
                if (tentativasRestantes > 0) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover tentativas associadas (restantes=" + tentativasRestantes + ")");
                }
            }
            boolean alternativaComRespostaGlobal = false;
            java.util.List<Integer> alternativasComResposta = new java.util.ArrayList<>();
            for (Questao q : questoes) {
                var alternativas = alternativaGateway.findAllByQuestao(q);
                for (Alternativa alt : alternativas) {
                    if (alt.getAlternativaChaveComposta() != null && respostaDoUsuarioGateway.existsByAlternativa(alt)) {
                        alternativaComRespostaGlobal = true;
                        alternativasComResposta.add(alt.getAlternativaChaveComposta().getIdAlternativa());
                    }
                }
            }
            if (alternativaComRespostaGlobal) {
                java.util.Map<String,Object> body = new java.util.HashMap<>();
                body.put("message", "Não foi possível excluir: alternativas ainda possuem respostas globais (provável reutilização de id_alternativa). Migração de chave necessária.");
                body.put("alternativasComResposta", alternativasComResposta);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
            }
        }

        for (Questao q : questoes) {
            var alternativas = alternativaGateway.findAllByQuestao(q);
            for (Alternativa alt : alternativas) {
                boolean hasResposta = respostaDoUsuarioGateway.existsByAlternativa(alt);
                if (hasResposta && !force) {
                    System.out.println("[AvaliacaoController]["+requestId+"] Não pode excluir: alternativa " + (alt.getAlternativaChaveComposta()!=null?alt.getAlternativaChaveComposta().getIdAlternativa():"null") + " possui respostas.");
                    java.util.Map<String,Object> body = new java.util.HashMap<>();
                    body.put("message", "Não é possível excluir: já existem respostas registradas.");
                    body.put("respostasCount", respostasCount);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                }
            }
        }

        for (Questao q : questoes) {
            var alternativas = alternativaGateway.findAllByQuestao(q);
            for (Alternativa alt : alternativas) {
                if (alt.getAlternativaChaveComposta() != null) {
                    if (respostaDoUsuarioGateway.existsByAlternativa(alt)) {
                        System.out.println("[AvaliacaoController]["+requestId+"] Skip delete alternativa " + alt.getAlternativaChaveComposta().getIdAlternativa() + " (resposta residual detectada)");
                        continue;
                    }
                    alternativaGateway.deleteById(alt.getAlternativaChaveComposta());
                }
            }
            if (q.getIdQuestaoComposto() != null) {
                questaoGateway.deleteById(q.getIdQuestaoComposto());
            }
        }

        try {
            avaliacaoGateway.deleteById(idAvaliacao);
            System.out.println("[AvaliacaoController]["+requestId+"] Avaliação " + idAvaliacao + " excluída.");
        } catch (Exception e) {
            System.out.println("[AvaliacaoController]["+requestId+"] Falha ao excluir avaliação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir avaliação");
        }
        return ResponseEntity.ok().body("Avaliação excluída com sucesso");
    }
}
