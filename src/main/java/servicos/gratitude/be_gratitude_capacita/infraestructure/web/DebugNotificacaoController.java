package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.dto.NotificacaoEmailDTO;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.producer.NotificacaoProducer;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.UsuarioRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CursoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/debug/notificacao")
public class DebugNotificacaoController {
    private static final Logger LOG = LoggerFactory.getLogger(DebugNotificacaoController.class);

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final NotificacaoProducer notificacaoProducer;
    @Value("${mailhog.api.base:http://localhost:8025}")
    private String mailhogApiBase;

    public DebugNotificacaoController(UsuarioRepository usuarioRepository,
                                      CursoRepository cursoRepository,
                                      NotificacaoProducer notificacaoProducer) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.notificacaoProducer = notificacaoProducer;
    }

    public static class TestNotificacaoRequest {
        public Integer idCurso;
        public String tituloCurso;
        public String descricaoCurso;

        public TestNotificacaoRequest() {}
    }

    public static class EmailNotificacaoRequest {
        public Integer idCurso;
        public String tituloCurso;
        public String descricaoCurso;
        public String emailAluno;
        public String nomeAluno;

        public EmailNotificacaoRequest() {}
    }

    @PostMapping("/enviar-para-todos")
    public ResponseEntity<?> enviarParaTodos(@RequestBody(required = false) TestNotificacaoRequest req) {
        Integer idCurso = (req != null) ? req.idCurso : null;
        String titulo = (req != null && req.tituloCurso != null) ? req.tituloCurso : null;
        String descricao = (req != null && req.descricaoCurso != null) ? req.descricaoCurso : null;

        // se veio apenas idCurso, buscar título/descrição no repositório
        if (idCurso != null && (titulo == null || descricao == null)) {
            var cOpt = cursoRepository.findById(idCurso);
            if (cOpt.isPresent()) {
                CursoEntity c = cOpt.get();
                if (titulo == null) titulo = c.getTituloCurso();
                if (descricao == null) descricao = c.getDescricao();
            }
        }

        // valores default caso ainda não tenhamos título/descrição
        if (titulo == null) titulo = "Curso de Teste";
        if (descricao == null) descricao = "Descrição do curso de teste.";

        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        int sent = 0;
        for (UsuarioEntity u : usuarios) {
            if (u.getEmail() == null || u.getEmail().isBlank()) continue;

            NotificacaoEmailDTO dto = new NotificacaoEmailDTO(
                    idCurso,
                    titulo,
                    descricao,
                    u.getEmail(),
                    u.getNome(),
                    System.currentTimeMillis()
            );

            try {
                notificacaoProducer.enviarNotificacao(dto);
                sent++;
            } catch (Exception e) {
                LOG.error("Falha ao enviar notificação para {}: {}", u.getEmail(), e.getMessage());
            }
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("totalUsuarios", usuarios.size());
        resp.put("enviadas", sent);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/mailhog/messages")
    public ResponseEntity<?> getMailhogMessages() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(new URI(mailhogApiBase + "/api/v2/messages")).GET().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(resp.body(), Object.class);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            LOG.error("Falha ao consultar MailHog: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/mailhog/messages")
    public ResponseEntity<?> deleteMailhogMessages() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(new URI(mailhogApiBase + "/api/v1/messages")).method("DELETE", HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            return ResponseEntity.ok(Map.of("deleted", true));
        } catch (Exception e) {
            LOG.error("Falha ao apagar mensagens no MailHog: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/enviar-para-usuario/{idUsuario}")
    public ResponseEntity<?> enviarParaUsuario(@PathVariable Integer idUsuario,
                                               @RequestBody(required = false) TestNotificacaoRequest req) {
        try {
            var usuarioOpt = usuarioRepository.findById(idUsuario);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuário não encontrado"));
            }

            UsuarioEntity u = usuarioOpt.get();
            if (u.getEmail() == null || u.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuário não possui email cadastrado"));
            }

            Integer idCurso = (req != null) ? req.idCurso : null;
            String titulo = (req != null && req.tituloCurso != null) ? req.tituloCurso : null;
            String descricao = (req != null && req.descricaoCurso != null) ? req.descricaoCurso : null;

            if (idCurso != null && (titulo == null || descricao == null)) {
                var cOpt = cursoRepository.findById(idCurso);
                if (cOpt.isPresent()) {
                    CursoEntity c = cOpt.get();
                    if (titulo == null) titulo = c.getTituloCurso();
                    if (descricao == null) descricao = c.getDescricao();
                }
            }

            if (titulo == null) titulo = "Curso de Teste";
            if (descricao == null) descricao = "Descrição do curso de teste.";

            NotificacaoEmailDTO dto = new NotificacaoEmailDTO(
                    idCurso,
                    titulo,
                    descricao,
                    u.getEmail(),
                    u.getNome(),
                    System.currentTimeMillis()
            );

            notificacaoProducer.enviarNotificacao(dto);

            Map<String, Object> resp = new HashMap<>();
            resp.put("enviadas", 1);
            resp.put("email", u.getEmail());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            LOG.error("Falha ao enviar notificação para usuario {}: {}", idUsuario, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/enviar-para-email")
    public ResponseEntity<?> enviarParaEmail(@RequestBody EmailNotificacaoRequest req) {
        try {
            if (req == null || req.emailAluno == null || req.emailAluno.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campo emailAluno é obrigatório"));
            }

            Integer idCurso = req.idCurso;
            String titulo = (req.tituloCurso != null) ? req.tituloCurso : null;
            String descricao = (req.descricaoCurso != null) ? req.descricaoCurso : null;

            if (idCurso != null && (titulo == null || descricao == null)) {
                var cOpt = cursoRepository.findById(idCurso);
                if (cOpt.isPresent()) {
                    CursoEntity c = cOpt.get();
                    if (titulo == null) titulo = c.getTituloCurso();
                    if (descricao == null) descricao = c.getDescricao();
                }
            }

            if (titulo == null) titulo = "Curso de Teste";
            if (descricao == null) descricao = "Descrição do curso de teste.";

            NotificacaoEmailDTO dto = new NotificacaoEmailDTO(
                    req.idCurso,
                    titulo,
                    descricao,
                    req.emailAluno,
                    req.nomeAluno != null ? req.nomeAluno : req.emailAluno,
                    System.currentTimeMillis()
            );

            notificacaoProducer.enviarNotificacao(dto);

            return ResponseEntity.ok(Map.of("enviadas", 1, "email", req.emailAluno));
        } catch (Exception e) {
            LOG.error("Falha ao enviar notificação para email {}: {}", req != null ? req.emailAluno : "-", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
