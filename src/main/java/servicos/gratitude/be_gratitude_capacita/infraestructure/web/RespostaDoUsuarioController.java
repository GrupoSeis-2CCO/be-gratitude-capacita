package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.respostaDoUsuario.CriarRespostaDoUsuarioCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.alternativa.EncontrarAlternativaPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.compoundKeys.*;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.CriarRespostaDoUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario.ListarRespostasDoUsuarioUseCase;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.tentativa.EncontrarTentativaPorId;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.*;

import java.util.List;

@RestController
@RequestMapping("/respostas")
public class RespostaDoUsuarioController {
    private final CriarRespostaDoUsuarioUseCase criarRespostaDoUsuarioUseCase;
    private final ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase;
    private final MontarChaveCompostaTentativaUseCase montarChaveCompostaTentativaUseCase;
    private final EncontrarTentativaPorId encontrarTentativaPorId;
    private final MontarChaveCompostaAlternativaUseCase montarChaveCompostaAlternativaUseCase;
    private final EncontrarAlternativaPorIdUseCase encontrarAlternativaPorIdUseCase;
    private final MontarChaveCompostaMatriculaUseCase montarChaveCompostaMatriculaUseCase;
    private final MontarChaveCompostaQuestaoUseCase montarChaveCompostaQuestaoUseCase;
    private final CriarNovaChaveCompostaRespostaDoUsuarioUseCase criarNovaChaveCompostaRespostaDoUsuarioUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    public RespostaDoUsuarioController(
            CriarRespostaDoUsuarioUseCase criarRespostaDoUsuarioUseCase,
            ListarRespostasDoUsuarioUseCase listarRespostasDoUsuarioUseCase,
            MontarChaveCompostaTentativaUseCase montarChaveCompostaTentativaUseCase,
            EncontrarTentativaPorId encontrarTentativaPorId,
            MontarChaveCompostaAlternativaUseCase montarChaveCompostaAlternativaUseCase,
            EncontrarAlternativaPorIdUseCase encontrarAlternativaPorIdUseCase,
            MontarChaveCompostaMatriculaUseCase montarChaveCompostaMatriculaUseCase,
            MontarChaveCompostaQuestaoUseCase montarChaveCompostaQuestaoUseCase,
            CriarNovaChaveCompostaRespostaDoUsuarioUseCase criarNovaChaveCompostaRespostaDoUsuarioUseCase,
            BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase
    ) {
        this.criarRespostaDoUsuarioUseCase = criarRespostaDoUsuarioUseCase;
        this.listarRespostasDoUsuarioUseCase = listarRespostasDoUsuarioUseCase;
        this.montarChaveCompostaTentativaUseCase = montarChaveCompostaTentativaUseCase;
        this.encontrarTentativaPorId = encontrarTentativaPorId;
        this.montarChaveCompostaAlternativaUseCase = montarChaveCompostaAlternativaUseCase;
        this.encontrarAlternativaPorIdUseCase = encontrarAlternativaPorIdUseCase;
        this.montarChaveCompostaMatriculaUseCase = montarChaveCompostaMatriculaUseCase;
        this.montarChaveCompostaQuestaoUseCase = montarChaveCompostaQuestaoUseCase;
        this.criarNovaChaveCompostaRespostaDoUsuarioUseCase = criarNovaChaveCompostaRespostaDoUsuarioUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
    }




    @PostMapping
    public ResponseEntity<RespostaDoUsuario> cadastrarRespostaDoUsuario(
            @RequestBody CriarRespostaDoUsuarioCommand request
    ){
        try {
            MatriculaCompoundKey idMatriculaComposto = montarChaveCompostaMatriculaUseCase.execute(request.fkCurso(), request.fkUsuario());
            TentativaCompoundKey idTentativaComposto = montarChaveCompostaTentativaUseCase.execute(idMatriculaComposto, request.fkTentativa());
            Tentativa tentativa = encontrarTentativaPorId.execute(idTentativaComposto);
            QuestaoCompoundKey idQuestaoComposto = montarChaveCompostaQuestaoUseCase.execute(request.fkAvaliacao(), request.fkQuestao());
            AlternativaCompoundKey idAlternativaComposta = montarChaveCompostaAlternativaUseCase.execute(idQuestaoComposto, request.fkAlternativa());
            Alternativa alternativa = encontrarAlternativaPorIdUseCase.execute(idAlternativaComposta);
            RespostaDoUsuarioCompoundKey idRespostaComposto = criarNovaChaveCompostaRespostaDoUsuarioUseCase.execute(idAlternativaComposta, idTentativaComposto);
            RespostaDoUsuario respostaDoUsuario = criarRespostaDoUsuarioUseCase.execute(tentativa, alternativa, idRespostaComposto);

            return ResponseEntity.status(HttpStatus.CREATED).body(respostaDoUsuario);
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

    @GetMapping("/{fkUsuario}")
    public ResponseEntity<List<RespostaDoUsuario>> listarRespostasPorUsuario(
            @PathVariable Integer fkUsuario
    ){
        try {
            Usuario usuario = buscarUsuarioPorIdUseCase.execute(fkUsuario);
            List<RespostaDoUsuario> respostas = listarRespostasDoUsuarioUseCase.execute(usuario);

            return ResponseEntity.status(HttpStatus.OK).body(respostas);
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
