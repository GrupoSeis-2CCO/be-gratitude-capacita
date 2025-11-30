package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.dto.NotificacaoEmailDTO;
import servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.producer.NotificacaoProducer;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.UsuarioEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CursoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.UsuarioRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CursoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller para envio de notificações por email via RabbitMQ.
 * Remove dependências do MailHog e utiliza apenas o sistema de mensageria.
 */
@RestController
@RequestMapping("/api/email-notifications")
public class EmailNotificationController {
    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationController.class);

    // ID do cargo de colaborador (conforme data.sql)
    private static final int CARGO_COLABORADOR = 2;

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final NotificacaoProducer notificacaoProducer;

    public EmailNotificationController(UsuarioRepository usuarioRepository,
                                       CursoRepository cursoRepository,
                                       NotificacaoProducer notificacaoProducer) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.notificacaoProducer = notificacaoProducer;
    }

    // ========== Request DTOs ==========

    public static class CourseNotificationRequest {
        public Integer idCurso;
        public String tituloCurso;
        public String descricaoCurso;

        public CourseNotificationRequest() {}
    }

    public static class EmailNotificationRequest {
        public Integer idCurso;
        public String tituloCurso;
        public String descricaoCurso;
        public String emailAluno;
        public String nomeAluno;

        public EmailNotificationRequest() {}
    }

    // ========== Endpoints ==========

    /**
     * Lista todos os colaboradores disponíveis para receber notificações.
     */
    @GetMapping("/collaborators")
    public ResponseEntity<?> getCollaborators() {
        try {
            List<UsuarioEntity> colaboradores = usuarioRepository.findByFkCargo_IdCargo(CARGO_COLABORADOR);
            
            List<Map<String, Object>> result = colaboradores.stream()
                .map(u -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idUsuario", u.getIdUsuario());
                    map.put("nome", u.getNome());
                    map.put("email", u.getEmail());
                    map.put("departamento", u.getDepartamento());
                    return map;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                "total", result.size(),
                "collaborators", result
            ));
        } catch (Exception e) {
            LOG.error("Erro ao buscar colaboradores: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao buscar colaboradores: " + e.getMessage()));
        }
    }

    /**
     * Lista todos os cursos disponíveis para notificação.
     */
    @GetMapping("/courses")
    public ResponseEntity<?> getCourses() {
        try {
            List<CursoEntity> cursos = cursoRepository.findAll();
            
            List<Map<String, Object>> result = cursos.stream()
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idCurso", c.getIdCurso());
                    map.put("tituloCurso", c.getTituloCurso());
                    map.put("descricao", c.getDescricao());
                    map.put("oculto", c.getOcultado());
                    return map;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOG.error("Erro ao buscar cursos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao buscar cursos: " + e.getMessage()));
        }
    }

    /**
     * Envia notificação de novo curso para TODOS os colaboradores do sistema.
     * Este é o endpoint principal para notificar sobre novos cursos.
     */
    @PostMapping("/send-to-collaborators")
    public ResponseEntity<?> sendToAllCollaborators(@RequestBody CourseNotificationRequest req) {
        try {
            if (req == null || req.idCurso == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Campo idCurso é obrigatório"));
            }

            // Buscar dados do curso
            String titulo = req.tituloCurso;
            String descricao = req.descricaoCurso;

            var cursoOpt = cursoRepository.findById(req.idCurso);
            if (cursoOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Curso não encontrado com ID: " + req.idCurso));
            }

            CursoEntity curso = cursoOpt.get();
            if (titulo == null) titulo = curso.getTituloCurso();
            if (descricao == null) descricao = curso.getDescricao();

            // Buscar apenas colaboradores (cargo = 2)
            List<UsuarioEntity> colaboradores = usuarioRepository.findByFkCargo_IdCargo(CARGO_COLABORADOR);
            
            int sent = 0;
            int skipped = 0;
            
            for (UsuarioEntity u : colaboradores) {
                if (u.getEmail() == null || u.getEmail().isBlank()) {
                    skipped++;
                    continue;
                }

                NotificacaoEmailDTO dto = new NotificacaoEmailDTO(
                    req.idCurso,
                    titulo,
                    descricao,
                    u.getEmail(),
                    u.getNome(),
                    System.currentTimeMillis()
                );

                try {
                    notificacaoProducer.enviarNotificacao(dto);
                    sent++;
                    LOG.info("Notificação enfileirada para colaborador: {} ({})", u.getNome(), u.getEmail());
                } catch (Exception e) {
                    LOG.error("Falha ao enfileirar notificação para {}: {}", u.getEmail(), e.getMessage());
                    skipped++;
                }
            }

            LOG.info("Notificações enviadas: {} de {} colaboradores para o curso '{}'", 
                sent, colaboradores.size(), titulo);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "totalCollaborators", colaboradores.size(),
                "sent", sent,
                "skipped", skipped,
                "course", Map.of(
                    "idCurso", req.idCurso,
                    "titulo", titulo
                )
            ));
        } catch (Exception e) {
            LOG.error("Erro ao enviar notificações para colaboradores: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao enviar notificações: " + e.getMessage()));
        }
    }

    /**
     * Envia notificação para um colaborador específico por ID.
     */
    @PostMapping("/send-to-user/{idUsuario}")
    public ResponseEntity<?> sendToUser(@PathVariable Integer idUsuario,
                                        @RequestBody(required = false) CourseNotificationRequest req) {
        try {
            var usuarioOpt = usuarioRepository.findById(idUsuario);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuário não encontrado"));
            }

            UsuarioEntity u = usuarioOpt.get();
            if (u.getEmail() == null || u.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Usuário não possui email cadastrado"));
            }

            // Verificar se é colaborador
            if (u.getFkCargo() == null || u.getFkCargo().getIdCargo() != CARGO_COLABORADOR) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Usuário não é um colaborador"));
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

            if (titulo == null) titulo = "Novo Curso Disponível";
            if (descricao == null) descricao = "Confira o novo curso disponível na plataforma!";

            NotificacaoEmailDTO dto = new NotificacaoEmailDTO(
                idCurso,
                titulo,
                descricao,
                u.getEmail(),
                u.getNome(),
                System.currentTimeMillis()
            );

            notificacaoProducer.enviarNotificacao(dto);

            LOG.info("Notificação enviada para usuário: {} ({})", u.getNome(), u.getEmail());

            return ResponseEntity.ok(Map.of(
                "success", true,
                "sent", 1,
                "email", u.getEmail(),
                "nome", u.getNome()
            ));
        } catch (Exception e) {
            LOG.error("Falha ao enviar notificação para usuário {}: {}", idUsuario, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao enviar notificação: " + e.getMessage()));
        }
    }

    /**
     * Envia notificação para um email específico (para testes ou emails externos).
     */
    @PostMapping("/send-to-email")
    public ResponseEntity<?> sendToEmail(@RequestBody EmailNotificationRequest req) {
        try {
            if (req == null || req.emailAluno == null || req.emailAluno.isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Campo emailAluno é obrigatório"));
            }

            Integer idCurso = req.idCurso;
            String titulo = req.tituloCurso;
            String descricao = req.descricaoCurso;

            if (idCurso != null && (titulo == null || descricao == null)) {
                var cOpt = cursoRepository.findById(idCurso);
                if (cOpt.isPresent()) {
                    CursoEntity c = cOpt.get();
                    if (titulo == null) titulo = c.getTituloCurso();
                    if (descricao == null) descricao = c.getDescricao();
                }
            }

            if (titulo == null) titulo = "Novo Curso Disponível";
            if (descricao == null) descricao = "Confira o novo curso disponível na plataforma!";

            NotificacaoEmailDTO dto = new NotificacaoEmailDTO(
                idCurso,
                titulo,
                descricao,
                req.emailAluno,
                req.nomeAluno != null ? req.nomeAluno : req.emailAluno,
                System.currentTimeMillis()
            );

            notificacaoProducer.enviarNotificacao(dto);

            LOG.info("Notificação enviada para email: {}", req.emailAluno);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "sent", 1,
                "email", req.emailAluno
            ));
        } catch (Exception e) {
            LOG.error("Falha ao enviar notificação para email {}: {}", 
                req != null ? req.emailAluno : "-", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao enviar notificação: " + e.getMessage()));
        }
    }

    /**
     * Verifica o status do sistema de mensageria (health check).
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            // Verificar se conseguimos acessar os repositórios
            long userCount = usuarioRepository.count();
            long courseCount = cursoRepository.count();
            long collaboratorCount = usuarioRepository.findByFkCargo_IdCargo(CARGO_COLABORADOR).size();

            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "messaging", "RabbitMQ",
                "stats", Map.of(
                    "totalUsers", userCount,
                    "totalCourses", courseCount,
                    "totalCollaborators", collaboratorCount
                )
            ));
        } catch (Exception e) {
            LOG.error("Health check falhou: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                    "status", "DOWN",
                    "error", e.getMessage()
                ));
        }
    }
}
