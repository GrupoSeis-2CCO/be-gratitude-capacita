package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.tentativa.CriarTentativaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.EncontrarAvaliacaoPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.CriarNovaChaveCompostaTentativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.EncontrarMatriculaPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.CriarTentativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.ListarTentativaPorMatriculaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.TentativaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.TentativaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.response.TentativaComCursoResponse;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tentativas")
public class TentativaController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TentativaController.class);
    private final CriarTentativaUseCase criarTentativaUseCase;
    private final ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase;
    private final MontarChaveCompostaMatriculaUseCase montarChaveCompostaMatriculaUseCase;
    private final EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase;
    private final CriarNovaChaveCompostaTentativaUseCase criarNovaChaveCompostaTentativaUseCase;
    private final EncontrarAvaliacaoPorIdUseCase encontrarAvaliacaoPorIdUseCase;
    private final servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase;
    private final servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.ListarTentativaPorUsuarioUseCase listarTentativaPorUsuarioUseCase;
    private final servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.CalcularNotaPorTentativaUseCase calcularNotaPorTentativaUseCase;
    private final TentativaRepository tentativaRepository;

    public TentativaController(CriarTentativaUseCase criarTentativaUseCase, ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase, MontarChaveCompostaMatriculaUseCase montarChaveCompostaMatriculaUseCase, EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase, CriarNovaChaveCompostaTentativaUseCase criarNovaChaveCompostaTentativaUseCase, EncontrarAvaliacaoPorIdUseCase encontrarAvaliacaoPorIdUseCase, servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.CalcularNotaPorTentativaUseCase calcularNotaPorTentativaUseCase, servicos.gratitude.be_gratitude_capacita.core.application.usecase.avaliacao.ListarAvaliacaoPorCursoUseCase listarAvaliacaoPorCursoUseCase, servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.ListarTentativaPorUsuarioUseCase listarTentativaPorUsuarioUseCase, TentativaRepository tentativaRepository) {
        this.criarTentativaUseCase = criarTentativaUseCase;
        this.listarTentativaPorMatriculaUseCase = listarTentativaPorMatriculaUseCase;
        this.montarChaveCompostaMatriculaUseCase = montarChaveCompostaMatriculaUseCase;
        this.encontrarMatriculaPorIdUseCase = encontrarMatriculaPorIdUseCase;
        this.criarNovaChaveCompostaTentativaUseCase = criarNovaChaveCompostaTentativaUseCase;
        this.encontrarAvaliacaoPorIdUseCase = encontrarAvaliacaoPorIdUseCase;
        this.calcularNotaPorTentativaUseCase = calcularNotaPorTentativaUseCase;
        this.listarAvaliacaoPorCursoUseCase = listarAvaliacaoPorCursoUseCase;
        this.listarTentativaPorUsuarioUseCase = listarTentativaPorUsuarioUseCase;
        this.tentativaRepository = tentativaRepository;
    }

    @PostMapping
    public ResponseEntity<Tentativa> cadastrarTentativa(
            CriarTentativaCommand request
    ){
        try {
            MatriculaCompoundKey idMatriculaComposto = montarChaveCompostaMatriculaUseCase.execute(request.fkCurso(), request.fkUsuario());
            Matricula matricula = encontrarMatriculaPorIdUseCase.execute(idMatriculaComposto);
            Avaliacao avaliacao = encontrarAvaliacaoPorIdUseCase.execute(request.fkAvaliacao());
            TentativaCompoundKey idTentativaComposto = criarNovaChaveCompostaTentativaUseCase.execute(idMatriculaComposto);
            Tentativa tentativa = criarTentativaUseCase.execute(matricula, avaliacao, idTentativaComposto);
            return ResponseEntity.status(HttpStatus.CREATED).body(tentativa);
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
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

    @GetMapping("/usuario/{fkUsuario}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<TentativaComCursoResponse>> listarTentativasPorUsuario(
            @PathVariable Integer fkUsuario
    ){
        try {
            // Buscar entidades com join fetch (dentro da transação)
            List<TentativaEntity> entities = tentativaRepository.findAllByUsuario(fkUsuario);

            if (entities.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            // Converter para DTO dentro da transação (curso está carregado)
            List<TentativaComCursoResponse> response = entities.stream().map(entity -> {
                TentativaComCursoResponse dto = new TentativaComCursoResponse();
                
                // Dados da tentativa
                dto.setIdTentativa(entity.getIdTentativaComposto().getIdTentativa());
                dto.setFkUsuario(entity.getIdTentativaComposto().getIdMatriculaComposto().getFkUsuario());
                dto.setFkCurso(entity.getIdTentativaComposto().getIdMatriculaComposto().getFkCurso());
                dto.setFkAvaliacao(entity.getAvaliacao() != null ? entity.getAvaliacao().getIdAvaliacao() : null);
                dto.setDtTentativa(entity.getDtTentativa());
                
                // Nome do curso (está carregado via join fetch)
                String nomeCurso = null;
                if (entity.getMatricula() != null && entity.getMatricula().getCurso() != null) {
                    nomeCurso = entity.getMatricula().getCurso().getTituloCurso();
                    if (nomeCurso == null && entity.getMatricula().getCurso().getIdCurso() != null) {
                        nomeCurso = "Curso " + entity.getMatricula().getCurso().getIdCurso();
                    }
                }
                dto.setNomeCurso(nomeCurso);
                
                // Calcular nota (usando domain object temporário)
                try {
                    Tentativa tentativaDomain = new Tentativa();
                    tentativaDomain.setIdTentativaComposto(servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.TentativaCompoundKeyMapper.toDomain(entity.getIdTentativaComposto()));
                    
                    // CRITICAL: Set Avaliacao so the UseCase can get total questions
                    if (entity.getAvaliacao() != null) {
                        tentativaDomain.setAvaliacao(servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.AvaliacaoMapper.toDomainKeyOnly(entity.getAvaliacao()));
                    }
                    
                    servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.NotaResult notaResult = calcularNotaPorTentativaUseCase.execute(tentativaDomain);
                    if (notaResult != null) {
                        dto.setNotaAcertos(notaResult.getCorretas());
                        dto.setNotaTotal(notaResult.getTotal());
                        dto.setNota(notaResult.getPercent());
                        logger.debug("TentativaController: nota calculada para tentativa {} -> corretas={}, total={}, percent={}", 
                                dto.getIdTentativa(), notaResult.getCorretas(), notaResult.getTotal(), notaResult.getPercent());
                    }
                } catch (Exception e){
                    logger.warn("TentativaController: falha ao calcular nota para tentativa {}: {}", dto.getIdTentativa(), e.getMessage(), e);
                }
                
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            logger.error("Erro ao listar tentativas por usuario {}", fkUsuario, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado", e);
        }
    }

    // Convenience endpoint: allow POST /tentativas/{fkCurso}/{fkUsuario}
    @PostMapping("/{fkCurso}/{fkUsuario}")
    public ResponseEntity<Tentativa> cadastrarTentativaPorPath(
            @PathVariable Integer fkCurso,
            @PathVariable Integer fkUsuario,
            @RequestParam(required = false) Integer fkAvaliacao
    ){
        // If fkAvaliacao not provided or invalid, try to pick a sensible fallback: first avaliacao for the course
        Integer chosenAvaliacao = fkAvaliacao;
        if (chosenAvaliacao != null) {
            try {
                // verify exists
                encontrarAvaliacaoPorIdUseCase.execute(chosenAvaliacao);
            } catch (Exception e) {
                chosenAvaliacao = null; // will try fallback
            }
        }

        if (chosenAvaliacao == null) {
            try {
                java.util.List<servicos.gratitude.be_gratitude_capacita.core.domain.Avaliacao> avaliacoes = listarAvaliacaoPorCursoUseCase.execute(fkCurso);
                if (avaliacoes != null && !avaliacoes.isEmpty()) {
                    chosenAvaliacao = avaliacoes.get(0).getIdAvaliacao();
                }
            } catch (Exception e) {
                // ignore and let below check produce 404
            }
        }

        if (chosenAvaliacao == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado avaliação para o curso informado");
        }

        CriarTentativaCommand cmd = new CriarTentativaCommand(fkUsuario, fkCurso, chosenAvaliacao);
        return cadastrarTentativa(cmd);
    }

    @GetMapping("/{fkCurso}/{fkUsuario}")
    public ResponseEntity<List<Tentativa>> listarTentativasPorMatricula(
            @PathVariable Integer fkCurso,
            @PathVariable Integer fkUsuario
    ){
        try {
            MatriculaCompoundKey idMatriculaComposto = montarChaveCompostaMatriculaUseCase.execute(fkCurso, fkUsuario);
            Matricula matricula = encontrarMatriculaPorIdUseCase.execute(idMatriculaComposto);
            List<Tentativa> tentativas = listarTentativaPorMatriculaUseCase.execute(matricula);

            if (tentativas.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            // calculate nota for each tentativa
            for (Tentativa t : tentativas) {
                try {
                    servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.NotaResult notaResult = calcularNotaPorTentativaUseCase.execute(t);
                    if (notaResult != null) {
                        t.setNotaAcertos(notaResult.getCorretas());
                        t.setNotaTotal(notaResult.getTotal());
                        t.setNota(notaResult.getPercent());
                        logger.debug("TentativaController: nota calculada para tentativa {} -> corretas={}, total={}, percent={}", t.getIdTentativaComposto(), notaResult.getCorretas(), notaResult.getTotal(), notaResult.getPercent());
                    }
                } catch (Exception e){
                    // if calculation fails, leave nota fields null but log to help debugging
                    logger.warn("TentativaController: falha ao calcular nota para tentativa {}: {}", t.getIdTentativaComposto(), e.toString());
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(tentativas);
        } catch (ValorInvalidoException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
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
}
