package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import servicos.gratitude.be_gratitude_capacita.core.application.command.apostila.AtualizarApostilaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.command.apostila.CriarApostilaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila.*;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Page;
import servicos.gratitude.be_gratitude_capacita.core.domain.Pageable;
import servicos.gratitude.be_gratitude_capacita.S3.S3Service;

import java.util.List;

@RestController
@RequestMapping("/apostilas")
public class ApostilaController {
    private final CriarApostilaUseCase criarApostilaUseCase;
    private final AtualizarDadosApostilaUseCase atualizarDadosApostilaUseCase;
    private final AtualizarOcultoApostilaUseCase atualizarOcultoApostilaUseCase;
    private final DeletarApostilaUseCase deletarApostilaUseCase;
    private final ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase;
    private final ListarApostilaPorCursoPaginadoUseCase listarApostilaPorCursoPaginadoUseCase;
    private final S3Service s3Service;

    public ApostilaController(CriarApostilaUseCase criarApostilaUseCase,
            AtualizarDadosApostilaUseCase atualizarDadosApostilaUseCase,
            AtualizarOcultoApostilaUseCase atualizarOcultoApostilaUseCase,
            DeletarApostilaUseCase deletarApostilaUseCase, ListarApostilaPorCursoUseCase listarApostilaPorCursoUseCase,
            ListarApostilaPorCursoPaginadoUseCase listarApostilaPorCursoPaginadoUseCase,
            S3Service s3Service) {
        this.criarApostilaUseCase = criarApostilaUseCase;
        this.atualizarDadosApostilaUseCase = atualizarDadosApostilaUseCase;
        this.atualizarOcultoApostilaUseCase = atualizarOcultoApostilaUseCase;
        this.deletarApostilaUseCase = deletarApostilaUseCase;
        this.listarApostilaPorCursoUseCase = listarApostilaPorCursoUseCase;
        this.listarApostilaPorCursoPaginadoUseCase = listarApostilaPorCursoPaginadoUseCase;
        this.s3Service = s3Service;
    }

    @PostMapping
    public ResponseEntity<Apostila> cadastrarApostila(
            @RequestBody CriarApostilaCommand request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarApostilaUseCase.execute(request));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Endpoint multipart para criar apostila com upload do PDF para S3.
     * Recebe o arquivo PDF, faz upload para o bucket de apostilas e cria o registro no banco.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Apostila> cadastrarApostilaMultipart(
            @RequestParam("fkCurso") Integer fkCurso,
            @RequestParam("nomeApostila") String nomeApostila,
            @RequestParam(value = "descricaoApostila", required = false) String descricaoApostila,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
        try {
            System.out.println("[ApostilaController] Recebendo cadastro de apostila via multipart...");
            System.out.println("  - fkCurso: " + fkCurso);
            System.out.println("  - nomeApostila: " + nomeApostila);
            System.out.println("  - descricaoApostila: " + descricaoApostila);
            System.out.println("  - Arquivo recebido? " + (arquivo != null && !arquivo.isEmpty()));

            String urlArquivo = null;
            Integer tamanhoBytes = 0;

            if (arquivo != null && !arquivo.isEmpty()) {
                System.out.println("[ApostilaController] Enviando PDF para S3...");
                urlArquivo = s3Service.uploadApostila(arquivo);
                tamanhoBytes = (int) arquivo.getSize();
                System.out.println("[ApostilaController] URL do arquivo recebida do S3: " + urlArquivo);
            }

            // Cria o command com os dados
            CriarApostilaCommand command = new CriarApostilaCommand(
                fkCurso,
                nomeApostila,
                descricaoApostila,
                tamanhoBytes,
                urlArquivo
            );

            Apostila apostilaCriada = criarApostilaUseCase.execute(command);
            System.out.println("[ApostilaController] Apostila criada. ID: " + apostilaCriada.getIdApostila() + ", URL: " + apostilaCriada.getUrlArquivo());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(apostilaCriada);
        } catch (ConflitoException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("[ApostilaController] Erro ao cadastrar apostila: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao cadastrar apostila: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{fkCurso}")
    public ResponseEntity<List<Apostila>> listarApostilaPorCurso(
            @PathVariable Integer fkCurso) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(listarApostilaPorCursoUseCase.execute(fkCurso));
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{fkCurso}/paginated")
    public ResponseEntity<Page<Apostila>> listarApostilaPorCursoPaginado(
            @PathVariable Integer fkCurso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        try {
            Pageable pageable = Pageable.of(page, size, sortBy, sortDirection);
            Page<Apostila> apostilasPage = listarApostilaPorCursoPaginadoUseCase.execute(fkCurso, pageable);

            if (apostilasPage.empty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(apostilasPage);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/atualizar-dados/{idApostila}")
    public ResponseEntity<Apostila> atualizarDadosApostila(
            @RequestBody AtualizarApostilaCommand request,
            @PathVariable Integer idApostila) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(atualizarDadosApostilaUseCase.execute(request, idApostila));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/atualizar-oculto/{idApostila}")
    public ResponseEntity<Apostila> atualizarOcultoApostila(
            @PathVariable Integer idApostila) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(atualizarOcultoApostilaUseCase.execute(idApostila));
        } catch (ConflitoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{idAPostila}")
    public ResponseEntity deletarApostila(
            @PathVariable("idAPostila") Integer idApostila) {
        try {
            deletarApostilaUseCase.execute(idApostila);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NaoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
