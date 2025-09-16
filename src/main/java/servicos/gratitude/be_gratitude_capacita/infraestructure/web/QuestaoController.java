package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.questao.AtualizarQuestaoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.questao.CriarQuestaoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.questao.DefinirRespostaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaAlternativaUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.MontarChaveCompostaQuestaoUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.questao.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Questao;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.QuestaoCompoundKey;

import java.util.List;

@RestController
@RequestMapping("/questoes")
public class QuestaoController {
    private final CriarQuestaoUseCase criarQuestaoUseCase;
    private final AtualizarQuestaoUseCase atualizarQuestaoUseCase;
    private final DefinirRespostaUseCase definirRespostaUseCase;
    private final DeletarQuestaoUseCase deletarQuestaoUseCase;
    private final ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase;
    private final EncontrarQuestaoPorIdUseCase encontrarQuestaoPorIdUseCase;
    private final MontarChaveCompostaQuestaoUseCase montarChaveCompostaQuestaoUseCase;
    private final MontarChaveCompostaAlternativaUseCase montarChaveCompostaAlternativaUseCase;

    public QuestaoController(
            CriarQuestaoUseCase criarQuestaoUseCase,
            AtualizarQuestaoUseCase atualizarQuestaoUseCase,
            DefinirRespostaUseCase definirRespostaUseCase,
            DeletarQuestaoUseCase deletarQuestaoUseCase,
            ListarQuestoesPorAvaliacaoUseCase listarQuestoesPorAvaliacaoUseCase,
            EncontrarQuestaoPorIdUseCase encontrarQuestaoPorIdUseCase,
            MontarChaveCompostaQuestaoUseCase montarChaveCompostaQuestaoUseCase,
            MontarChaveCompostaAlternativaUseCase montarChaveCompostaAlternativaUseCase
    ) {
        this.criarQuestaoUseCase = criarQuestaoUseCase;
        this.atualizarQuestaoUseCase = atualizarQuestaoUseCase;
        this.definirRespostaUseCase = definirRespostaUseCase;
        this.deletarQuestaoUseCase = deletarQuestaoUseCase;
        this.listarQuestoesPorAvaliacaoUseCase = listarQuestoesPorAvaliacaoUseCase;
        this.encontrarQuestaoPorIdUseCase = encontrarQuestaoPorIdUseCase;
        this.montarChaveCompostaQuestaoUseCase = montarChaveCompostaQuestaoUseCase;
        this.montarChaveCompostaAlternativaUseCase = montarChaveCompostaAlternativaUseCase;
    }

    @PostMapping
    public ResponseEntity<Questao> cadastrarQuestao(
            @RequestBody CriarQuestaoCommand request
    ) {
        try {
            // Busca todas as questões da avaliação pelo id da avaliação
            List<Questao> questoes = listarQuestoesPorAvaliacaoUseCase.execute(request.fkAvaliacao());
            int maxId = 0;
            int maxNumero = 0;
            for (Questao q : questoes) {
                if (q.getIdQuestaoComposto() != null && q.getIdQuestaoComposto().getIdQuestao() != null && q.getIdQuestaoComposto().getIdQuestao() > maxId) {
                    maxId = q.getIdQuestaoComposto().getIdQuestao();
                }
                if (q.getNumeroQuestao() != null && q.getNumeroQuestao() > maxNumero) {
                    maxNumero = q.getNumeroQuestao();
                }
            }
            int idQuestao = maxId + 1;
            int numeroQuestao = maxNumero + 1;

            // Monta a chave composta da questão
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(request.fkAvaliacao(), idQuestao);

            // Cria um novo comando com o número da questão correto
            CriarQuestaoCommand comandoComNumero = new CriarQuestaoCommand(
                    request.fkAvaliacao(),
                    numeroQuestao,
                    request.enunciado(),
                    request.oculto()
            );

            Questao questao = criarQuestaoUseCase.execute(comandoComNumero, idQuestaoComposto);

            return ResponseEntity.status(HttpStatus.CREATED).body(questao);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ConflitoException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping("/{fkAvaliacao}")
    public ResponseEntity<List<Questao>> listarQuestoesPorAvaliacao(
            @PathVariable Integer fkAvaliacao
    ) {
        try {
            List<Questao> questoes = listarQuestoesPorAvaliacaoUseCase.execute(fkAvaliacao);
            return ResponseEntity.ok(questoes);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{fkAvaliacao}/{idQuestao}")
    public ResponseEntity<Questao> encontrarQuestaoPorId(
            @PathVariable Integer fkAvaliacao,
            @PathVariable Integer idQuestao
    ) {
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(fkAvaliacao, idQuestao);

            Questao questao = encontrarQuestaoPorIdUseCase.execute(idQuestaoComposto);

            return ResponseEntity.ok(questao);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{fkAvaliacao}/{idQuestao}")
    public ResponseEntity<Questao> atualizarQuestao(
            @PathVariable Integer fkAvaliacao,
            @PathVariable Integer idQuestao,
            @RequestBody AtualizarQuestaoCommand request
    ) {
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(fkAvaliacao, idQuestao);

            Questao questao = atualizarQuestaoUseCase.execute(request, idQuestaoComposto);

            return ResponseEntity.ok(questao);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @PutMapping("/definir-resposta/{fkAvaliacao}/{idQuestao}")
    public ResponseEntity<Questao> definirResposta(
            @PathVariable Integer fkAvaliacao,
            @PathVariable Integer idQuestao,
            @RequestBody DefinirRespostaCommand request
    ) {
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(fkAvaliacao, idQuestao);

            AlternativaCompoundKey idAlternativaComposto = montarChaveCompostaAlternativaUseCase.execute(
                    idQuestaoComposto,           // QuestaoCompoundKey primeiro
                    request.fkAlternativa()      // Integer segundo
            );

            Questao questao = definirRespostaUseCase.execute(idAlternativaComposto, idQuestaoComposto);

            return ResponseEntity.ok(questao);
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @DeleteMapping("/{fkAvaliacao}/{idQuestao}")
    public ResponseEntity<Void> deletarQuestao(
            @PathVariable Integer fkAvaliacao,
            @PathVariable Integer idQuestao
    ) {
        try {
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(fkAvaliacao, idQuestao);

            deletarQuestaoUseCase.execute(idQuestaoComposto);

            return ResponseEntity.ok().build();
        } catch (ValorInvalidoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}