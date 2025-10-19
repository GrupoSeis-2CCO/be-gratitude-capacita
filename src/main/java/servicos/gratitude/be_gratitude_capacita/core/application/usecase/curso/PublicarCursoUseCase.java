package servicos.gratitude.be_gratitude_capacita.core.application.usecase.curso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servicos.gratitude.be_gratitude_capacita.core.application.command.curso.PublicarCursoCommand;
import servicos.gratitude.be_gratitude_capacita.core.application.exception.NaoEncontradoException;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.CursoGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;
import servicos.gratitude.be_gratitude_capacita.core.gateways.UsuarioGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.dto.NotificacaoEmailDTO;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.producer.NotificacaoProducer;

import java.util.List;
import java.util.Objects;

public class PublicarCursoUseCase {
    private static final Logger logger = LoggerFactory.getLogger(PublicarCursoUseCase.class);

    private final CursoGateway cursoGateway;
    private final MatriculaGateway matriculaGateway;
    private final UsuarioGateway usuarioGateway;
    private final NotificacaoProducer notificacaoProducer;

    public PublicarCursoUseCase(
            CursoGateway cursoGateway,
            MatriculaGateway matriculaGateway,
            UsuarioGateway usuarioGateway,
            NotificacaoProducer notificacaoProducer) {
        this.cursoGateway = cursoGateway;
        this.matriculaGateway = matriculaGateway;
        this.usuarioGateway = usuarioGateway;
        this.notificacaoProducer = notificacaoProducer;
    }

    public Curso execute(PublicarCursoCommand command) {
        // Validar comando
        if (Objects.isNull(command) || Objects.isNull(command.idCurso())) {
            throw new IllegalArgumentException("ID do curso é obrigatório");
        }

        // Buscar curso
        Curso curso = cursoGateway.findById(command.idCurso());
        if (Objects.isNull(curso)) {
            throw new NaoEncontradoException("Curso não encontrado com ID: " + command.idCurso());
        }

        // Descultar o curso
        curso.setOcultado(false);
        Curso cursoPublicado = cursoGateway.save(curso);

        // Enviar notificações se solicitado
        if (command.notificarTodos() != null && command.notificarTodos()) {
            enviarNotificacoesTodos(cursoPublicado);
        } else if (command.idsAlunosSelecionados() != null && !command.idsAlunosSelecionados().isEmpty()) {
            enviarNotificacoesSelecionados(cursoPublicado, command.idsAlunosSelecionados());
        }

        logger.info("Curso {} publicado com sucesso", cursoPublicado.getIdCurso());
        return cursoPublicado;
    }

    private void enviarNotificacoesTodos(Curso curso) {
        try {
            List<Matricula> matriculas = matriculaGateway.findByCurso(curso);
            logger.info("Enviando notificações para {} alunos do curso {}", matriculas.size(), curso.getIdCurso());

            for (Matricula matricula : matriculas) {
                if (matricula.getUsuario() != null && matricula.getUsuario().getEmail() != null) {
                    criarEEnviarNotificacao(curso, matricula.getUsuario());
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar notificações para todos os alunos: {}", e.getMessage(), e);
        }
    }

    private void enviarNotificacoesSelecionados(Curso curso, List<Integer> idsAlunos) {
        try {
            logger.info("Enviando notificações para {} alunos selecionados do curso {}", 
                idsAlunos.size(), curso.getIdCurso());

            for (Integer idAluno : idsAlunos) {
                Usuario usuario = usuarioGateway.findById(idAluno);
                if (usuario != null && usuario.getEmail() != null) {
                    criarEEnviarNotificacao(curso, usuario);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar notificações para alunos selecionados: {}", e.getMessage(), e);
        }
    }

    private void criarEEnviarNotificacao(Curso curso, Usuario usuario) {
        NotificacaoEmailDTO notificacao = new NotificacaoEmailDTO(
            curso.getIdCurso(),
            curso.getTituloCurso(),
            curso.getDescricao(),
            usuario.getEmail(),
            usuario.getNome(),
            System.currentTimeMillis()
        );
        notificacaoProducer.enviarNotificacao(notificacao);
    }
}
