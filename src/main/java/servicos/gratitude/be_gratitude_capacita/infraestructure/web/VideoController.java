package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.command.video.AtualizarVideoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.video.CriarVideoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.video.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Video;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {
    private final AtualizarDadosVideoUseCase atualizarDadosVideoUseCase;
    private final AtualizarOcultoVideoUseCase atualizarOcultoVideoUseCase;
    private final CriarVideoUseCase criarVideoUseCase;
    private final DeletarVideoUseCase deletarVideoUseCase;
    private final ListarVideoPorCursoUseCase listarVideoPorCursoUseCase;

    public VideoController(AtualizarDadosVideoUseCase atualizarDadosVideoUseCase, AtualizarOcultoVideoUseCase atualizarOcultoVideoUseCase, CriarVideoUseCase criarVideoUseCase, DeletarVideoUseCase deletarVideoUseCase, ListarVideoPorCursoUseCase listarVideoPorCursoUseCase) {
        this.atualizarDadosVideoUseCase = atualizarDadosVideoUseCase;
        this.atualizarOcultoVideoUseCase = atualizarOcultoVideoUseCase;
        this.criarVideoUseCase = criarVideoUseCase;
        this.deletarVideoUseCase = deletarVideoUseCase;
        this.listarVideoPorCursoUseCase = listarVideoPorCursoUseCase;
    }

    @PostMapping
    public ResponseEntity<Video> cadastrarVideo(
            @RequestBody CriarVideoCommand request
    ){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarVideoUseCase.execute(request));
        } catch (ConflitoException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e
            );
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping("/{fkCurso}")
    public ResponseEntity<List<Video>> listarVideoPorCurso(
            @PathVariable Integer fkCurso
    ){
        try {
            List<Video> responses = listarVideoPorCursoUseCase.execute(fkCurso);

            if (responses.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @PutMapping("/atualizar-dados/{idVideo}")
    public ResponseEntity<Video> atualizarDados(
            @RequestBody AtualizarVideoCommand request,
            @PathVariable Integer idVideo
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarDadosVideoUseCase.execute(request, idVideo));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e
            );
        }
    }

    @PutMapping("/atualizar-oculto/{idVideo}")
    public ResponseEntity<Video> atualizarOculto(
            @PathVariable Integer idVideo
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarOcultoVideoUseCase.execute(idVideo));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @DeleteMapping("/{idVideo}")
    public ResponseEntity deletarVideo(
            @PathVariable Integer idVideo
    ){
        try {
            deletarVideoUseCase.execute(idVideo);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }
}
