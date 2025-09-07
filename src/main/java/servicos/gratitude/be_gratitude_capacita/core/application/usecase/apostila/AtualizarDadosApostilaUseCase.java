package servicos.gratitude.be_gratitude_capacita.core.application.usecase.apostila;

import servicos.gratitude.be_gratitude_capacita.core.application.command.apostila.AtualizarApostilaCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.ConflitoException;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;
import servicos.gratitude.be_gratitude_capacita.core.gateways.ApostilaGateway;

import java.time.LocalDateTime;
import java.util.Objects;

public class AtualizarDadosApostilaUseCase {
    private final ApostilaGateway apostilaGateway;

    public AtualizarDadosApostilaUseCase(ApostilaGateway apostilaGateway) {
        this.apostilaGateway = apostilaGateway;
    }

    public Apostila execute(AtualizarApostilaCommand command, Integer idApostila){
        if (!apostilaGateway.existsById(idApostila)){
            throw new NaoEncontradoException("Não foi encontrado apostila com o id informado");
        }

        Apostila apostilaComMesmoNome = apostilaGateway.findByNome(command.nomeApostila());
        if (Objects.nonNull(apostilaComMesmoNome) && apostilaComMesmoNome.getIdApostila() != idApostila){
            throw new ConflitoException("Já existe uma apostila com esse nome");
        }

        Apostila apostilaDoBanco = apostilaGateway.findById(idApostila);
        Apostila apostilaAtualizada = new Apostila();

        apostilaAtualizada.setIdApostila(idApostila);
        apostilaAtualizada.setNomeApostilaOriginal(command.nomeApostila());
        apostilaAtualizada.setDescricaoApostila(command.descricaoApostila());
        apostilaAtualizada.setOrdemApostila(command.ordem());
        apostilaAtualizada.setDataAtualizacaoApostila(LocalDateTime.now());

        apostilaAtualizada.setTamanhoBytes(apostilaDoBanco.getTamanhoBytes());
        apostilaAtualizada.setFkCurso(apostilaDoBanco.getFkCurso());
        apostilaAtualizada.setNomeApostilaArmazenamento(apostilaDoBanco.getNomeApostilaArmazenamento());
        apostilaAtualizada.setDataPostadoApostila(apostilaDoBanco.getDataPostadoApostila());
        apostilaAtualizada.setApostilaOculto(apostilaDoBanco.getApostilaOculto());

        return apostilaGateway.save(apostilaAtualizada);
    }
}
