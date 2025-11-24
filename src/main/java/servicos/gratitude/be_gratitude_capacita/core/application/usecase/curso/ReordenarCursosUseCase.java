package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Atualiza a ordem dos cursos conforme lista recebida.
 */
public class ReordenarCursosUseCase {

    private final CursoGateway cursoGateway;

    public ReordenarCursosUseCase(CursoGateway cursoGateway) {
        this.cursoGateway = cursoGateway;
    }

    public List<Curso> execute(List<Item> itens) {
        if (itens == null || itens.isEmpty()) return cursoGateway.findAll();
        // Map para lookup rápido
        Map<Integer, Integer> novaOrdem = itens.stream()
                .filter(i -> i.idCurso() != null && i.ordemCurso() != null)
                .collect(Collectors.toMap(Item::idCurso, Item::ordemCurso));

        List<Curso> todos = cursoGateway.findAll();
        for (Curso c : todos) {
            Integer nova = novaOrdem.get(c.getIdCurso());
            if (nova != null) {
                c.setOrdemCurso(nova);
            }
        }
        // Persistir um por um (gateway.save) para manter compatibilidade
        for (Curso c : todos) {
            cursoGateway.save(c);
        }
        // Retorna lista já ordenada asc
        return cursoGateway.findAll();
    }

    public static record Item(Integer idCurso, Integer ordemCurso) {}
}