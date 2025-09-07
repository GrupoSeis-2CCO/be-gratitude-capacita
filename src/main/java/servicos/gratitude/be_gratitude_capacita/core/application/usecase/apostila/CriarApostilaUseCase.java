package servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila;

import servicos.gratitude.be_gratitude_capacita.core.application.command.apostila.CriarApostilaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;

import java.time.LocalDateTime;
import java.util.List;

public class CriarApostilaUseCase {
    private final ApostilaGateway apostilaGateway;
    private final CursoGateway cursoGateway;

    public CriarApostilaUseCase(ApostilaGateway apostilaGateway, CursoGateway cursoGateway) {
        this.apostilaGateway = apostilaGateway;
        this.cursoGateway = cursoGateway;
    }

    public Apostila execute(CriarApostilaCommand command){
        if (apostilaGateway.existsByNome(command.nomeApostila())){
            throw new ConflitoException("Já existe uma apostila com o nome informado");
        } else if (!cursoGateway.existsById(command.fkCurso())){
            throw new NaoEncontradoException("Não foi encontrado um curso com o id informado");
        }

        Curso curso = cursoGateway.findById(command.fkCurso());

        List<Apostila> apostilasDoCurso = apostilaGateway.findAllByCurso(curso);
        Integer maiorOrdem = 0;

        for (Apostila apostilaDaVez : apostilasDoCurso) {
            if (apostilaDaVez.getOrdemApostila() > maiorOrdem){
                maiorOrdem = apostilaDaVez.getOrdemApostila();
            }
        }

        Apostila apostila = new Apostila();

        apostila.setNomeApostilaOriginal(command.nomeApostila());
        apostila.setDescricaoApostila(command.descricaoApostila());
        apostila.setTamanhoBytes(command.tamanhoBytes());
        apostila.setFkCurso(curso);

        apostila.setNomeApostilaArmazenamento(command.nomeApostila() + LocalDateTime.now());
        apostila.setDataPostadoApostila(LocalDateTime.now());
        apostila.setDataAtualizacaoApostila(LocalDateTime.now());
        apostila.setApostilaOculto(true);
        apostila.setOrdemApostila(maiorOrdem);

        return apostilaGateway.save(apostila);
    }
}
