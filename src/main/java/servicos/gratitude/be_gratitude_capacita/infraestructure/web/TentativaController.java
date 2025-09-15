package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.util.List;

@RestController
@RequestMapping("/tentativas")
public class TentativaController {
    private final CriarTentativaUseCase criarTentativaUseCase;
    private final ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase;
    private final MontarChaveCompostaMatriculaUseCase montarChaveCompostaMatriculaUseCase;
    private final EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase;
    private final CriarNovaChaveCompostaTentativaUseCase criarNovaChaveCompostaTentativaUseCase;
    private final EncontrarAvaliacaoPorIdUseCase encontrarAvaliacaoPorIdUseCase;

    public TentativaController(CriarTentativaUseCase criarTentativaUseCase, ListarTentativaPorMatriculaUseCase listarTentativaPorMatriculaUseCase, MontarChaveCompostaMatriculaUseCase montarChaveCompostaMatriculaUseCase, EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase, CriarNovaChaveCompostaTentativaUseCase criarNovaChaveCompostaTentativaUseCase, EncontrarAvaliacaoPorIdUseCase encontrarAvaliacaoPorIdUseCase) {
        this.criarTentativaUseCase = criarTentativaUseCase;
        this.listarTentativaPorMatriculaUseCase = listarTentativaPorMatriculaUseCase;
        this.montarChaveCompostaMatriculaUseCase = montarChaveCompostaMatriculaUseCase;
        this.encontrarMatriculaPorIdUseCase = encontrarMatriculaPorIdUseCase;
        this.criarNovaChaveCompostaTentativaUseCase = criarNovaChaveCompostaTentativaUseCase;
        this.encontrarAvaliacaoPorIdUseCase = encontrarAvaliacaoPorIdUseCase;
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
