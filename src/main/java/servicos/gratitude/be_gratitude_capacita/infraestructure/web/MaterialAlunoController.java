package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.materialAluno.*;
import servicos.gratitude.be_gratitude_capacita.core.application.usecase.matricula.EncontrarMatriculaPorIdUseCase;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;

import java.util.List;

@RestController
@RequestMapping("/materiais-alunos")
public class MaterialAlunoController {
    private final CriarMaterialAlunoUseCase criarMaterialAlunoUseCase;
    private final ListarMaterialPorMatriculaUseCase listarMaterialPorMatriculaUseCase;
    private final AtualizarUltimoAcessoMaterialUseCase atualizarUltimoAcessoMaterialUseCase;
    private final FinalizarMaterialUseCase finalizarMaterialUseCase;
    private final DeletarMaterialUseCase deletarMaterialUseCase;
    private final EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase;

    public MaterialAlunoController(
            CriarMaterialAlunoUseCase criarMaterialAlunoUseCase,
            ListarMaterialPorMatriculaUseCase listarMaterialPorMatriculaUseCase,
            AtualizarUltimoAcessoMaterialUseCase atualizarUltimoAcessoMaterialUseCase,
            FinalizarMaterialUseCase finalizarMaterialUseCase,
            DeletarMaterialUseCase deletarMaterialUseCase,
            EncontrarMatriculaPorIdUseCase encontrarMatriculaPorIdUseCase
    ) {
        this.criarMaterialAlunoUseCase = criarMaterialAlunoUseCase;
        this.listarMaterialPorMatriculaUseCase = listarMaterialPorMatriculaUseCase;
        this.atualizarUltimoAcessoMaterialUseCase = atualizarUltimoAcessoMaterialUseCase;
        this.finalizarMaterialUseCase = finalizarMaterialUseCase;
        this.deletarMaterialUseCase = deletarMaterialUseCase;
        this.encontrarMatriculaPorIdUseCase = encontrarMatriculaPorIdUseCase;
    }

    @GetMapping("/listar-por-matricula/{fkUsuario}/{fkCurso}")
    public ResponseEntity<List<MaterialAluno>> listarMateriaisPorMatricula(
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
        idMatriculaComposto.setFkUsuario(fkUsuario);
        idMatriculaComposto.setFkCurso(fkCurso);

        Matricula matricula = encontrarMatriculaPorIdUseCase.execute(idMatriculaComposto);
        List<MaterialAluno> materiais = listarMaterialPorMatriculaUseCase.execute(matricula);

        return ResponseEntity.ok(materiais);
    }

    @DeleteMapping("/{idMaterialAluno}/{fkUsuario}/{fkCurso}")
    public ResponseEntity<Void> deletarMaterialAluno(
            @PathVariable Integer idMaterialAluno,
            @PathVariable Integer fkUsuario,
            @PathVariable Integer fkCurso
    ) {
        MatriculaCompoundKey idMatriculaComposto = new MatriculaCompoundKey();
        idMatriculaComposto.setFkUsuario(fkUsuario);
        idMatriculaComposto.setFkCurso(fkCurso);

        MaterialAlunoCompoundKey idMaterialAlunoComposto = new MaterialAlunoCompoundKey();
        idMaterialAlunoComposto.setIdMaterialAluno(idMaterialAluno);
        idMaterialAlunoComposto.setIdMatriculaComposto(idMatriculaComposto);

        deletarMaterialUseCase.execute(idMaterialAlunoComposto);

        return ResponseEntity.ok().build();
    }
}