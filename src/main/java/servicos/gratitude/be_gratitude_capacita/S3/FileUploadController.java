package servicos.gratitude.be_gratitude_capacita.S3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@RestController
@RequestMapping("/arquivos")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tipoBucket", defaultValue = "bronze") String tipoBucket) {
        logger.info("Recebendo upload de arquivo: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
        try {
            // Detectar tipo de arquivo e enviar para S3
            String contentType = file.getContentType();
            String url;
            
            if (contentType != null && contentType.equals("application/pdf")) {
                // Enviar PDF para S3
                logger.info("Detectado arquivo PDF, enviando para S3...");
                url = s3Service.uploadPdfFile(file);
            } else if (contentType != null && contentType.startsWith("image/")) {
                // Enviar imagem para S3
                logger.info("Detectado arquivo de imagem, enviando para S3...");
                url = s3Service.uploadCourseImage(file);
            } else {
                // Para outros tipos, também enviar como PDF (ou tratar como arquivo genérico)
                logger.info("Tipo de arquivo: {}, enviando como PDF para S3...", contentType);
                url = s3Service.uploadPdfFile(file);
            }
            
            logger.info("Upload para S3 realizado com sucesso. URL: {}", url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            logger.error("Erro ao enviar arquivo para S3", e);
            return ResponseEntity.status(500).body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    @PatchMapping("/apostila/{idApostila}/url")
    public ResponseEntity<String> patchUrlApostila(
            @PathVariable Integer idApostila,
            @RequestBody Map<String, String> body) {
        logger.info("=== PATCH REQUEST RECEBIDO ===");
        logger.info("ID da Apostila: {}", idApostila);
        logger.info("Body completo: {}", body);

        try {
            String url = body.get("url");
            logger.info("URL extraída: {}", url);

            if (url == null || url.trim().isEmpty()) {
                logger.error("ERRO: URL está vazia ou nula");
                return ResponseEntity.badRequest().body("URL não pode estar vazia");
            }

            logger.info("Chamando s3Service.patchAtualizarUrlArquivo...");
            s3Service.patchAtualizarUrlArquivo(idApostila, url);
            logger.info("Método do service executado com sucesso");

            String responseMessage = "URL da apostila atualizada com sucesso";
            logger.info("Resposta: {}", responseMessage);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            logger.error("ERRO no controller: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Erro ao atualizar URL: " + e.getMessage());
        }
    }
}