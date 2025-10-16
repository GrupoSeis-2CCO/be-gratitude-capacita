package servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario;

import servicos.gratitude.be_gratitude_capacita.core.application.exception.ValorInvalidoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.RespostaDoUsuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.RespostaDoUsuarioGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListarRespostasDoUsuarioUseCase {
    private final RespostaDoUsuarioGateway respostaDoUsuarioGateway;

    public ListarRespostasDoUsuarioUseCase(RespostaDoUsuarioGateway respostaDoUsuarioGateway) {
        this.respostaDoUsuarioGateway = respostaDoUsuarioGateway;
    }

    public List<RespostaDoUsuario> execute(Usuario usuario){
        if (Objects.isNull(usuario)){
            throw new ValorInvalidoException("Valores inválidos para campos obrigatórios");
        }

        List<RespostaDoUsuario> respostas = respostaDoUsuarioGateway.findAll();
        List<RespostaDoUsuario> respostasPorUsuario = new ArrayList<>();

        if (respostas == null || respostas.isEmpty()) return respostasPorUsuario;

        Integer idUsuarioRef = usuario.getIdUsuario();

        for (RespostaDoUsuario respostaDaVez : respostas) {
            if (respostaDaVez == null) continue;
            var tentativa = respostaDaVez.getTentativa();
            if (tentativa == null) continue;
            var matricula = tentativa.getMatricula();
            if (matricula == null) continue;
            var usuarioResp = matricula.getUsuario();
            if (usuarioResp == null) continue;
            Integer idUsuarioResp = usuarioResp.getIdUsuario();
            if (idUsuarioResp != null && idUsuarioRef != null && idUsuarioResp.equals(idUsuarioRef)){
                respostasPorUsuario.add(respostaDaVez);
            }
        }

        return respostasPorUsuario;
    }
}
